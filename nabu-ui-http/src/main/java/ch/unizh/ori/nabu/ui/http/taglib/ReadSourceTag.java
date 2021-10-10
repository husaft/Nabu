package ch.unizh.ori.nabu.ui.http.taglib;

import ch.unizh.ori.nabu.core.Central;
import ch.unizh.ori.nabu.ui.http.HttpCentral;
import ch.unizh.ori.nabu.voc.Source;
import java.io.StringReader;
import java.net.URL;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.apache.commons.digester.Digester;

public class ReadSourceTag extends BodyTagSupport {

	private static final long serialVersionUID = -4992358153307963819L;

	private HttpCentral central;
	private String sourceXml;
	private URL base;

	@SuppressWarnings("rawtypes")
	public int doStartTag() throws JspException {
		try {
			if (this.base == null) {
				this.base = new URL(this.central.getUploadVocLocation());
			}

			this.sourceXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + this.sourceXml;
			System.err.println(this.sourceXml);
			Digester digester = new Digester();
			Central.configureSource(digester, "src");
			Source<?> src = (Source) digester.parse(new StringReader(this.sourceXml));
			List lessons = src.readLections(this.base);
			this.pageContext.setAttribute("lessons", lessons);
		} catch (Exception e) {
			throw new JspException(e);
		}

		return 2;
	}

	public int doEndTag() throws JspException {
		init();
		return 6;
	}

	private void init() {
		this.central = null;
		this.sourceXml = null;
		this.base = null;
	}

	public void setCentral(HttpCentral central) {
		this.central = central;
	}

	public String getSourceXml() {
		return this.sourceXml;
	}

	public void setSourceXml(String xml) {
		this.sourceXml = xml;
	}

	public URL getBase() {
		return this.base;
	}

	public void setBase(URL base) {
		this.base = base;
	}
}