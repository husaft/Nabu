package ch.unizh.ori.nabu.ui.http.taglib;

import ch.unizh.ori.nabu.core.QuestionIterator;
import ch.unizh.ori.nabu.ui.http.HttpRenderer;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class FocusTag extends TagSupport {

	private static final long serialVersionUID = -6096586696613680994L;

	private String ques;

	public int doStartTag() throws JspException {
		String focus;
		QuestionIterator iter = (QuestionIterator) this.pageContext.getSession().getAttribute("iter");
		HttpRenderer r = (HttpRenderer) iter.getRenderer();
		JspWriter out = this.pageContext.getOut();

		if (r == null) {
			return 0;
		}

		if (r.isShowSolution()) {
			focus = "ok";
		} else {
			focus = "q_" + r.getFocusKey();
		}
		try {
			out.write(focusString(this.ques, focus));
		} catch (IOException e) {
			throw new JspException(e);
		}

		return 0;
	}

	public static String focusString(String ques, String focus) {
		String str = "<script language=\"JavaScript\">\n<!--\nif (document." + ques + " && document." + ques + "."
				+ focus + ")\n" + "  document." + ques + "." + focus + ".focus();\n"
				+ ((focus == "ok") ? "" : ("  document." + ques + "." + focus + ".select();\n")) + "//-->\n"
				+ "</script>";

		return str;
	}

	public String getQues() {
		return this.ques;
	}

	public void setQues(String string) {
		this.ques = string;
	}
}