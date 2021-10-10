package ch.unizh.ori.nabu.ui.http.taglib;

import ch.unizh.ori.nabu.core.QuestionIterator;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class RendererTag extends TagSupport {

	private static final long serialVersionUID = 1155368565701266623L;

	public int doStartTag() throws JspException {
		QuestionIterator iter = (QuestionIterator) this.pageContext.getSession().getAttribute("iter");
		this.pageContext.setAttribute("r", iter.getRenderer());
		return 1;
	}

	public int doEndTag() throws JspException {
		return 6;
	}
}