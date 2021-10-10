package ch.unizh.ori.common.text;

import ch.unizh.ori.nabu.core.api.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import ch.unizh.ori.nabu.core.api.PageContext;

public abstract class OldScript {
	public static final String UNICODE = "unicode";
	public static final String ASCII = "ascii";
	public static final String TRANSLITERATION = "transliteration";
	public static final String AS_IS = "as_is";
	public static final String AS_XML_ENTITIES = "as_xml_entities";
	public static final String AS_IMAGE = "as_image";
	public static final String PREF_RENDER_MODE = "nabu.tupp.Script.renderMode";
	public static final String PREF_RENDER_DEFAULT = "nabu.tupp.Script.renderMode_default";
	private String name;
	private String defaultRenderMode = "as_is";

	public abstract List<Boolean> getForms();

	public abstract OldText getText(ResultSet paramResultSet, int paramInt) throws SQLException;

	public abstract void setText(PreparedStatement paramPreparedStatement, int paramInt, String paramString)
			throws SQLException;

	public String getRenderMode(PageContext pageContext) {
		User u = User.getUser(pageContext);
		String r = u.getPreferences().getProperty("nabu.tupp.Script.renderMode_" + getName());
		System.out.println("pref: nabu.tupp.Script.renderMode_" + getName() + ": " + r);
		if (r == null) {
			r = u.getPreferences().getProperty("nabu.tupp.Script.renderMode_default", getDefaultRenderMode());
			System.out.println("pref: nabu.tupp.Script.renderMode_default: " + r);
		}
		System.out.println("renderMode: " + r);
		return r;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefaultRenderMode() {
		return this.defaultRenderMode;
	}

	public void setDefaultRenderMode(String defaultRenderMode) {
		this.defaultRenderMode = defaultRenderMode;
	}
}