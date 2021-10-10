package ch.unizh.ori.common.text;

public abstract class DefaultText implements OldText {

	private static final long serialVersionUID = -2578001042571136167L;

	private OldScript script;

	public String getImageURL() {
		return OldStringText.getImageURL(getUnicodeString());
	}

	public OldScript getScript() {
		return this.script;
	}

	public void setScript(OldScript script) {
		this.script = script;
	}

	public abstract String getAsciiString();

	public abstract OldText getTransliteration();

	public abstract String getUnicodeString();
}