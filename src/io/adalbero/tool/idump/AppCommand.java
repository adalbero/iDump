package io.adalbero.tool.idump;

import com.documentum.fc.common.DfException;

import io.adalbero.tool.idump.util.AppDctmUtil;

public abstract class AppCommand {
	protected AppContext context;
	protected AppCommandLine cmdLine;

	public abstract void printHelp();

	public abstract String getName();

	public abstract Object execute() throws DfException;

	public void setContext(AppContext context) {
		this.context = context;
	}

	public void setCommandLine(AppCommandLine cmdLine) {
		this.cmdLine = cmdLine;
	}

	public AppTable query(String dql) throws DfException {
		AppConsole.printf("dql> %s", dql);
		return AppDctmUtil.queryTable(this.context.dmSession, dql);
	}
}
