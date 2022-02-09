package io.adalbero.tool.idump;

import java.util.ArrayList;
import java.util.List;

public class AppTable {
	private String[] header = null;
	private String[] row = null;
	private List<String[]> rows = new ArrayList<>();
	private int[] width;
	private int maxWidth = 40;
	private int size = 0;

	public AppTable(int size) {
		this.size = size;
		this.width = new int[size];
	}

	public int getSize() {
		return this.size;
	}
	
	public void setMaxWidth(int w) {
		this.maxWidth = w;
	}

	public void setHeader(int idx, String value) {
		if (header == null) {
			header = new String[size];
		}

		header[idx] = value;
		calcWidth(idx, value);
	}

	public String getHeader(int idx) {
		String value = header[idx];
		return value;
	}

	public int count() {
		return rows.size();
	}

	public void addRow() {
		row = new String[size];
		rows.add(row);
	}

	public void setRow(int idx, String value) {
		row[idx] = value;
		calcWidth(idx, value);
	}

	public String getRow(int line, int idx) {
		row = rows.get(line);
		String value = row[idx];
		return value;
	}

	public void calcWidth(int idx, String value) {
		int w = Math.max(width[idx], value.length());
		
		if (maxWidth > 0) {
			w = Math.min(maxWidth, w);
		}
		
		setWidth(idx, w);
	}

	public void setWidth(int idx, int w) {
		width[idx] = w;
	}

	public int getWidth(int idx) {
		return width[idx];
	}
}
