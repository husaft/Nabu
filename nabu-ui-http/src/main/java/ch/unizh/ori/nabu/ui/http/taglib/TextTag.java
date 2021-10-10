package ch.unizh.ori.nabu.ui.http.taglib;

import ch.unizh.ori.nabu.core.impl.SrvPageContext;
import ch.unizh.ori.common.text.OldScript;
import ch.unizh.ori.common.text.OldText;
import ch.unizh.ori.nabu.core.Utilities;
import java.io.IOException;
import java.io.Writer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class TextTag extends BodyTagSupport {

	private static final long serialVersionUID = 8202817291737889779L;

	private OldText value;
	private String mode;

	public void otherDoStartTagOperations() {
		try {
			JspWriter out = this.pageContext.getOut();
			String renderMode = "as_is";
			if (getMode() != null) {
				renderMode = getMode();
			} else {
				OldScript script = getValue().getScript();
				if (script == null) {
					out.print(getValue().getUnicodeString());
					return;
				}
				renderMode = script.getRenderMode(new SrvPageContext(this.pageContext));
			}
			if ("as_xml_entities".equals(renderMode)) {
				out.print(Utilities.htmlEntities(getValue().getUnicodeString()));
			} else if ("as_image".equals(renderMode)) {
				String path = getValue().getImageURL();
				path = ((HttpServletRequest) this.pageContext.getRequest()).getContextPath() + path;
				path = ((HttpServletResponse) this.pageContext.getResponse()).encodeURL(path);
				out.print("<img src=\"" + path + "\">");
			} else if ("as_is".equals(renderMode)) {
				out.print(getValue().getUnicodeString());
			} else {
				this.pageContext.getServletContext().log("Unknown Render Mode");
			}
		} catch (IOException ex) {
			this.pageContext.getServletContext().log("TextTag", ex);
		}
	}

	public boolean theBodyShouldBeEvaluated() {
		return false;
	}

	public void otherDoEndTagOperations() {
	}

	public boolean shouldEvaluateRestOfPageAfterEndTag() {
		return true;
	}

	public int doStartTag() throws JspException, JspException {
		otherDoStartTagOperations();

		if (theBodyShouldBeEvaluated()) {
			return 2;
		}
		return 0;
	}

	public int doEndTag() throws JspException, JspException {
		otherDoEndTagOperations();

		if (shouldEvaluateRestOfPageAfterEndTag()) {
			return 6;
		}
		return 5;
	}

	public OldText getValue() {
		return this.value;
	}

	public void setValue(OldText value) {
		this.value = value;
	}

	public void writeTagBodyContent(JspWriter out, BodyContent bodyContent) throws IOException {
		bodyContent.writeOut((Writer) out);
		bodyContent.clearBody();
	}

	public void handleBodyContentException(Exception ex) throws JspException {
		throw new JspException("error in TextTag: " + ex);
	}

	public int doAfterBody() throws JspException {
		try {
			JspWriter out = getPreviousOut();
			BodyContent bodyContent = getBodyContent();

			writeTagBodyContent(out, bodyContent);
		} catch (Exception ex) {
			handleBodyContentException(ex);
		}

		if (theBodyShouldBeEvaluatedAgain()) {
			return 2;
		}
		return 0;
	}

	public boolean theBodyShouldBeEvaluatedAgain() {
		return false;
	}

	public String getMode() {
		return this.mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
}