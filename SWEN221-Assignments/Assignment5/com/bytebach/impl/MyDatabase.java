package com.bytebach.impl;

import java.util.*;

import com.bytebach.model.*;

public class MyDatabase implements Database {
	private Map<String, Table> tables = new HashMap<>();

	@Override
	public Collection<? extends Table> tables() {
		return tables.values();
	}

	@Override
	public Table table(String name) {
		return tables.get(name);
	}

	@Override
	public void createTable(String name, List<Field> fields) {
		if (tables.containsKey(name))
			throw new InvalidOperation("Table exist");
		Table table = new MyTable(name, fields, this);
		tables.put(name, table);
	}

	@Override
	public void deleteTable(String name) {
		if (!tables.containsKey(name) || name == null)
			throw new InvalidOperation("No table");
		tables.remove(name);
		// when delete an table
		// all the references to the table should all be deleted
		deleteRef(name);

	}

	@SuppressWarnings("unused")
	private void deleteRef(String name) {
		// going through the reference table if found the reference delete it
		for (Map.Entry<String, Table> entrySet : tables.entrySet()) {
			Table table = entrySet.getValue();
			List<Field> schema = table.fields();
			int index = 0;
			for (Field f : schema) {
				if (Field.Type() == Field.Type.REFERENCE) {
					deleteRow(table, index, name);
				}
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void deleteRow(Table table, int index, String name) {
		// delete the row that was found using the remove() method in the
		// implemented iterator.
		List<List<Value>> rows = table.rows();
		for (Iterator itr = rows.iterator(); itr.hasNext();) {
			List<Value> row = (List<Value>) itr.next();
			ReferenceValue ref = (ReferenceValue) row.get(index);
			if (ref.table().equals(name)) {
				itr.remove();
			}
		}
	}

	// This is where you'll probably want to start. You'll need to provide an
	// implementation of Table as well.
	//
	// One of the key challenges in this assignment is to provide you're
	// own implementations of the List interface which can intercept the various
	// operations (e.g. add, set, remove, etc) and check whether they violate
	// the constraints and/or update the database appropriately (e.g. for the
	// cascading delete).
	//
	// HINT: to get started, don't bother providing your own implementations of
	// List as discussed above! Instead, implement MyDatabase and MyTabe using
	// conventional Collections. When you have that working, and the web system
	// is doing something sensible, then consider how you're going to get those
	// unit test to past.
}
