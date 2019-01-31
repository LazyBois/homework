package util;

public class Log {

	public static void info(String msg, Object... params) {
		System.out.println(Utils.createStr(msg, params));
	}

	public static void error(String msg, Object... params) {
		System.err.println(Utils.createStr(msg, params));
	}

}
