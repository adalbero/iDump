package io.adalbero.tool.idump.command;

import org.apache.commons.lang.StringUtils;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;

import io.adalbero.tool.idump.AppCommand;
import io.adalbero.tool.idump.AppCommandFactory;
import io.adalbero.tool.idump.AppCommandLine;
import io.adalbero.tool.idump.AppConsole;
import io.adalbero.tool.idump.AppContext;
import io.adalbero.tool.idump.util.AppDctmUtil;
import io.adalbero.tool.idump.util.AppStringUtil;

public class ShellCommand extends AppCommand {

	public static final String NAME = "SHELL";

	private String credential;

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void printHelp() {
		AppConsole.printf("  SHELL <repo|ASK> <user> <pass|ASK> [<command>]");
	}

	@Override
	public Object execute() throws DfException {
		IDfSession dmSession = connect(cmdLine);

		try {
			if (dmSession == null) {
				return null;
			}

			this.context = new AppContext();
			this.context.dmSession = dmSession;
			this.credential = getCredential();

			String line = cmdLine.getArgumentTilEnd(4);
			line = AppStringUtil.unquote(line);

			if (line != null) {
				executeCommand(line);
			} else {
				AppConsole.printf();
				AppConsole.printf("iDump SHELL - try: HELP for help and EXIT to exit.");
				loop();
			}
		} finally {
			disconnect(dmSession);
		}

		return "Bye";
	}

	private String getCredential() throws DfException {
		return AppDctmUtil.getCredential(this.context.dmSession);
	}

	private Object loop() throws DfException {
		Object result = null;

		while (true) {
			String line = prompt();
			if (StringUtils.isNotEmpty(line)) {
				if (AppStringUtil.equals(line, "QUIT|EXIT|BYE")) {
					break;
				}

				executeCommand(line);
			}
		}

		return result;
	}

	private String prompt() {
		AppConsole.printf();
		String msg = String.format("[%s] %s ", credential, this.context.pwd);
		String line = AppConsole.prompt(msg);

		return line;
	}

	private Object executeCommand(String line) throws DfException {
		Object result = null;

		AppCommandLine cmdLine = new AppCommandLine(line);
		AppCommand cmd = AppCommandFactory.getCommand(this.context, cmdLine);

		AppConsole.printf();
		AppConsole.printf("cmd [%s]> %s", cmd.getName(), cmdLine.getCommandLine());

		try {
			result = cmd.execute();

			AppConsole.printf();
			AppConsole.printResult(result);
		} catch (Exception ex) {
			AppConsole.printf();
			AppConsole.printError(ex);
		}

		return result;

	}

	private IDfSession connect(AppCommandLine cmdLine) throws DfException {
		AppConsole.printTitle("Connect");

		String repo = cmdLine.getArgument(1);
		if (repo == null) {
			return null;
		} else if (AppStringUtil.equals(repo, "ASK")) {
			String prompt = String.format("Repository?");
			repo = AppConsole.prompt(prompt);
		}

		String user = cmdLine.getArgument(2);
		if (user == null) {
			String prompt = String.format("[@%s] User?", repo);
			user = AppConsole.prompt(prompt);
		}
		

		String pass = cmdLine.getArgument(3);
		pass = AppStringUtil.equals(pass, "ASK") ? null : pass;

		if (pass == null) {
			String prompt = String.format("[%s@%s] Password?", user, repo);
			pass = AppConsole.promptPassword(prompt);
		}

		AppConsole.printf("Connecting...");
		IDfSession dmSession = AppDctmUtil.connect(repo, user, pass);
		AppConsole.printf("Connected to %s", AppDctmUtil.getCredential(dmSession));

		return dmSession;
	}

	private void disconnect(IDfSession dmSession) {
		if (dmSession != null) {
			AppDctmUtil.disconnect(dmSession);

			AppConsole.printf();
			AppConsole.printf("Disconnected");
		}
	}

}
