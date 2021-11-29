package io.adalbero.tool.idump.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import com.documentum.com.DfClientX;
import com.documentum.fc.client.DfClient;
import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfFolder;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.client.IDfUser;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLoginInfo;
import com.documentum.fc.common.IDfList;
import com.documentum.operations.IDfExportNode;
import com.documentum.operations.IDfExportOperation;
import com.documentum.operations.IDfOperationError;

import io.adalbero.tool.idump.AppTable;

public class AppDctmUtil {
	public static String getCredential(IDfSession dmSession) throws DfException {
		String repo = dmSession.getDocbaseName();
		String user = dmSession.getLoginInfo().getUser();

		return String.format("%s@%s", user, repo);
	}

	public static IDfSession connect(String repo, String user, String pass) throws DfException {
		IDfClient dmClient = DfClient.getLocalClient();
		IDfSession dmSession = dmClient.newSession(repo, new DfLoginInfo(user, pass));

		return dmSession;
	}

	public static void disconnect(IDfSession dmSession) {
		try {
			dmSession.disconnect();
		} catch (DfException ex) {
		}
	}

	public static IDfTypedObject getObject(IDfSession dmSession, String qualification) throws DfException {
		if (qualification == null) {
			throw new DfException("No object specified.");
		} else if (AppStringUtil.equals(qualification, "session")) {
			return dmSession.getSessionConfig();
		} else if (AppStringUtil.equals(qualification, "dfc")) {
			return dmSession.getClientConfig();
		} else if (AppStringUtil.equals(qualification, "connection")) {
			return dmSession.getConnectionConfig();
		} else if (AppStringUtil.equals(qualification, "docbase")) {
			return dmSession.getDocbaseConfig();
		} else if (AppStringUtil.equals(qualification, "docbroker")) {
			return dmSession.getDocbrokerMap();
		} else if (AppStringUtil.equals(qualification, "server")) {
			return dmSession.getServerConfig();
		} else if (AppStringUtil.equals(qualification, "serverMap")) {
			return dmSession.getServerMap(dmSession.getDocbaseName());
		} else if (AppStringUtil.startsWith(qualification, "USER ")) {
			String value = qualification.substring("USER ".length());
			IDfUser dmUser = dmSession.getUser(value);
			if (dmUser == null) {
				dmUser = dmSession.getUserByLoginName(value, null);
			}
			return dmUser;
		} else if (AppStringUtil.startsWith(qualification, "GROUP ")) {
			String value = qualification.substring("GROUP ".length());
			return dmSession.getGroup(value);
		} else if (AppStringUtil.startsWith(qualification, "TYPE ")) {
			String value = qualification.substring("TYPE ".length());
			return dmSession.getType(value);
		} else if (AppStringUtil.match(qualification, "[0-9a-fA-F]{16}")) {
			return dmSession.getObject(new DfId(qualification));
		} else if (AppStringUtil.startsWith(qualification, "/")) {
			return dmSession.getObjectByPath(qualification);
		} else {
			qualification = qualification.substring("FROM ".length());
			return dmSession.getObjectByQualification(qualification);
		}

	}

	public static AppTable queryTable(IDfSession dmSession, String dql) throws DfException {
		AppTable table = null;

		IDfCollection coll = null;
		try {
			IDfQuery query = new DfQuery(dql);
			coll = query.execute(dmSession, DfQuery.DF_READ_QUERY);
			int n = coll.getAttrCount();

			while (coll.next()) {
				if (table == null) {
					table = new AppTable(n);
					for (int i = 0; i < n; i++) {
						String name = coll.getAttr(i).getName();
						table.setHeader(i, name);
					}
				}

				table.addRow();
				for (int i = 0; i < n; i++) {
					String name = coll.getAttr(i).getName();
					String value = coll.getString(name);
					table.setRow(i, value);
				}
			}

		} finally {
			if (coll != null) {
				coll.close();
			}
		}

		return table;
	}

	public static String getDefaultAttrs(String type) {

		if (AppStringUtil.equals(type, "dm_user|esop_user")) {
			return "r_object_id, user_name, user_login_name, user_source";
		} else if (AppStringUtil.equals(type, "dm_group")) {
			return "r_object_id, group_name, i_all_users_names";
		} else {
			return "r_object_id, object_name, title, r_content_size";
		}
	}

	public static String getContentAsString(IDfSysObject dmDoc) throws DfException {
		if (dmDoc.getContentSize() > 0) {
			try (ByteArrayInputStream in = dmDoc.getContent()) {
				int n = in.available();
				if (n > 0) {
					byte[] buff = new byte[n];
					in.read(buff, 0, n);
					String str = new String(buff);
					return str;
				}
			} catch (IOException ex) {
				throw new DfException(ex);
			}
		}

		throw new DfException("No content");
	}

	public static File exportFile(IDfSysObject dmDoc, String destination) throws DfException {
		IDfExportOperation exportOp = new DfClientX().getExportOperation();
		exportOp.setDestinationDirectory(destination);
		IDfExportNode node = (IDfExportNode) exportOp.add(dmDoc);

		boolean result = exportOp.execute();
		if (!result) {
			IDfList errors = exportOp.getErrors();
			DfException ex = null;
			for (int i = 0; i < errors.getCount(); i++) {
				IDfOperationError err = (IDfOperationError) errors.get(i);
				ex = new DfException(err.getMessage(), ex);
			}
			throw ex;
		}

		return new File(node.getFilePath());
	}

	public static void validatePath(IDfSession dmSession, String path) throws DfException {
		IDfFolder dmFolder = dmSession.getFolderByPath(path);
		if (dmFolder == null) {
			throw new DfException("Path not found: " + path);
		}
	}
}
