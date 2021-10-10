package ch.unizh.ori.nabu.ui.http.taglib;

import ch.unizh.ori.nabu.core.QuestionIterator;
import ch.unizh.ori.nabu.voc.ModeField;
import ch.unizh.ori.nabu.voc.Sotm;
import ch.unizh.ori.nabu.voc.StringColumn;
import ch.unizh.ori.nabu.voc.Voice;
import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class SoundTag extends TagSupport {

	private static final long serialVersionUID = 910985500321767227L;

	private ModeField mf;
	private String var;

	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	public int doStartTag() throws JspException {
		QuestionIterator iter = (QuestionIterator) this.pageContext.getSession().getAttribute("iter");

		Sotm sotm = this.mf.getColumn().getVoc().getSound(this.mf.getKey());
		boolean hasSotm = (sotm != null && sotm.getVoices().size() > 0);
		if (hasSotm) {
			this.pageContext.getRequest().setAttribute("hasSotm", Boolean.TRUE);
		}
		if (this.pageContext.getRequest().getParameter("play") == null) {
			return 0;
		}
		if (hasSotm) {
			int i = (int) (Math.random() * sotm.getVoices().size());
			Voice v = sotm.getVoices().get(i);
			Map q = (Map) iter.getQuestion();
			String toSay = null;
			if (this.mf.getColumn() instanceof StringColumn) {
				StringColumn sc = (StringColumn) this.mf.getColumn();
				toSay = (String) q.get(this.mf.getColumn().getId());
			}
			String name = sotm.getUtterance(toSay, q);
			String prefix = (String) this.pageContext.getSession()
					.getAttribute("prefix." + this.mf.getColumn().getId());
			if (prefix == null) {
				prefix = v.getPrefix();
			}

			if (name != null) {
				String s = prefix + name;
				this.pageContext.setAttribute(this.var, s);
				return 1;
			}
		}

		return 0;
	}

	public void setModeField(ModeField mf) {
		this.mf = mf;
	}

	public void setVar(String string) {
		this.var = string;
	}
}