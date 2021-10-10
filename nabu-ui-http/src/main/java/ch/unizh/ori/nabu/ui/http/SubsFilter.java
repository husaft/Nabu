package ch.unizh.ori.nabu.ui.http;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

public class SubsFilter implements Filter {

	@SuppressWarnings("unchecked")
	public void init(FilterConfig config) throws ServletException {
		this.subs = new HashMap<Object, Object>();
		ServletContext application = config.getServletContext();
		File dir = new File(application.getRealPath("/WEB-INF/subs"));
		if (dir.exists() && dir.isDirectory()) {
			for (int i = 0; i < (dir.list()).length; i++) {
				String f = dir.list()[i];
				this.LOGGER.info("Adding subCentral: " + f);
				HttpCentral central = new HttpCentral(application, f);
				this.subs.put(f, central);
				central.setId(f);
				central.setName(f);
				central.setUploadVocLocation(application.getRealPath("/WEB-INF/subs/" + f));
				central.read();
			}
		}
	}

	public void destroy() {
	}

	Logger LOGGER = Logger.getLogger(SubsFilter.class);

	@SuppressWarnings("rawtypes")
	private Map subs;

	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest _request, ServletResponse _response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequestWrapper httpServletRequestWrapper = null;
		HttpServletRequest request = (HttpServletRequest) _request;
		HttpServletResponse response = (HttpServletResponse) _response;

		String thePath = request.getServletPath();

		for (Iterator<String> iter = this.subs.keySet().iterator(); iter.hasNext();) {
			String key = iter.next();
			if (thePath.startsWith("/" + key + "/")) {

				String subpath = thePath.substring(1 + key.length());
				this.LOGGER.debug("key: " + key);

				if (!request.isSecure() && subpath.startsWith("/admin/")) {
					String url = "https://" + request.getServerName() + request.getRequestURI();
					response.sendRedirect(response.encodeRedirectURL(url));
					return;
				}
				this.LOGGER.debug(request.isUserInRole("nabu-tutor") + " " + request.isUserInRole("tutor-" + key));

				HttpCentral c = (HttpCentral) this.subs.get(key);
				if (c == null) {
					throw new IllegalArgumentException("Url no good");
				}
				request.setAttribute("central", c);
				String s = getDispatchPath(subpath, request, response);
				this.LOGGER.debug("s: " + s);
				httpServletRequestWrapper = new HttpServletRequestWrapper(request);
				httpServletRequestWrapper.getRequestDispatcher(s).forward((ServletRequest) httpServletRequestWrapper,
						(ServletResponse) response);
				return;
			}
		}
		chain.doFilter((ServletRequest) httpServletRequestWrapper, (ServletResponse) response);
	}

	public String getDispatchPath(String path, HttpServletRequest request, HttpServletResponse response) {
		String queryString = request.getQueryString();
		if (queryString != null) {
			return path + "?" + queryString;
		}
		return path;
	}
}