package ch.unizh.ori.nabu.ui.http.taglib;

import ch.unizh.ori.common.text.PlotterPresentation;
import ch.unizh.ori.nabu.core.QuestionIterator;
import ch.unizh.ori.nabu.ui.http.HttpCentral;
import ch.unizh.ori.nabu.voc.ImgColumn;
import ch.unizh.ori.nabu.voc.ModeField;
import ch.unizh.ori.nabu.voc.StringColumn;
import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class ImageTag extends TagSupport {

	private static final long serialVersionUID = -7950736165899593538L;

	private ModeField mf;
	private String var;

	@SuppressWarnings("rawtypes")
	public int doStartTag() throws JspException {
		QuestionIterator iter = (QuestionIterator) this.pageContext.getSession().getAttribute("iter");

		Map q = (Map) iter.getQuestion();
		String url = null;
		if (this.mf.getColumn() instanceof ImgColumn) {
			ImgColumn ic = (ImgColumn) this.mf.getColumn();
			if (ic != null) {
				String name = (String) q.get(ic.getId());
				String prefix = ic.getPrefix();

				if (name != null) {
					url = prefix + name;
				}
			}
		} else if (this.mf.getColumn() instanceof StringColumn) {
			StringColumn sc = (StringColumn) this.mf.getColumn();
			PlotterPresentation p = (PlotterPresentation) this.mf.getPresentation();
			HttpCentral httpCentral = (HttpCentral) sc.getVoc().getCentral();
			url = httpCentral.getPlotterUrl(p, sc.getScript(), q.get(this.mf.getKey()), sc.getTransliteration());
		}

		if (url != null) {
			this.pageContext.setAttribute(this.var, url);
			return 1;
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