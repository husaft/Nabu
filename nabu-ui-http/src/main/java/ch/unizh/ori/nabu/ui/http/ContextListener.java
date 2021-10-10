package ch.unizh.ori.nabu.ui.http;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.Logger;

public class ContextListener implements ServletContextListener {
	private static Logger LOGGER = Logger.getLogger(ContextListener.class);

	public void contextInitialized(ServletContextEvent e) {
		LOGGER.info("Starting context " + e.getServletContext().getServletContextName()
				+ ". Logging enabled with level " + LOGGER.getEffectiveLevel());

		HttpCentral c = new HttpCentral(e.getServletContext(), "central");
		c.read();
	}

	public void contextDestroyed(ServletContextEvent e) {
	}
}