package ch.unizh.ori.nabu.ui.http.taglib;

import ch.unizh.ori.nabu.core.QuestionIterator;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class ShowQuestionTag extends TagSupport {

	private static final long serialVersionUID = 1080596873699233816L;

	public static final String SHOW_QUESTION_KEY = "showQuestion.jsp.path";

	public int doStartTag() throws JspException {
		QuestionIterator iter = (QuestionIterator) this.pageContext.getSession().getAttribute("iter");
		this.pageContext.setAttribute("r", iter.getRenderer());
		return 1;
	}

	public int doEndTag() throws JspException {
		QuestionIterator iter = (QuestionIterator) this.pageContext.getSession().getAttribute("iter");
		if (iter.getRenderer().isShowSolution()) {
			iter.next();
		}
		return 6;
	}
}