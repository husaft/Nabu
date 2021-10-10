package ch.unizh.ori.nabu.ui.http.taglib;

import ch.unizh.ori.nabu.core.QuestionIterator;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class StatsTag extends TagSupport {

	private static final long serialVersionUID = -4232862805801759618L;

	@SuppressWarnings("deprecation")
	public int doStartTag() throws JspException {
		QuestionIterator iter = (QuestionIterator) this.pageContext.getSession().getAttribute("iter");
		this.pageContext.setAttribute("stats", iter.getStatistics());
		Object q = iter.getQuestion();
		int times = iter.getTimesForProblem(q);
		boolean hasProblems = (times > 0);
		this.pageContext.setAttribute("hasProblems", new Boolean(hasProblems));
		if (hasProblems) {
			this.pageContext.setAttribute("times", new Integer(times));
		}

		return 1;
	}
}