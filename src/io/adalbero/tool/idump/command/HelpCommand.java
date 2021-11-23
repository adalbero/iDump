package io.adalbero.tool.idump.command;

import com.documentum.fc.common.DfException;

import io.adalbero.tool.idump.AppCommand;
import io.adalbero.tool.idump.AppCommandFactory;
import io.adalbero.tool.idump.AppCommandLine;
import io.adalbero.tool.idump.AppConsole;

public class HelpCommand extends AppCommand {

	public static final String NAME = "HELP";

	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	public void printHelp() {
		AppConsole.printf("  HELP [<cmd>]");
	}

	@Override
	public Object execute() throws DfException {
		String line = cmdLine.getArgumentTilEnd(1);

		if (line != null) {
			AppConsole.printf("Command:");
			AppCommandLine helpCmdLine = new AppCommandLine(line);
			AppCommand cmd = AppCommandFactory.getCommand(this.context, helpCmdLine);
			cmd.printHelp();
		} else {
			AppCommandFactory.printCommandsHelp();
		}

		return null;
	}

}
