package ch.unizh.ori.nabu.core;

import java.util.Properties;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

public class User {
	private Properties preferences = new Properties();

	public Properties getPreferences() {
		return this.preferences;
	}

	public void setPreferences(Properties preferences) {
		this.preferences = preferences;
	}

	public static User getUser(HttpSession session) {
		User u = (User) session.getAttribute("user");
		if (u == null) {
			u = new User();
			session.setAttribute("user", u);
		}
		return u;
	}

	public static User getUser(PageContext pageContext) {
		return getUser(pageContext.getSession());
	}
}