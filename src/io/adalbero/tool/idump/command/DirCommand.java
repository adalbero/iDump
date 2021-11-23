package io.adalbero.tool.idump.command;

import com.documentum.fc.common.DfException;

import io.adalbero.tool.idump.AppCommand;
import io.adalbero.tool.idump.AppConsole;

public class DirCommand extends AppCommand {

	public static final String NAME = "DIR";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void printHelp() {
		AppConsole.printf("  DIR <full_path>");
	}

	@Override
	public Object execute() throws DfException {
		Object result = null;

		String type = "dm_document";
//		String attributes = AppDctmUtil.getDefaultAttrs(type);
		String folder = cmdLine.getArgumentTilEnd(1);

		String dql = "";
		dql += String.format(
				" SELECT 0 as x, r_object_id, object_name, r_object_type FROM dm_folder WHERE FOLDER('%s')", folder);
		dql += String.format(" UNION");
		dql += String.format(" SELECT 1 as x, r_object_id, object_name, title FROM dm_document WHERE FOLDER('%s')",
				folder);
		dql += String.format(" ORDER BY 1, 3");

		result = query(dql);

		return result;
	}
}
