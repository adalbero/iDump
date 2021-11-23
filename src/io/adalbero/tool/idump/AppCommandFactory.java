package io.adalbero.tool.idump;

import java.util.LinkedHashMap;
import java.util.Map;

import io.adalbero.tool.idump.command.DumpCommand;

public class AppCommandFactory {
	private static Map<String, Class<? extends AppCommand>> commands = new LinkedHashMap<>();

	public static void addCommand(String cmdName, Class<? extends AppCommand> cmd) {
		commands.put(cmdName, cmd);
	}

	public static AppCommand getCommand(AppContext context, AppCommandLine cmdLine) {
		String cmdName = cmdLine.getArgument(0).toUpperCase();
		
		Class<? extends AppCommand> cmdClass = commands.get(cmdName);
		if (cmdClass == null) {
			cmdClass = DumpCommand.class;
		}

		AppCommand cmd = newInstance(cmdClass);
		cmd.setContext(context);
		cmd.setCommandLine(cmdLine);
		return cmd;
	}

	public static void printCommandsHelp() {
		AppConsole.printf("Commands:");
		for (Class<? extends AppCommand> cmdClass : commands.values()) {
			AppCommand cmd = newInstance(cmdClass);
			cmd.printHelp();
		}
	}

	private static AppCommand newInstance(Class<? extends AppCommand> cmdClass) {
		try {
			return cmdClass.getDeclaredConstructor().newInstance();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}
