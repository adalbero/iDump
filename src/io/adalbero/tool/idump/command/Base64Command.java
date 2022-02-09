package io.adalbero.tool.idump.command;

import com.documentum.fc.common.DfException;

import io.adalbero.tool.idump.AppCommand;
import io.adalbero.tool.idump.AppConsole;
import io.adalbero.tool.idump.AppMap;
import io.adalbero.tool.idump.util.AppStringUtil;

public class Base64Command extends AppCommand {
	public static final String NAME = "BASE64";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void printHelp() {
		AppConsole.printf("  BASE64 <encoded|decoded>");
	}

	@Override
	public Object execute() throws DfException {
		String msg = cmdLine.getArgumentTilEnd(1);
		String result[] = AppStringUtil.base64(msg);

		AppMap map = new AppMap();
		map.setMaxWidth(0);

		map.add("decoded", result[0]);
		map.add("encoded", result[1]);

		return map;
	}
}
