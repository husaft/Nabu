package ch.unizh.ori.nabu.ui.http.taglib;

import ch.unizh.ori.nabu.core.QuestionIterator;
import ch.unizh.ori.nabu.ui.http.HttpRenderer;
import java.io.IOException;
import java.net.URLEncoder;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class QuestionFormTag extends TagSupport {

	private static final long serialVersionUID = 4920013535318154435L;

	private String next;

	@SuppressWarnings("unused")
	public int doStartTag() throws JspException {
		QuestionIterator iter = (QuestionIterator) this.pageContext.getSession().getAttribute("iter");
		if (iter == null) {
			String message = "Sie haben keine laufende Sitzung (mehr?) :-(<br/>W&auml;hlen Sie doch eine neue aus.";

			try {
				this.pageContext.forward("overview.jsp?message=" + URLEncoder.encode(message, "UTF-8"));
				return 0;
			} catch (ServletException ex) {
				throw new JspException("Problem forwarding to overview.jsp", ex);
			} catch (IOException ex) {
				throw new JspException("Problem forwarding to overview.jsp", ex);
			}
		}
		iter.setProblemsOnly((this.pageContext.getRequest().getParameter("problemsOnly") != null));

		if (iter == null) {
			return goNext();
		}

		if (this.pageContext.getSession().getAttribute("first") == null) {
			evaluate(iter);
		} else {
			this.pageContext.getSession().setAttribute("first", null);
		}

		if (iter.getQuestion() == null) {
			if (iter.isProblemsOnly()) {
				iter.setProblemsOnly(false);
				iter.next();
			}
			if (iter.getQuestion() == null) {

				try {
					HttpServletResponse response = (HttpServletResponse) this.pageContext.getResponse();
					HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
					String url = getNext();
					if (url.startsWith("/"))
						url = request.getContextPath() + url;
					response.sendRedirect(response.encodeRedirectURL(url));
					System.out.println(response.encodeRedirectURL(this.next));
					System.out.println(response.encodeURL(this.next));
				} catch (IOException ex) {
					throw new JspException(ex);
				}
				return 5;
			}
		}

		HttpRenderer r = (HttpRenderer) iter.getRenderer();
		this.pageContext.setAttribute("r", r);

		return 1;
	}

	private int goNext() throws JspException {
		try {
			RequestDispatcher rd = this.pageContext.getServletContext().getRequestDispatcher(this.next);
			rd.forward(this.pageContext.getRequest(), this.pageContext.getResponse());
			return 5;
		} catch (ServletException e) {
			throw new JspException(e);
		} catch (IOException e) {
			throw new JspException(e);
		}
	}

	private void evaluate(QuestionIterator iter) {
		HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
		HttpRenderer r = (HttpRenderer) iter.getRenderer();
		boolean showSolution = (request.getParameter("fail") != null);
		if (r != null) {
			r.processRequest(request, showSolution);
		}
		iter.doEvaluate();
	}

	public int doEndTag() throws JspException {
		QuestionIterator iter = (QuestionIterator) this.pageContext.getSession().getAttribute("iter");
		if (iter != null && iter.getRenderer() != null && iter.getRenderer().isShowSolution()) {
			iter.next();
			this.pageContext.getSession().setAttribute("first", "on");
		}
		return 6;
	}

	public String getNext() {
		return this.next;
	}

	public void setNext(String string) {
		this.next = string;
	}
}