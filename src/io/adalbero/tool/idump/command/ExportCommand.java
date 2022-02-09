package io.adalbero.tool.idump.command;

import java.io.File;

import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;

import io.adalbero.tool.idump.AppCommand;
import io.adalbero.tool.idump.AppConsole;
import io.adalbero.tool.idump.util.AppDctmUtil;

public class ExportCommand extends AppCommand {

	public static final String NAME = "EXPORT";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void printHelp() {
		AppConsole.printf("  EXPORT <object_id>");
		AppConsole.printf("  EXPORT /<cabinet>/<folder>/../<object_name>");
		AppConsole.printf("  EXPORT <type> WHERE <condition>");
	}

	@Override
	public Object execute() throws DfException {
		Object result = null;

		String qualification = cmdLine.getArgumentTilEnd(1);

		IDfSysObject dmDoc = (IDfSysObject) AppDctmUtil.getObject(context.dmSession, qualification);

		if (dmDoc == null) {
			throw new DfException("[Object not found] " + qualification);
		} else {
			File f = AppDctmUtil.exportFile(dmDoc, ".");
			result = String.format("Exported: %s", f.getAbsolutePath());
		}

		return result;
	}

}
