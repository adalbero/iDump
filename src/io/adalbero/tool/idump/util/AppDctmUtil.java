package io.adalbero.tool.idump.util;

import com.documentum.fc.client.DfClient;
import com.documentum.fc.client.DfQuery;
import com.documentum.fc.client.IDfClient;
import com.documentum.fc.client.IDfCollection;
import com.documentum.fc.client.IDfQuery;
import com.documentum.fc.client.IDfSession;
import com.documentum.fc.client.IDfTypedObject;
import com.documentum.fc.client.IDfUser;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfId;
import com.documentum.fc.common.DfLoginInfo;

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
			return "r_object_id, object_name, title";
		}
	}
}
