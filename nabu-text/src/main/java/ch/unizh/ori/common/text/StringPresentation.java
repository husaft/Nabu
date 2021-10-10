package ch.unizh.ori.common.text;

import ch.unizh.ori.nabu.core.DefaultDescriptable;

public class StringPresentation extends DefaultDescriptable implements Presentation {

	private static final long serialVersionUID = 5673518500966651886L;

	private String font;
	private String fontSize;
	private String outTransliteration;
	private Script script;

	public StringPresentation() {
	}

	public StringPresentation(String id, String name) {
		super(id, name);
	}

	public String getOutText(Object value, String enc) {
		return (String) this.script.convert(value, enc, getOutTransliteration());
	}

	public String getFont() {
		return this.font;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public String getOutTransliteration() {
		return this.outTransliteration;
	}

	public void setOutTransliteration(String outEnc) {
		this.outTransliteration = outEnc;
	}

	public Script getScript() {
		return this.script;
	}

	public void setScript(Script script) {
		this.script = script;
	}

	public String getFontSize() {
		return this.fontSize;
	}

	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}
}