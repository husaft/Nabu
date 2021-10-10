package ch.unizh.ori.tuppu.hieroglyph;

import ch.unizh.ori.common.text.StringText;
import ch.unizh.ori.common.text.Text;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class EgyptianText {
	private String manCode;
	private Text translit;
	public static String imageUrl;

	public EgyptianText(String manCode, String translit) {
		this.manCode = manCode;
		this.translit = (Text) new StringText(null, translit);
	}

	public String getAsciiString() {
		return this.manCode;
	}

	public String getImageURL() {
		return getImageURL(this.manCode);
	}

	public static String getImageURL(String string) {
		try {
			return imageUrl + URLEncoder.encode(string, "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			return "";
		}
	}

	public static void setImageURL(String imageUrl) {
		EgyptianText.imageUrl = imageUrl;
	}

	public String getUnicodeString() {
		return this.manCode;
	}

	public Text getTransliteration() {
		return this.translit;
	}

	public static String string2speech(String s) {
		if (s == null)
			return null;
		StringBuffer ret = new StringBuffer(s.length());
		boolean wasCons = false;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
			case 'A':
			case 'a':
				if (comes("aA", s, i)) {
					ret.append("a ");
				} else {
					ret.append("a");
				}
				wasCons = false;
				break;
			case 'i':
			case 'j':
			case 'y':
				if (!comes("aAijw", s, i)) {
					ret.append("ee");
					wasCons = false;
					break;
				}
				ret.append("y");
				wasCons = true;
				break;

			case 'w':
				if (!comes("aAijw", s, i)) {
					ret.append("ooh");
					wasCons = false;
					break;
				}
				ret.append("wh");
				wasCons = true;
				break;

			case 'H':
			case 'X':
			case 'h':
			case 'x':
				if (wasCons)
					ret.append("e");
				ret.append("kh");
				wasCons = true;
				break;
			case 'q':
				if (wasCons)
					ret.append("e");
				ret.append("k");
				wasCons = true;
				break;
			case 'S':
				if (wasCons)
					ret.append("e");
				ret.append("sh");
				wasCons = true;
				break;
			case 'T':
				if (wasCons)
					ret.append("e");
				ret.append("ty");
				wasCons = true;
				break;
			case 'D':
				if (wasCons)
					ret.append("e");
				ret.append("j");
				wasCons = true;
				break;
			case 'b':
			case 'd':
			case 'f':
			case 'g':
			case 'k':
			case 'm':
			case 'n':
			case 'p':
			case 'r':
			case 's':
			case 't':
			case 'z':
				if (wasCons)
					ret.append("e");
				ret.append(c);
				wasCons = true;
				break;
			case '(':
				break;
			default:
				ret.append(c);
				wasCons = false;
				break;
			}

		}
		return ret.toString();
	}

	private static boolean comes(String pattern, String s, int i) {
		if (i + 1 == s.length())
			return false;
		return (pattern.indexOf(s.charAt(i + 1)) >= 0);
	}
}