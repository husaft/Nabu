package ch.unizh.ori.common.text;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OldUnicodeScript extends OldScript {
	public static final OldScript SANSKRIT = new OldUnicodeScript("sanskrit", "sanskrit");
	public static final OldScript ARABIC = new OldUnicodeScript("MacArabic", "arabic");
	public static final OldScript ACCADIAN = new OldUnicodeScript("accadian", "accadian");
	public static final OldScript HEBREW = new OldUnicodeScript("UTF-8", "hebrew");
	public static final OldScript NULL = new OldUnicodeScript(null, null);

	static {
		SANSKRIT.setDefaultRenderMode("as_image");
		ARABIC.setDefaultRenderMode("as_is");
		ACCADIAN.setDefaultRenderMode("as_xml_entities");
		HEBREW.setDefaultRenderMode("as_xml_entities");
	}

	private String enc;

	public OldUnicodeScript(String enc) {
		this.enc = enc;
	}

	public OldUnicodeScript(String enc, String name) {
		this.enc = enc;
		setName(name);
	}

	public List<Boolean> getForms() {
		return null;
	}

	public OldText getText(ResultSet rs, int col) throws SQLException {
		return new OldStringText(Conversion.getColumn(rs, col, this.enc), this);
	}

	public void setText(PreparedStatement stmt, int col, String str) throws SQLException {
		Conversion.setColumn(stmt, col, str, this.enc);
	}
}