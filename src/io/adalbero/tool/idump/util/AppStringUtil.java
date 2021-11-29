package io.adalbero.tool.idump.util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang.StringUtils;

public class AppStringUtil {
	public static boolean match(String str, String regex) {
		return str != null && str.matches("(?i)" + regex);
	}

	public static boolean equals(String str, String value) {
		return match(str, value);
	}

	public static boolean startsWith(String str, String value) {
		return match(str, value + ".*");
	}

	public static boolean contains(String str, String value) {
		return match(str, ".*" + value + ".*");
	}

	public static String replace(String str, String regex, String target) {
		return str.replaceAll("(?i)" + regex, target);
	}

	public static boolean isEmpty(String str) {
		return (str == null || str.trim().length() == 0);
	}

	public static String getTimestamp() {
		return DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now());
	}

	public static String truncate(String str, int len) {
		if (len > 4) {
			return StringUtils.abbreviate(str, len);
		} else if (str.length() > len) {
			return str.substring(0, len);
		} else {
			return str;
		}
	}

	public static String alignLeft(String str, int len) {
		return String.format("%-" + len + "s", truncate(str, len));
	}

	public static String fill(String str, int len) {
		return new String(new char[len]).replace("\0", str);
	}

	public static String unquote(String str) {
		if (!isEmpty(str)) {
			return str.replaceAll("\"", "");
		} else {
			return str;
		}

	}

	public static String getFullPath(String base, String path) {

		path = AppStringUtil.isEmpty(path) ? "" : path;

		Path fullPath = Paths.get(base);
		fullPath = fullPath.resolve(path);
		fullPath = fullPath.normalize();

		String result = fullPath.toString();
		result = result.replaceAll("\\\\", "/");

		return result;
	}
}
