package ch.unizh.ori.common.prefs;

import ch.unizh.ori.nabu.core.DefaultDescriptable;
import java.util.HashMap;
import java.util.Map;

public class DefaultPreferences extends DefaultDescriptable implements Preferences {

	private static final long serialVersionUID = -2334570701119320156L;

	private Preferences parent = null;

	private Map<String, Object> prefs = new HashMap<String, Object>();

	public Object getPref(String name, Object def) {
		Preferences providingPreferences = providingPreferences(name);
		if (providingPreferences == null)
			return def;
		return providingPreferences.getDirectPref(name);
	}

	public void setPref(String name, Object value) {
		if (value != null) {
			this.prefs.put(name, value);
		} else {
			this.prefs.remove(name);
		}
	}

	public Preferences providingPreferences(String name) {
		while (true) {
			Preferences preferences = this;
			while (preferences.getDirectPref(name) == null) {
				preferences = preferences.getParent();
				if (preferences == null)
					break;
			}
			if (preferences != null)
				return preferences;
			int i = name.lastIndexOf(' ');
			if (i < 0) {
				name = null;
			} else {
				name = name.substring(0, i);
			}
			if (name == null)
				return null;
		}
	}

	public String getPrefString(String name, String def) {
		Object o = getPref(name, def);
		return (o == null) ? def : o.toString();
	}

	public int getPrefInt(String name, int def) {
		Object o = getPref(name, null);
		if (o instanceof Number) {
			Number n = (Number) o;
			return n.intValue();
		}
		if (o instanceof String) {
			String s = (String) o;
			return Integer.parseInt(s);
		}
		if (o != null) {
			throw new IllegalArgumentException();
		}
		return def;
	}

	public boolean isPref(String name, boolean def) {
		Object o = getPref(name, null);
		if (o == null)
			return def;
		if (o instanceof Boolean)
			return ((Boolean) o).booleanValue();
		if (o instanceof String) {
			String s = (String) o;
			if (s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("on") || s.equalsIgnoreCase("true")) {
				return true;
			}
			if (s.equalsIgnoreCase("no") || s.equalsIgnoreCase("off") || s.equalsIgnoreCase("false")) {
				return false;
			}
			throw new IllegalArgumentException("Boolean value from String " + s + " not recognized");
		}
		throw new IllegalArgumentException("Cannot parse value " + o);
	}

	public Preferences getParent() {
		return this.parent;
	}

	public void setParent(Preferences parent) {
		this.parent = parent;
	}

	public PrefDesc getPrefDesc(String name) {
		return null;
	}

	public Object getDirectPref(String name) {
		return this.prefs.get(name);
	}
}