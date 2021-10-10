package ch.unizh.ori.common.prefs;

import ch.unizh.ori.nabu.core.Descriptable;

public interface Preferences extends Descriptable {

	Preferences getParent();

	Object getPref(String paramString, Object paramObject);

	Preferences providingPreferences(String paramString);

	void setPref(String paramString, Object paramObject);

	PrefDesc getPrefDesc(String paramString);

	String getPrefString(String paramString1, String paramString2);

	int getPrefInt(String paramString, int paramInt);

	boolean isPref(String paramString, boolean paramBoolean);

	Object getDirectPref(String paramString);

}