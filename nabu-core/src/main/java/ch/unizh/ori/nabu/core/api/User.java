package ch.unizh.ori.nabu.core.api;

import java.util.Properties;

public interface User {

	Properties getPreferences();

	static User getUser(PageContext pageContext) {
		return null;
	}

}