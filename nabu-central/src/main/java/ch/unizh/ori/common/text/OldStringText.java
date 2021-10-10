package ch.unizh.ori.common.text;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class OldStringText extends DefaultText {

	private static final long serialVersionUID = -9191646919426515165L;

	private String string;
	public static String imageUrl = "/string?string=";

	public OldStringText(String string) {
		this(string, null);
	}

	public OldStringText(String string, OldScript script) {
		this.string = string;
		setScript(script);
	}

	public String getAsciiString() {
		return this.string;
	}

	public String getImageURL() {
		return getImageURL(this.string);
	}

	public static String getImageURL(String string) {
		try {
			String str = imageUrl + URLEncoder.encode(string, "UTF-8");
			System.out.println("imageUrl: " + str);
			return str;
		} catch (UnsupportedEncodingException ex) {
			return "";
		}
	}

	public static void setImageURL(String imageUrl) {
		OldStringText.imageUrl = imageUrl;
	}

	public OldText getTransliteration() {
		return this;
	}

	public String getUnicodeString() {
		return this.string;
	}

	public static OldText string2Text(String str) {
		return new OldStringText(str);
	}
}