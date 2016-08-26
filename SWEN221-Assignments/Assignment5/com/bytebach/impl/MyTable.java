package com.bytebach.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bytebach.model.Database;
import com.bytebach.model.Field;
import com.bytebach.model.InvalidOperation;
import com.bytebach.model.ReferenceValue;
import com.bytebach.model.Table;
import com.bytebach.model.Value;

public class MyTable implements Table {

	private String name;
	private List<Field> fields;
	private MyRows rows;
	private List<Field> keys;
	private List<Integer> keyValueIndexes; // stores all the indexes of key
											// fields in a schema
	public final Database parent;

	public MyTable(String name, List<Field> fields, Database parent) {
		this.parent = parent;
		this.name = name;
		this.fields = fields;
		this.keys = new ArrayList<Field>();
		this.keyValueIndexes = new ArrayList<Integer>();
		this.rows = new MyRows(this);
		for (int i = 0; i < fields.size(); i++) {
			if (fields.get(i).isKey()) {
				keys.add(fields.get(i));
				keyValueIndexes.add(i);
			}
		}
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public List<Field> fields() {
		return fields;
	}

	@Override
	public List<List<Value>> rows() {
		return rows;
	}

	@Override
	public List<Value> row(Value... keys) {
		for (int i = 0; i < rows.size(); i++) {
			List<Value> tableRow = rows.get(i);
			boolean match = matchKeys(tableRow, keys);
			if (match)
				return tableRow;
		}

		throw new InvalidOperation("No matching rows");
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
	private boolean matchKeys(List<Value> row, Value... keys) {
		boolean match = true;
		for (Value value : keys) {
			if (!row.contains(value))
				match = false;
		}
		return match;
	}

	/**
	 * return the list of keys in the schema
	 * 
	 * @return
	 */
	public List<Field> getKeys() {
		return keys;
	}

	/**
	 * return a list that stores all the indexes of key fields in a schema
	 * 
	 * @return
	 */
	public List<Integer> getKeyValueIndexes() {
		return keyValueIndexes;
	}

	@Override
	public void delete(Value... keys) {
		boolean foundMatch = false;
		for (Iterator itr = rows.iterator(); itr.hasNext();) {
			List<Value> tableRow = (List<Value>) itr.next();
			if (matchKeys(tableRow, keys)) {
				itr.remove();
				foundMatch = true;
				deleteRefRow(keys);
			}
		}
		if (!foundMatch)
			throw new InvalidOperation("No row matching given keys has been found");
	}

	/**
	 * to delete all the reference rows when deleting a row
	 * 
	 * @param keys
	 */
	private void deleteRefRow(Value[] keys) {
		for (Table otherTable : this.parent.tables()) {
			if (otherTable != this) {
				List<Field> fields = otherTable.fields();
				int refIndex = 0;
				for (Field f : fields) {
					if (f.type() == Field.Type.REFERENCE) {
						match(keys, refIndex, otherTable);
					}
					refIndex++;
				}
			}
		}
	}

	private void match(Value[] keys, int index, Table toCheck) {
		for (Iterator itr = toCheck.rows().iterator(); itr.hasNext();) {
			List<Value> rowToCheck = (List<Value>) itr.next();
			if (((ReferenceValue) rowToCheck.get(index)).table().equals(this.name())
					&& (((ReferenceValue) rowToCheck.get(index)).keys())[0].equals(keys[0])) {
				itr.remove();
			}
		}
	}

}