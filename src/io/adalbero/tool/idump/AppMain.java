package io.adalbero.tool.idump;

import com.documentum.fc.client.DfClient;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.common.DfException;

import io.adalbero.tool.idump.command.Base64Command;
import io.adalbero.tool.idump.command.CatCommand;
import io.adalbero.tool.idump.command.CdCommand;
import io.adalbero.tool.idump.command.DecodeCommand;
import io.adalbero.tool.idump.command.DirCommand;
import io.adalbero.tool.idump.command.DumpCommand;
import io.adalbero.tool.idump.command.ExportCommand;
import io.adalbero.tool.idump.command.HelpCommand;
import io.adalbero.tool.idump.command.ListCommand;
import io.adalbero.tool.idump.command.SelectCommand;
import io.adalbero.tool.idump.command.ShellCommand;
import io.adalbero.tool.idump.util.AppStringUtil;

public class AppMain {

	public static void main(String[] args) {
		new AppMain().run(args);
//		new AppMain().test();
	}

//	public static void test() {
//		String result = "";
//		
//		result = AppDctmUtil.registryPassword("password");
//		System.out.println(result);
//
//		result = AppDctmUtil.registryPassword("AAAAEKFifJGYNg+PlAgNcDUB1CvAZ9DHHYOhXEWDdLd6jOUI");
//		System.out.println(result);
//	}
//
	private void printHeader() {
		// https://patorjk.com/software/taag/#p=display&f=Small&t=iDump
		AppConsole.printf();
		AppConsole.printf("-----------------------------------------");
		AppConsole.printf("      _ ____");
		AppConsole.printf("     (_) __ \\__  ______ ___  ____");
		AppConsole.printf("    / / / / / / / / __ `__ \\/ __ \\");
		AppConsole.printf("   / / /_/ / /_/ / / / / / / /_/ /");
		AppConsole.printf("  /_/_____/\\__,_/_/ /_/ /_/ .___/");
		AppConsole.printf("                         /_/");
		AppConsole.printf("  Version 0.0.2                          ");
		AppConsole.printf("  by Adalbero F. Guimaraes (2022)        ");
		AppConsole.printf("-----------------------------------------");
		AppConsole.printf("  %s", AppStringUtil.getTimestamp());
	}

	public void run(String[] args) {
		try {
			initCommands();

			printHeader();

			printClientInfo();

			if (args.length > 0) {
				AppCommandLine cmdLine = new AppCommandLine("SHELL", args);
				AppCommand shell = AppCommandFactory.getCommand(null, cmdLine);
				Object result = shell.execute();

				AppConsole.printf();
				AppConsole.printResult(result);
			} else {
				printHelp();
			}

		} catch (Exception ex) {
			AppConsole.printf();
			AppConsole.printError(ex);
		}

		AppConsole.printf();

	}

	private void initCommands() {
		AppCommandFactory.addCommand(DumpCommand.NAME, DumpCommand.class);
		AppCommandFactory.addCommand(SelectCommand.NAME, SelectCommand.class);
		AppCommandFactory.addCommand(ListCommand.NAME, ListCommand.class);
		AppCommandFactory.addCommand(CdCommand.NAME, CdCommand.class);
		AppCommandFactory.addCommand(DirCommand.NAME, DirCommand.class);
		AppCommandFactory.addCommand(CatCommand.NAME, CatCommand.class);
		AppCommandFactory.addCommand(ExportCommand.NAME, ExportCommand.class);
		AppCommandFactory.addCommand(Base64Command.NAME, Base64Command.class);
		AppCommandFactory.addCommand(DecodeCommand.NAME, DecodeCommand.class);
		AppCommandFactory.addCommand(ShellCommand.NAME, ShellCommand.class);
		AppCommandFactory.addCommand(HelpCommand.NAME, HelpCommand.class);
	}

	private void printHelp() {
		AppConsole.printf();
		AppConsole.printf("Usage: iDump <repo> <user> <pass>|ASK <command>");
		AppCommandFactory.printCommandsHelp();
	}

	public void printClientInfo() throws DfException {
		AppConsole.printTitle("DFC Config");

		IDfClient dmClient = DfClient.getLocalClient();

		IDfTypedObject dmConfig = dmClient.getClientConfig();

		AppConsole.printAttr(dmConfig, "dfc.config.file");
		AppConsole.printAttr(dmConfig, "dfc.name");
		AppConsole.printAttr(dmConfig, "dfc.version");

		AppConsole.printTitle("Docbroker");
		int n = dmConfig.getValueCount("dfc.docbroker.host");
		for (int i = 0; i < n; i++) {
			String host = dmConfig.getRepeatingString("dfc.docbroker.host", i);
			String port = dmConfig.getRepeatingString("dfc.docbroker.port", i);
			AppConsole.printf("dfc.docbroker.host:port[%d]: %s:%s", i, host, port);
		}

		AppConsole.printTitle("Docbase");
		IDfTypedObject dmDocbaseMap = dmClient.getDocbaseMap();
		n = dmDocbaseMap.getValueCount("r_docbase_name");
		for (int i = 0; i < n; i++) {
			String name = dmDocbaseMap.getRepeatingString("r_docbase_name", i);
			String description = dmDocbaseMap.getRepeatingString("r_docbase_description", i);
			String host = dmDocbaseMap.getRepeatingString("i_host_addr", i);

			AppConsole.printf("docbase[%d]: %s (%s) @ %s", i, name, description, host);
		}
	}

}
