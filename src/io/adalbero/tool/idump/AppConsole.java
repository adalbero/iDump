package io.adalbero.tool.idump;

import org.apache.log4j.Logger;

import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.common.DfException;

import io.adalbero.tool.idump.util.AppStringUtil;

public class AppConsole {
	private static Logger out = Logger.getLogger("out");
	private static Logger log = Logger.getLogger(AppMain.class);

	private static String buffer = "";

	public static void printf() {
		printf("");
	}

	public static void printf(String msg, Object... args) {
		out.info(String.format(msg, args));
	}

	public static void appendf(String msg, Object... args) {
		buffer += String.format(msg, args);
	}

	public static void flush() {
		printf(buffer);
		buffer = "";
	}

	public static String promptPassword(String msg) {
		return new String(System.console().readPassword("%s> ", msg));
	}

	public static String prompt(String msg) {
		return System.console().readLine("%s> ", msg);
	}

	public static void printTitle(String title) {
		printf();
		printf("*** %s ***", title);
	}

	public static void printError(Throwable ex) {
		printf("ERROR: [%s] %s", ex.getClass().getSimpleName(), ex.getMessage());
		log.error("ERROR: " + ex.getMessage(), ex);
	}

	public static void printResult(IDfTypedObject dmObj) throws DfException {
		printf(dmObj.dump());
	}

	public static void printResult(String str) throws DfException {
		if (!AppStringUtil.isEmpty(str)) {
			printf(str);
		}
	}

	public static void printResult(AppTable table) throws DfException {
		int n = table.getSize();

		// print header
		for (int i = 0; i < n; i++) {
			int len = table.getWidth(i);
			String value = table.getHeader(i);
			AppConsole.appendf("%s  ", AppStringUtil.alignLeft(value, len));
		}
		AppConsole.flush();

		// print sep
		for (int i = 0; i < n; i++) {
			int len = table.getWidth(i);
			String value = AppStringUtil.fill("=", len);
			AppConsole.appendf("%s  ", AppStringUtil.alignLeft(value, len));
		}
		AppConsole.flush();

		// print rows
		int count = table.count();
		for (int row = 0; row < count; row++) {
			for (int i = 0; i < n; i++) {
				int len = table.getWidth(i);
				String value = table.getRow(row, i);
				AppConsole.appendf("%s  ", AppStringUtil.alignLeft(value, len));
			}
			AppConsole.flush();
		}
	}

	public static void printResult(Object obj) throws DfException {
		if (obj == null) {
			// noop
		} else if (obj instanceof String) {
			printResult((String) obj);
		} else if (obj instanceof IDfTypedObject) {
			IDfTypedObject dmObj = (IDfTypedObject) obj;
			printResult(dmObj);
		} else if (obj instanceof AppTable) {
			AppTable table = (AppTable) obj;
			printResult(table);
		} else {
			printResult(obj.toString());
		}
	}

	public static void printAttr(IDfTypedObject dmObj, String attrName) throws DfException {
		String value = dmObj.getString(attrName);
		printf("%s: %s", attrName, value);
	}

}
