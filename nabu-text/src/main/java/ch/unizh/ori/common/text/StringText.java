package ch.unizh.ori.common.text;

public class StringText implements Text {

	private Script script;
	private String text;

	public StringText(Script script, String text) {
		this.script = script;
		this.text = text;
	}

	public Script getScript() {
		return this.script;
	}

	public String getText() {
		return this.text;
	}
}