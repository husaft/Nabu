package ch.unizh.ori.nabu.ui.http.taglib;

import ch.unizh.ori.nabu.core.DefaultQuestionIterator;
import ch.unizh.ori.nabu.ui.Renderer;
import ch.unizh.ori.nabu.ui.http.HttpMappingRenderer;
import ch.unizh.ori.nabu.voc.FieldStream;
import ch.unizh.ori.nabu.voc.Mode;
import ch.unizh.ori.nabu.voc.Vocabulary;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class SelectVocTag extends TagSupport {

	private static final long serialVersionUID = 5406650829280405197L;

	private Vocabulary voc;
	private String show;

	public int doStartTag() throws JspException {
		ServletRequest request = this.pageContext.getRequest();
		ServletResponse response = this.pageContext.getResponse();
		HttpSession session = this.pageContext.getSession();

		String mode = request.getParameter("mode");
		String fillVariables = request.getParameter("fillVariables");
		if (mode != null && !"on".equals(fillVariables)) {

			List<FieldStream> lections = new ArrayList<FieldStream>();
			boolean all = isOn(request, "l-all");
			Iterator<FieldStream> iterator = this.voc.getLections().iterator();
			while (iterator.hasNext()) {

				FieldStream fs = iterator.next();

				if (all || isOn(request, "l." + fs.getId())) {
					lections.add(fs);
				}
			}

			Mode m = (Mode) this.voc.getModes().get(mode);
			HttpMappingRenderer httpMappingRenderer = new HttpMappingRenderer(m);

			DefaultQuestionIterator iter = this.voc.createIter(lections, (Renderer) httpMappingRenderer, m.getFilter(),
					m);
			iter.init();
			session.setAttribute("iter", iter);

			session.setAttribute("first", "yes");

			session.setAttribute("newVocUrl", getOriginalUrl((HttpServletRequest) request) + "&fillVariables=on");
			session.setAttribute("newVocName", this.voc.getName());

			iter.next();

			try {
				this.pageContext.getServletContext().getRequestDispatcher(this.show).forward(request, response);
				return 5;
			} catch (ServletException e) {
				throw new JspException(e);
			} catch (IOException e) {
				throw new JspException(e);
			}
		}
		return 1;
	}

	public static String getOriginalUrl(HttpServletRequest request) {
		/* String serverName = request.getServerName();
		int serverPort = request.getServerPort();
		String contextPath = request.getContextPath(); */
		String servletPath = request.getServletPath();
		if (servletPath == null)
			servletPath = "";
		String pathInfo = request.getPathInfo();
		if (pathInfo == null)
			pathInfo = "";
		String queryString = request.getQueryString();
		if (queryString == null)
			queryString = "";
		return servletPath + pathInfo + "?" + queryString;
	}

	public static boolean isOn(ServletRequest request, String param) {
		String s = request.getParameter(param);
		return (s != null);
	}

	public String getVoc() {
		return this.voc.getId();
	}

	public void setVoc(String vocabulary) {
		this.voc = (Vocabulary) this.pageContext.findAttribute(vocabulary);
	}

	public String getShow() {
		return this.show;
	}

	public void setShow(String string) {
		this.show = string;
	}
}