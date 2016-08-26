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

public class MyRows implements List<List<Value>> {

	private static int INITIALCAPACITY = 50;

	private MyRow[] data;
	private int size = 0;
	public final MyTable parent;
	private List<Field> schema;
	private List<Integer> keyValueIndexes;     // stores all the indexes of key fields in a schema

	public MyRows(MyTable parent) {
		this.parent = parent;
		schema = this.parent.fields();
		data = new MyRow[INITIALCAPACITY];
		keyValueIndexes = this.parent.getKeyValueIndexes();
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
		if (!(o instanceof MyRow))
			return false;

		for (MyRow row : data) {
			if (row.equals(o))
				return true;
		}

		return false; // nothing match
	}

	@Override
	public Iterator<List<Value>> iterator() {
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
	public boolean add(List<Value> e) {
		if (e == null)
			throw new InvalidOperation("Can't add null to a table.");

		if (e.size() != schema.size())
			throw new InvalidOperation("The row's length doesn't match the schema's length.");

		for (Integer i: keyValueIndexes) {
			Value keyValue = e.get(i);
			if (keysExist(keyValue))
				throw new InvalidOperation("Keys already exist in the table.");
		}

		for (int i = 0; i < e.size(); i++) {
			if (!matchValueType(e.get(i), i))
				throw new InvalidOperation("Unexpected value type.");
			if (e.get(i) instanceof ReferenceValue)
				checkRefSet(i, e.get(i));
		}

		if (size == data.length)
			ensureCapacity(size + 1);

		MyRow row = new MyRow(this);
		for (int i = 0; i < e.size(); i++)
			row.noSenseSecretInput(e.get(i));
		data[size++] = row;
		return true;
	}

	@Override
	public boolean remove(Object o) {
		if (o == null) {
			for (int index = 0; index < size; index++)
				if (data[index] == null) {
					int numMoved = size - index - 1;
					if (numMoved > 0)
						System.arraycopy(data, index+1, data, index, numMoved);
					data[--size] = null;
					return true;
				}
		} else {
			for (int index = 0; index < size; index++)
				if (o.equals(data[index])) {
					int numMoved = size - index - 1;
					if (numMoved > 0)
						System.arraycopy(data, index+1, data, index, numMoved);
					data[--size] = null;
					return true;
				}
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throw new InvalidOperation("containsAll operation not allowed.");
	}

	@Override
	public boolean addAll(Collection<? extends List<Value>> c) {
		throw new InvalidOperation("addAll operation not allowed.");
	}

	@Override
	public boolean addAll(int index, Collection<? extends List<Value>> c) {
		throw new InvalidOperation("addAll operation not allowed.");
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new InvalidOperation("removeAll operation not allowed.");
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
	public List<Value> get(int index) {
		if (index < 0 || index > size)
			throw new ArrayIndexOutOfBoundsException();
		return data[index];
	}

	@Override
	public List<Value> set(int index, List<Value> element) {
		if (index < 0 || index > size)
			throw new ArrayIndexOutOfBoundsException();

		if (element == null)
			throw new InvalidOperation("Can't set a row to null");

		for (int i = 0; i < element.size(); i++) {
			if (!matchValueType(element.get(i), i))
				throw new InvalidOperation("Wrong type");
		}

		List<Value> oldValue = data[index];

		MyRow row = new MyRow(this);
		for (int i = 0; i < element.size(); i++)
			row.noSenseSecretInput(element.get(i));

		data[index] = row;
		return oldValue;
	}

	@Override
	public void add(int index, List<Value> element) {
		throw new InvalidOperation("cannot add by index.");
	}

	@Override
	public List<Value> remove(int index) {
		if (index < 0 || index > size)
			throw new IndexOutOfBoundsException();
		if (isEmpty())
			throw new InvalidOperation("Nothing can be removed.");

		List<Value> toRemove= data[index];

		for (int i = index; i < data.length - 1; i++)
			data[i] = data[i+1];

		size--;
		return toRemove;
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
	public ListIterator<List<Value>> listIterator() {
		throw new InvalidOperation("Iterator not allowed");
	}

	@Override
	public ListIterator<List<Value>> listIterator(int index) {
		throw new InvalidOperation("Iterator not allowed");
	}

	@Override
	public List<List<Value>> subList(int fromIndex, int toIndex) {
		throw new InvalidOperation("sublist not allowed");
	}

	public void ensureCapacity(int minCapacity) {
		int current = data.length;
		if (minCapacity > current) {
			MyRow[] newData = new MyRow[Math.max(current * 2, minCapacity)];
			System.arraycopy(data, 0, newData, 0, size);
			data = newData;
		}
	}

	/**
	 * check if the given key is already in the table
	 *
	 * @param value
	 * @return true if the key already exist in the table
	 */
	private boolean keysExist(Value value) {

		if (value == null)
			throw new InvalidOperation("Key value in row to be added is null");

		for (Integer ind: keyValueIndexes){
			for (Iterator itr = this.parent.rows().iterator(); itr.hasNext();){
				List<Value> rowToCheck = (List<Value>) itr.next();
				if (rowToCheck.contains(value))
					return true;
			}
		}

		return false;
	}

	/**
	 * Method get called by set method, check the new element type matches field column
	 * if not match, will throw InvalidOperation
	 * @param index: position to add to row
	 * @param element: new element to be added
	 */
	private boolean matchValueType(Value value, int index) {
		Field.Type fieldType = schema.get(index).type();
		Class<?> valueType = value.getClass();

		if (fieldType == Field.Type.BOOLEAN && valueType != BooleanValue.class)
			return false;
		if (fieldType == Field.Type.INTEGER && valueType != IntegerValue.class)
			return false;
		if (fieldType == Field.Type.REFERENCE && valueType != ReferenceValue.class)
			return false;
		if (fieldType == Field.Type.TEXT && valueType != StringValue.class)
			return false;
		if (fieldType == Field.Type.TEXT && valueType == StringValue.class) {
			if (((StringValue)value).value().contains("\\n"))
				return false;
		}
		if (fieldType == Field.Type.TEXTAREA && valueType != StringValue.class)
			return false;

		return true;
	}

	/**
	 * Method called by set method if the element added in is a refereneValue, it will check
	 * whether the ref table exist and the ref row exist
	 * @param index: position in the row element goes in
	 * @param element to be added in
	 */
	private void checkRefSet(int index, Value e) {
		ReferenceValue refE = (ReferenceValue)e;
		String tableName = refE.table();
		Value[] keyValue = refE.keys();

		if (keyValue == null || keyValue.length == 0)
			throw new InvalidOperation("Element does not contain key Value, therefore is invalid");

		Table refTable = this.parent.parent.table(tableName);

		if (refTable == null)
			throw new InvalidOperation("Table " + tableName + " referred to does not exist");
		boolean matchK = true;

		for (Iterator itr = refTable.rows().iterator(); itr.hasNext();) {
			List<Value> rowTocheck = (List<Value>) itr.next();
			matchK = matchKeys(rowTocheck, keyValue);
			if (matchK)
				break;
		}

		if (!matchK)
			throw new InvalidOperation("Keyvalue referred to does not match");
	}

	/**
	 * private method called by checkRefSet method, will check wither the ReferenceValue contains
	 * the accurate key Value
	 * @param refTo: table referred to
	 * @param row contained in the table need to checked
	 * @param key value contained in new referenceValue
	 * @return true if matches the keys
	 */
	private boolean matchKeys(List<Value> row, Value... keys){
		boolean match = true;
		for (Value v: keys) {
			if (!row.contains(v))
				match = false;
		}
		return match;
	}

	private class Iter<MyRow> implements Iterator<MyRow> {

		int cursor; // index of next element to return
		int lastRet = -1; // index of last element returned; -1 if no such

		@Override
		public boolean hasNext() {
			return cursor != size;
		}

		@Override
		public MyRow next() {
			int i = cursor;
			if (i >= size)
				throw new NoSuchElementException();
			if (i >= data.length)
				throw new ConcurrentModificationException();
			cursor = i + 1;
			return (MyRow) data[lastRet = i];
		}

		@Override
		public void remove() {
			if (lastRet < 0)
				throw new IllegalStateException();

			try {
				MyRows.this.remove(lastRet);
				cursor = lastRet;
				lastRet = -1;
			} catch (IndexOutOfBoundsException e) {
				throw new ConcurrentModificationException();
			}
		}

	}

}
