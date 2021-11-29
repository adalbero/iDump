package io.adalbero.tool.idump.command;

import com.documentum.fc.common.DfException;

import io.adalbero.tool.idump.AppCommand;
import io.adalbero.tool.idump.AppConsole;

public class SelectCommand extends AppCommand {

	public static final String NAME = "SELECT";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void printHelp() {
		AppConsole.printf("  SELECT <attr_list> FROM <type> WHERE <condition>");
	}

	@Override
	public Object execute() throws DfException {
		Object result = null;

		String dql = cmdLine.getCommandLine();

		result = query(dql);

		return result;
	}
}
