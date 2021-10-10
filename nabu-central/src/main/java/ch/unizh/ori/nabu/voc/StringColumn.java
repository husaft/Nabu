package ch.unizh.ori.nabu.voc;

public class StringColumn extends AbstractColumn {

	private static final long serialVersionUID = -4842875862339167696L;

	private String script;
	private String del;
	private String transliteration;

	public Object map(String[] arr) {
		String t = "";
		if (getColumn() < arr.length) {
			t = arr[getColumn()];
		}
		return t;
	}

	public String getDel() {
		return this.del;
	}

	public String getScript() {
		return this.script;
	}

	public void setDel(String del) {
		this.del = del;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getTransliteration() {
		return this.transliteration;
	}

	public void setTransliteration(String transl) {
		this.transliteration = transl;
	}
}