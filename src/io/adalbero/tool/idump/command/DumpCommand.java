package io.adalbero.tool.idump.command;

import com.documentum.fc.common.DfException;

import io.adalbero.tool.idump.AppCommand;
import io.adalbero.tool.idump.AppConsole;
import io.adalbero.tool.idump.util.AppDctmUtil;
import io.adalbero.tool.idump.util.AppStringUtil;

public class DumpCommand extends AppCommand {
	public static final String NAME = "DUMP";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void printHelp() {
		AppConsole.printf("  DUMP dfc|docbroker|docbase|server|serverMap|session|connection");
		AppConsole.printf("  DUMP USER <user_name|user_login_name>");
		AppConsole.printf("  DUMP GROUP <group_name>");
		AppConsole.printf("  DUMP TYPE <group_name>");
		AppConsole.printf("  DUMP <object_id>");
		AppConsole.printf("  DUMP /<cabinet>/<folder>/../<object_name>");
		AppConsole.printf("  DUMP <type> WHERE <condition>");
	}

	@Override
	public Object execute() throws DfException {
		Object result = null;

		String line = cmdLine.getCommandLine();
		String qualification = AppStringUtil.replace(line, "^DUMP ", "");

		result = AppDctmUtil.getObject(context.dmSession, qualification);
		
		if (result == null) {
			result = "[no Object found]";
		}
		
		return result;
	}
}
