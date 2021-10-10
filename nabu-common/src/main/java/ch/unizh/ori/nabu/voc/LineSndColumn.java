package ch.unizh.ori.nabu.voc;

import java.text.MessageFormat;
import java.util.Map;

public class LineSndColumn extends SndColumn {

	private static final long serialVersionUID = 5917728179125184389L;

	private String patternString;
	private MessageFormat mf;
	private int idFill = 3;
	private int lessonFill = 1;

	public String getUtterance(String toSay, Map<Object, Object> question) {
		String ret = null;
		Object[] par = { fill(question.get("lesson"), this.lessonFill), fill(question.get("id"), this.idFill) };

		if (par[0] != null && par[1] != null && messageFormat() != null) {
			ret = messageFormat().format(par);
		}

		return ret;
	}

	public static String fill(Object o, int i) {
		String str = (o == null) ? "" : o.toString();
		switch (i - str.length()) {
		case 1:
			return "0" + str;
		case 2:
			return "00" + str;
		case 3:
			return "000" + str;
		case 4:
			return "0000" + str;
		case 5:
			return "00000" + str;
		}
		if (str.length() >= i) {
			return str;
		}
		StringBuffer tmp = new StringBuffer();
		for (int j = i - str.length(); j > 1; j--) {
			tmp.append('0');
		}
		return tmp + str;
	}

	private MessageFormat messageFormat() {
		if (this.mf == null && this.patternString != null) {
			this.mf = new MessageFormat(this.patternString);
		}
		return this.mf;
	}

	public String getPattern() {
		return this.patternString;
	}

	public void setPattern(String pattern) {
		this.patternString = pattern;
	}

	public int getIdFill() {
		return this.idFill;
	}

	public void setIdFill(int idFill) {
		this.idFill = idFill;
	}

	public int getLessonFill() {
		return this.lessonFill;
	}

	public void setLessonFill(int lessonFill) {
		this.lessonFill = lessonFill;
	}
}