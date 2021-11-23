package io.adalbero.tool.idump.command;

import com.documentum.fc.common.DfException;

import io.adalbero.tool.idump.AppCommand;
import io.adalbero.tool.idump.AppConsole;
import io.adalbero.tool.idump.util.AppDctmUtil;

public class ListCommand extends AppCommand {

	public static final String NAME = "LIST";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void printHelp() {
		AppConsole.printf("  LIST <type> WHERE <condition>");
	}

	@Override
	public Object execute() throws DfException {
		Object result = null;

		String qualification = cmdLine.getArgumentTilEnd(1);
		String attributes = AppDctmUtil.getDefaultAttrs(cmdLine.getArgument(1));
		String dql = String.format("SELECT %s FROM %s", attributes, qualification);

		result = query(dql);

		return result;
	}

}
