package ch.unizh.ori.common.text.helper;

public class ThaiHelper {

	public static String VOWELS = "aiWueEoOY";
	public static String CONS = "kcdtpbhyfslgnmw?";
	public static String TONES = "1234";
	public static char[] ACCENTS = new char[] { '̀', '̂', '́', '̌' };

	public static String ASCII_CHARS = "WEYO?";
	public static String IPA_CHARS = "ɯɛɤɔʔ";

	public static String convertPronunciation(String in) {
		StringBuffer ret = new StringBuffer(in.length());
		boolean inV = false;
		int startV = -1;
		for (int i = 0; i < in.length(); i++) {
			char ch = in.charAt(i);
			if (VOWELS.indexOf(ch) >= 0) {
				if (!inV) {
					startV = i;
				}
				inV = true;
				ret.append(ch);
			} else {

				inV = false;
				int tone = TONES.indexOf(ch);
				if (tone >= 0 && startV >= 0)

				{
					char acc = ACCENTS[tone];
					if (ret.charAt(startV) == 'i') {
						ret.setCharAt(startV, 'ı');
					}
					ret.insert(startV + 1, acc);

					startV = -1;
				} else {
					if (CONS.indexOf(ch) < 0) {
						startV = -1;
					}
					ret.append(ch);
				}
			}
		}
		String str = ret.toString();
		for (int j = 0; j < ASCII_CHARS.length(); j++)
			str = str.replace(ASCII_CHARS.charAt(j), IPA_CHARS.charAt(j));
		return str;
	}
}