package ch.unizh.ori.common.text.helper;

public class ChineseHelper {

	public static String VOWELS = "aeouüi";
	public static String CONS = "bcdfghjklmnpqrstvwxyz";
	public static String TONES = "1234";
	public static char[] ACCENTS = new char[] { '̄', '́', '̌', '̀' };

	public static String convertPinyin(String in) {
		StringBuffer ret = new StringBuffer(in.length());
		boolean inV = false;
		int startV = -1;
		int endV = -1;
		for (int i = 0; i < in.length(); i++) {
			char ch = in.charAt(i);
			int v = vowelWeight(ch);
			if (v >= 0) {
				endV = i;
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
					int offset = endV;
					if (startV != endV) {
						int weight = vowelWeight(in.charAt(endV));
						for (int j = endV - 1; j >= startV; j--) {
							int weight2 = vowelWeight(in.charAt(j));
							if (weight2 <= weight) {
								offset = j;
								weight = weight2;
							}
						}
					}
					ret.insert(offset + 1, acc);

					startV = endV = -1;
				}

				else

				{
					if (CONS.indexOf(ch) < 0) {
						startV = endV = -1;
					}
					ret.append(ch);
				}
			}
		}
		return ret.toString();
	}

	private static int vowelWeight(char ch) {
		ch = Character.toLowerCase(ch);
		return VOWELS.indexOf(ch);
	}
}