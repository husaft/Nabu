package ch.unizh.ori.nabu.ui.http.taglib;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class CheckboxTag extends TagSupport {

	private static final long serialVersionUID = 358090042919250163L;

	private String name;
	private Boolean value;

	public int doStartTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		try {
			out.print("<input type=CHECKBOX name=\"");
			out.print(this.name);
			out.print("\"");

			if (this.value != null && this.value.booleanValue()) {
				out.print(" checked");
			}
			out.print(">");
		} catch (IOException ex) {
			throw new JspException(ex);
		}
		return 0;
	}

	public void setName(String string) {
		this.name = string;
	}

	public void setValue(Boolean s) {
		this.value = s;
	}
}