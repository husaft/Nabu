package ch.unizh.ori.common.text;

import ch.unizh.ori.common.text.helper.AkkadianHelper;
import ch.unizh.ori.common.text.helper.SanskritHelper;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Conversion {
	public static final String ACCADIAN = "accadian";
	public static final String SANSKRIT = "sanskrit";
	public static final String EGYPTIAN = "egyptian";

	public static String toUnicode(byte[] code, String encoding) {
		try {
			if ("accadian".equals(encoding))
				return AkkadianHelper.ascii2unicode(new String(code, "US-ASCII"));
			if ("sanskrit".equals(encoding))
				return SanskritHelper.ascii2unicode(new String(code, "US-ASCII"));
			if ("egyptian".equals(encoding)) {
				return new String(code, "US-ASCII");
			}
			return new String(code, encoding);
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static byte[] fromUnicode(String str, String encoding) {
		try {
			if ("accadian".equals(encoding))
				return AkkadianHelper.unicode2ascii(str).getBytes("US-ASCII");
			if ("sanskrit".equals(encoding))
				return SanskritHelper.unicode2ascii(str).getBytes("US-ASCII");
			if ("egyptian".equals(encoding)) {
				return str.getBytes("US-ASCII");
			}
			return str.getBytes(encoding);
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static void setColumn(PreparedStatement stmt, int col, String str, String enc) throws SQLException {
		if (enc == null) {
			stmt.setString(col, str);
		} else {
			stmt.setBytes(col, fromUnicode(str, enc));
		}
	}

	public static String getColumn(ResultSet rs, int col, String enc) throws SQLException {
		if (enc == null) {
			return rs.getString(col);
		}
		return toUnicode(rs.getBytes(col), enc);
	}
}