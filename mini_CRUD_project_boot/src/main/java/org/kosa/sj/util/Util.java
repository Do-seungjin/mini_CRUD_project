package org.kosa.sj.util;

public class Util {
	public static int parseInt(String str, int defaultValue) {
		try {
			if (str != null && str.length() != 0) {
				return Integer.parseInt(str);
			}
		} catch (Exception e) {
		}
		return defaultValue;
	}

}
