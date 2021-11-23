package io.adalbero.tool.idump;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppCommandLine {
	private String cmdLine;
	private String[] args;

	public AppCommandLine(String cmdLine) {
		this.cmdLine = cmdLine;
		this.args = splitArguments(cmdLine);
	}

	public AppCommandLine(String[] args) {
		this.cmdLine = joinArguments(args);
		this.args = args;
	}

	public AppCommandLine(String cmd, String[] args) {
		this(cmd + " " + joinArguments(args));
	}

	public String getCommandLine() {
		return cmdLine;
	}

	public String[] getArguments() {
		return args;
	}

	public String getArgument(int idx) {
		return (idx < args.length) ? args[idx] : null;
	}

	public String getArgumentTilEnd(int idx) {
		if (idx < args.length) {
			return joinArguments(args, idx);
		} else {
			return null;
		}
	}

	@Override
	public String toString() {
		return cmdLine;
	}

	public static String joinArguments(String[] args) {
		return joinArguments(args, 0);
	}

	public static String joinArguments(String[] args, int start) {
		String cmd = "";
		String sep = "";

		for (int i = start; i < args.length; i++) {
			String arg = args[i];

			if (arg.contains(" ")) {
				cmd += sep + "\"" + arg + "\"";
			} else {
				cmd += sep + arg;
			}
			sep = " ";
		}

		return cmd;
	}

	public static String[] splitArguments(String cmd) {
		List<String> list = new ArrayList<>();

		Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(cmd);
		while (m.find()) {
			list.add(m.group(1).replace("\"", ""));
		}
		return list.toArray(new String[] {});
	}

}
