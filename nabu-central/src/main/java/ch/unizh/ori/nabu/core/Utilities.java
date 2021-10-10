package ch.unizh.ori.nabu.core;

import ch.unizh.ori.common.text.OldStringText;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class Utilities {
	public static List<String> split(String str, String delim) {
		return split(str, delim, (List<String>) null);
	}

	public static List<String> split(String str, String delim, int n) {
		return split(str, delim, new ArrayList<String>(n));
	}

	public static List<String> split(String str, String delim, List<String> orig) {
		if (orig == null) {
			orig = new ArrayList<String>();
		}
		if (str == null || delim == null) {
			return null;
		}
		StringTokenizer tok = new StringTokenizer(str, delim);
		while (tok.hasMoreTokens()) {
			orig.add(tok.nextToken());
		}
		return orig;
	}

	public static int[] splitIntoInts(String str, String delim) {
		List<String> l = split(str, delim);
		int[] ret = new int[l.size()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = Integer.parseInt((String) l.get(i));
		}
		return ret;
	}

	public static Object dummy(Object in) {
		return in;
	}

	public static OldStringText app2(String a, String b) {
		return new OldStringText(a + " / " + b);
	}

	public static OldStringText app3(String a, String b, String c) {
		return new OldStringText(a + " / " + b + " / " + c);
	}

	public static OldStringText union2(String a, String b) {
		if (isEmpty(a))
			return new OldStringText(b);
		if (isEmpty(b))
			return new OldStringText(a);
		return app2(a, b);
	}

	public static OldStringText union3(String a, String b, String c) {
		if (isEmpty(a))
			return union2(b, c);
		if (isEmpty(b))
			return union2(a, c);
		if (isEmpty(c))
			return union2(a, b);
		return app3(a, b, c);
	}

	public static String union(String[] fields, Map<Object, Object> map, String delim) {
		StringBuffer ret = new StringBuffer();
		boolean first = true;
		for (int i = 0; i < fields.length; i++) {
			String f = fields[i];
			String q = (String) map.get(f);
			if (!isEmpty(q)) {

				if (!first) {
					ret.append(delim);
				}
				ret.append(q);
			}
		}
		return ret.toString();
	}

	public static boolean isEmpty(String a) {
		return (a == null || a.length() == 0);
	}

	public static String htmlEntities(String str) {
		String ret = "";
		for (int i = 0; i < str.length(); i++) {
			ret = ret + "&#x" + Integer.toHexString(str.charAt(i)) + ";";
		}
		return ret;
	}
}