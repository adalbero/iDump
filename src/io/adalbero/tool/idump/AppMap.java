package io.adalbero.tool.idump;

public class AppMap extends AppTable {

	public AppMap() {
		super(2);
		
		this.setHeader(0, "key");
		this.setHeader(1, "value");
	}
	
	public void add(String key, String value) {
		this.addRow();
		this.setRow(0, key);
		this.setRow(1, value);
	}

}
