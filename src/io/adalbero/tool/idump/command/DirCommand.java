package io.adalbero.tool.idump.command;

import com.documentum.fc.common.DfException;

import io.adalbero.tool.idump.AppCommand;
import io.adalbero.tool.idump.AppConsole;
import io.adalbero.tool.idump.util.AppDctmUtil;
import io.adalbero.tool.idump.util.AppStringUtil;

public class DirCommand extends AppCommand {

	public static final String NAME = "DIR";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void printHelp() {
		AppConsole.printf("  DIR [<path>]");
	}

	@Override
	public Object execute() throws DfException {
		Object result = null;

		String type = "dm_document";
		String attributes = AppDctmUtil.getDefaultAttrs(type);
		String path = cmdLine.getArgumentTilEnd(1);

		String fullPath = AppStringUtil.getFullPath(this.context.pwd, path);
		AppDctmUtil.validatePath(this.context.dmSession, fullPath);

		String dqlFolders = String.format(
				" SELECT r_object_id, object_name, r_object_type FROM dm_folder WHERE FOLDER('%s') ORDER BY object_name",
				fullPath);
		String dqlFiles = String.format(" SELECT %s FROM %s WHERE FOLDER('%s') ORDER BY object_name", attributes, type,
				fullPath);

		result = new Object[] { "DIR " + fullPath, query(dqlFolders), query(dqlFiles) };

		return result;
	}
}
