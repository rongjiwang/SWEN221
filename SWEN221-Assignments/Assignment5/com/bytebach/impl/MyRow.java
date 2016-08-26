package com.bytebach.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import com.bytebach.model.BooleanValue;
import com.bytebach.model.Field;
import com.bytebach.model.IntegerValue;
import com.bytebach.model.InvalidOperation;
import com.bytebach.model.ReferenceValue;
import com.bytebach.model.StringValue;
import com.bytebach.model.Table;
import com.bytebach.model.Value;

public class MyRow implements List<Value> {

	private Value[] data;
	private int size = 0;
	public final MyRows parent;
	private List<Field> schema;
	private List<Integer> keyValueIndexes; // stores all the indexes of key
											// fields in a schema

	public MyRow(MyRows parent) {
		this.parent = parent;
		schema = this.parent.parent.fields();
		data = new Value[schema.size()];
		keyValueIndexes = this.parent.parent.getKeyValueIndexes();
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public boolean contains(Object o) {
		if (!(o instanceof Value))
			return false;

		for (Value value : data) {
			if (value.equals(o))
				return true;
		}

		return false; // nothing match
	}

	@Override
	public Iterator<Value> iterator() {
		return new Iter();
	}

	@Override
	public Object[] toArray() {
		return Arrays.copyOf(data, size);
	}

	@Override
	public <T> T[] toArray(T[] a) {
		throw new InvalidOperation("toArray operation not allowed.");
	}

	@Override
	public boolean add(Value e) {
		throw new InvalidOperation("Cannot add a value to a row.");
	}

	@Override
	public boolean remove(Object o) {
		throw new InvalidOperation("Cannot remove a value from a row.");
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throw new InvalidOperation("containsAll operation not allowed.");
	}

	@Override
	public boolean addAll(Collection<? extends Value> c) {
		throw new InvalidOperation("Cannot add values to a row.");
	}

	@Override
	public boolean addAll(int index, Collection<? extends Value> c) {
		throw new InvalidOperation("Cannot add values to a row.");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new InvalidOperation("Cannot remove a value from a row.");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new InvalidOperation("retainAll operation not allowed.");
	}

	@Override
	public void clear() {
		size = 0;
	}

	@Override
	public Value get(int index) {
		if (index < 0 || index > size)
			throw new ArrayIndexOutOfBoundsException();

		return data[index];
	}

	@Override
	public Value set(int index, Value element) {
		if (index < 0 || index > schema.size())
			throw new InvalidOperation("Index exceed row size");

		checkElemType(index, element); // check if all types match schema before
										// setting

		// check if it's a key value before setting
		if (keyValueIndexes.contains(index))
			throw new InvalidOperation("Cannot set key value");

		// check if it's a reference value before setting
		if (element instanceof ReferenceValue) {
			checkRefSet(index, element);
		}

		Value oldValue = data[index];
		data[index] = element;

		return oldValue;
	}

	/**
	 * Method called by set method if the element added in is a refereneValue,
	 * it will check whether the ref table exist and the ref row exist
	 * 
	 * @param index:
	 *            position in the row element goes in
	 * @param element
	 *            to be added in
	 */
	private void checkRefSet(int index, com.bytebach.model.Value element) {
		ReferenceValue refE = (ReferenceValue) element;
		String tableName = refE.table();
		Value[] keyValue = refE.keys();
		if (keyValue == null || keyValue.length == 0)
			throw new InvalidOperation("Element does not contain key Value, therefore is invalid");

		Table refTable = this.parent.parent.parent.table(tableName);

		if (refTable == null)
			throw new InvalidOperation("Table " + tableName + " referred to does not exist");

		boolean matchK = true;
		for (Iterator itr = refTable.rows().iterator(); itr.hasNext();) {
			List<Value> rowTocheck = (List<Value>) itr.next();
			matchK = matchKeys(refTable, rowTocheck, keyValue);
			if (matchK)
				break;
		}

		if (!matchK)
			throw new InvalidOperation("Keyvalue referred to does not match");
	}

	/**
	 * private method called by checkRefSet method, will check wither the
	 * ReferenceValue contains the accurate key Value
	 * 
	 * @param refTo:
	 *            table referred to
	 * @param row
	 *            contained in the table need to checked
	 * @param key
	 *            value contained in new referenceValue
	 * @return true if matches the keys
	 */
	private boolean matchKeys(Table refTable, List<Value> row, Value... keys) {
		List<Integer> ki = ((MyTable) refTable).getKeyValueIndexes();
		if (ki.size() != keys.length)
			throw new InvalidOperation("Table contains " + keyValueIndexes.size() + " keys, ref element contains "
					+ keys.length + " keys");

		boolean match = true;
		for (Value v : keys) {
			if (!row.contains(v))
				match = false;
		}
		return match;
	}

	/**
	 * Method get called by set method, check the new element type matches field
	 * column if not match, will throw InvalidOperation
	 * 
	 * @param index:
	 *            position to add to row
	 * @param element:
	 *            new element to be added
	 */
	private void checkElemType(int index, com.bytebach.model.Value element) {
		Field.Type ft = schema.get(index).type();
		Class<?> vt = element.getClass();

		if (ft == Field.Type.BOOLEAN && vt != BooleanValue.class)
			throw new InvalidOperation("Expecting a boolean value, but element type is " + vt.toString() + ".");
		if (ft == Field.Type.INTEGER && vt != IntegerValue.class)
			throw new InvalidOperation("Expecting an integer value, but element type is " + vt.toString() + ".");
		if (ft == Field.Type.REFERENCE && vt != ReferenceValue.class)
			throw new InvalidOperation("Expecting a reference value, but element type is " + vt.toString() + ".");
		if (ft == Field.Type.TEXT && vt != StringValue.class)
			throw new InvalidOperation("Expecting a string value, but element type is " + vt.toString() + ".");
		if (ft == Field.Type.TEXT && vt == StringValue.class) {
			String newline = System.getProperty("line.separator");
			if (((StringValue) element).value().contains(newline))
				throw new InvalidOperation("Expecting a text value, but element type contains '\n'.");
		}
		if (ft == Field.Type.TEXTAREA && vt != StringValue.class)
			throw new InvalidOperation(
					"Expecting a string value(textArea), but element type is " + vt.toString() + ".");
	}

	@Override
	public void add(int index, Value element) {
		throw new InvalidOperation("Cannot add a value to a row.");
	}

	@Override
	public Value remove(int index) {
		throw new InvalidOperation("Cannot remove a value from a row.");
	}

	@Override
	public int indexOf(Object o) {
		if (o == null) {
			for (int i = 0; i < size; i++)
				if (data[i] == null)
					return i;
		} else {
			for (int i = 0; i < size; i++)
				if (o.equals(data[i]))
					return i;
		}
		return -1;
	}

	@Override
	public int lastIndexOf(Object o) {
		if (o == null) {
			for (int i = size - 1; i >= 0; i--)
				if (data[i] == null)
					return i;
		} else {
			for (int i = size - 1; i >= 0; i--)
				if (o.equals(data[i]))
					return i;
		}
		return -1;
	}

	@Override
	public ListIterator<Value> listIterator() {
		throw new InvalidOperation("Iterator not allowed");
	}

	@Override
	public ListIterator<Value> listIterator(int index) {
		throw new InvalidOperation("Iterator not allowed");
	}

	@Override
	public List<Value> subList(int fromIndex, int toIndex) {
		throw new InvalidOperation("subList not allowed");
	}

	public void noSenseSecretInput(Value value) {
		if (value == null)
			return;
		data[size++] = value;
	}

	private class Iter<Value> implements Iterator<Value> {
		int cursor; // index of next element to return
		int lastRet = -1; // index of last element returned; -1 if no such

		@Override
		public boolean hasNext() {
			return cursor != size;
		}

		@Override
		public Value next() {
			int i = cursor;
			if (i >= size)
				throw new NoSuchElementException();
			// Object[] data = ArrayList.this.data;
			if (i >= data.length)
				throw new ConcurrentModificationException();
			cursor = i + 1;
			return (Value) data[lastRet = i];
		}

		@Override
		public void remove() {
			if (lastRet < 0)
				throw new IllegalStateException();

			try {
				MyRow.this.remove(lastRet);
				cursor = lastRet;
				lastRet = -1;
			} catch (IndexOutOfBoundsException e) {
				throw new ConcurrentModificationException();
			}
		}

	}
}
