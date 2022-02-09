package io.adalbero.tool.idump.command;

import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.common.DfException;

import io.adalbero.tool.idump.AppCommand;
import io.adalbero.tool.idump.AppConsole;
import io.adalbero.tool.idump.util.AppDctmUtil;

public class CatCommand extends AppCommand {

	public static final String NAME = "CAT";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void printHelp() {
		AppConsole.printf("  CAT <object_id>");
		AppConsole.printf("  CAT /<cabinet>/<folder>/../<object_name>");
		AppConsole.printf("  CAT <type> WHERE <condition>");
	}

	@Override
	public Object execute() throws DfException {
		Object result = null;

		String qualification = cmdLine.getArgumentTilEnd(1);

		IDfTypedObject dmObj = AppDctmUtil.getObject(context.dmSession, qualification);

		if (dmObj == null) {
			throw new DfException("[Object not found] " + qualification);
		} else {
			result = AppDctmUtil.getContentAsString((IDfSysObject) dmObj);
		}

		return result;
	}

}
