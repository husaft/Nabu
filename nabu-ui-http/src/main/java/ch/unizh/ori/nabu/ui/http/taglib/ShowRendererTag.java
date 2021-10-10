package ch.unizh.ori.nabu.ui.http.taglib;

import ch.unizh.ori.nabu.core.QuestionIterator;
import ch.unizh.ori.nabu.ui.http.HttpRenderer;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class ShowRendererTag extends TagSupport {

	private static final long serialVersionUID = 3050825487610184407L;

	public int doStartTag() throws JspException {
		QuestionIterator iter = (QuestionIterator) this.pageContext.getSession().getAttribute("iter");
		HttpRenderer r = (HttpRenderer) iter.getRenderer();

		ServletRequest request = this.pageContext.getRequest();
		ServletResponse response = this.pageContext.getResponse();
		if (r == null) {
			this.pageContext.getServletContext().log("ShowRendererTag: Renderer is null");
			return 0;
		}
		String jspPath = r.getJspPath();
		try {
			RequestDispatcher requestDispatcher = this.pageContext.getServletContext().getRequestDispatcher(jspPath);

			this.pageContext.getOut().flush();
			requestDispatcher.include(request, response);
		} catch (ServletException e) {
			throw new JspException(e);
		} catch (IOException e) {
			throw new JspException(e);
		}

		return 0;
	}
}