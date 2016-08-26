package swen221.lab4.lang;

import java.util.ArrayList;

public class DatabaseImpl implements Database{
	private ArrayList<Object[]> rows = new ArrayList<Object[]>();
	private int key;
	private ColumnType[] schema;
	public DatabaseImpl(ArrayList<Object[]> rows, int key, ColumnType[] schema){
		this.rows = rows;
		this.key = key;
		this.schema = schema;
	}

	@Override
	public ColumnType[] getSchema() {
		return this.schema;
	}

	@Override
	public int getKeyField() {         //done
		return this.key;
	}

	@Override
	public int size() {        //done
		return rows.size();
	}

	@Override
	public void addRow(Object... data) throws InvalidRowException, DuplicateKeyException {

		rows.add(data);
		for(int i=rows.size()-1; i>=0; i--){
			 if(rows.get(rows.size()-1).length!=2){
				rows.remove(rows.size()-1);
				throw new InvalidRowException();
			}
			else if(rows.get(rows.size()-1)[0].equals(rows.get(rows.size()-1)[1])){
				
				rows.remove(rows.size()-1);
				throw new InvalidRowException();
			}
		
			else if(rows.size()-1 != i && rows.get(rows.size()-1)[0].equals(rows.get(i)[0])){
				rows.remove(rows.size()-1);
				throw new DuplicateKeyException();
			}
			
		}
		
}

	@Override
	public Object[] getRow(Object key) throws InvalidKeyException {
		for(int i=0; i< rows.size(); i++){
			if(rows.get(i)[0].equals(key)){
				return rows.get(i);
			}
			
			if(rows.get(i)[1].equals(key) && this.key ==1){
				return rows.get(i);
				

			}
		}
		throw new InvalidKeyException();
	}

	@Override
	public Object[] getRow(int index) throws IndexOutOfBoundsException {
	return rows.get(index);
	}

}
