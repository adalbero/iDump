package io.adalbero.tool.idump.command;

import com.documentum.fc.common.DfException;

import io.adalbero.tool.idump.AppCommand;
import io.adalbero.tool.idump.AppConsole;
import io.adalbero.tool.idump.util.AppDctmUtil;
import io.adalbero.tool.idump.util.AppStringUtil;

public class CdCommand extends AppCommand {

	public static final String NAME = "CD";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void printHelp() {
		AppConsole.printf("  CD [<path>]");
	}

	@Override
	public Object execute() throws DfException {
		Object result = null;

		String path = cmdLine.getArgumentTilEnd(1);
		String fullPath = AppStringUtil.getFullPath(this.context.pwd, path);

		AppDctmUtil.validatePath(this.context.dmSession, fullPath);

		this.context.pwd = fullPath;

		result = "CD " + fullPath;

		return result;
	}

}
