package ch.unizh.ori.common.text.helper;

public class ArabicHelper {

	public static final String[][] latinizeTable = new String[][] { { "ـ", "" }, { "ء", "ʾ" }, { "آ", "ʾ\b1" },
			{ "أ", "ʾ" }, { "ؤ", "ʾ" }, { "إ", "ʾ" }, { "ئ", "ʾ" }, { "ُوْ", "uw" }, { "ِيْ", "iy" }, { "ُوَ", "uwa" },
			{ "ِيَ", "iya" }, { "ُوّ", "uww" }, { "ِيّ", "iyy" }, { "َا", "ā" }, { "ُو", "ū" }, { "ِي", "ī" },
			{ "َى", "à" }, { "ُوا ", "û " }, { " الْ", " ăl-" }, { "التّ", "ăt-t" }, { "الثّ", "ăṯ-ṯ" },
			{ "الدّ", "ăd-d" }, { "الذّ", "ăḏ-ḏ" }, { "الرّ", "ăr-r" }, { "الزّ", "ăz-z" }, { "السّ", "ăs-s" },
			{ "اشّ", "ăš-š" }, { "الصّ", "ăṣ-ṣ" }, { "الضّ", "ăḍ-ḍ" }, { "الطّ", "ăṭ-ṭ" }, { "الظّ", "ăẓ-ẓ" },
			{ "اللّ", "ăl-l" }, { "ب", "b" }, { "ة", "ẗ" }, { "ت", "t" }, { "ث", "ṯ" }, { "ج", "ǧ" }, { "ح", "ḥ" },
			{ "خ", "ḫ" }, { "د", "d" }, { "ذ", "ḏ" }, { "ر", "r" }, { "ز", "z" }, { "س", "s" }, { "ش", "š" },
			{ "ص", "ṣ" }, { "ض", "ḍ" }, { "ط", "ṭ" }, { "ظ", "ẓ" }, { "ع", "ʿ" }, { "غ", "ġ" }, { "ـ", "" },
			{ "ف", "f" }, { "ق", "q" }, { "ك", "k" }, { "ل", "l" }, { "م", "m" }, { "ن", "n" }, { "ه", "h" },
			{ "و", "w" }, { "ي", "y" }, { "ًا", ":an" }, { "ً", ":an" }, { "ٌ", ":un" }, { "ٍ", ":in" }, { "َ", "a" },
			{ "ُ", "u" }, { "ِ", "i" }, { "ّ", "+" }, { "ْ", "" }, { "ٰ", "ā" }, { "ٱ", "ĭ" }, { "آ", "ʾā" } };

	public static final String[][] latinizeMbrolaTable = new String[][] { { "ـ", "-" }, { "ء", "?" }, { "آ", "?aa" },
			{ "أ", "?" }, { "ؤ", "?" }, { "إ", "?" }, { "ئ", "?" }, { "ُوْ", "uw" }, { "ِيْ", "iy" }, { "ُوَ", "uwa" },
			{ "ِيَ", "iya" }, { "ُوّ", "uww" }, { "ِيّ", "iyy" }, { "َا", "aa" }, { "ُو", "uu" }, { "ِي", "ii" },
			{ "َى", "aa" }, { "ُوا ", "uu " }, { " الْ", " al " }, { "التّ", "at t" }, { "الثّ", "ath th" },
			{ "الدّ", "ad d" }, { "الذّ", "adh dh" }, { "الرّ", "ar r" }, { "الزّ", "az z" }, { "السّ", "as s" },
			{ "اشّ", "ash sh" }, { "الصّ", "aS S" }, { "الضّ", "aD D" }, { "الطّ", "aT T" }, { "الظّ", "aZ Z" },
			{ "اللّ", "al l" }, { "بّ", "bb" }, { "تّ", "tt" }, { "ثّ", "thth" }, { "جّ", "jj" }, { "حّ", "HH" },
			{ "خّ", "khkh" }, { "دّ", "dd" }, { "ذّ", "dhdh" }, { "رّ", "rr" }, { "زّ", "zz" }, { "سّ", "ss" },
			{ "شّ", "shsh" }, { "صّ", "SS" }, { "ضّ", "DD" }, { "طّ", "TT" }, { "ظّ", "ZZ" }, { "عّ", "99" },
			{ "غّ", "ghgh" }, { "فّ", "ff" }, { "قّ", "qq" }, { "كّ", "kk" }, { "لّ", "ll" }, { "مّ", "mm" },
			{ "نّ", "nn" }, { "هّ", "hh" }, { "وّ", "ww" }, { "يّ", "yy" }, { "ب", "b" }, { "ة", "t" }, { "ت", "t" },
			{ "ث", "th" }, { "ج", "j" }, { "ح", "H" }, { "خ", "kh" }, { "د", "d" }, { "ذ", "dh" }, { "ر", "r" },
			{ "ز", "z" }, { "س", "s" }, { "ش", "sh" }, { "ص", "S" }, { "ض", "D" }, { "ط", "T" }, { "ظ", "Z" },
			{ "ع", "9" }, { "غ", "gh" }, { "ـ", "" }, { "ف", "f" }, { "ق", "q" }, { "ك", "k" }, { "ل", "l" },
			{ "م", "m" }, { "ن", "n" }, { "ه", "h" }, { "و", "w" }, { "ي", "y" }, { "ًا", "an" }, { "ً", "an" },
			{ "ٌ", "un" }, { "ٍ", "in" }, { "َ", "a" }, { "ُ", "u" }, { "ِ", "i" }, { "ّ", "+" }, { "ْ", "" },
			{ "ٰ", "aa" }, { "ٱ", "" }, { "آ", "?aa" }, { " ", " " } };

	public static final String[][] noharakatTable = new String[][] { { "ً", "" }, { "ٌ", "" }, { "ٍ", "" }, { "َ", "" },
			{ "ُ", "" }, { "ِ", "" }, { "ّ", "" }, { "ْ", "" }, { "ٓ", "" } };

	public static final String[][] dotlessTable = new String[][] { { "ي ", "ى " }, { "ب", "ٮ" }, { "ة", "ه" },
			{ "ت", "ٮ" }, { "ث", "ٮ" }, { "ج", "ح" }, { "خ", "ح" }, { "ذ", "د" }, { "ز", "ر" }, { "ش", "س" },
			{ "ض", "ص" }, { "ظ", "ط" }, { "غ", "ع" }, { "ف", "ٯ" }, { "ق", "ٯ" }, { "ن", "ٮ" }, { "ي", "ٮ" } };

	public static final String[][] surrogateTable = new String[][] { { "ٯ", "ڤ" }, { "ٮ", "پ" } };

	public static final String[][] nosurrogateTable = new String[][] { { "ڤ", "ٯ" }, { "پ", "ٮ" } };

	public static final String[][] notatweelTable = new String[][] { { "ـ", "" } };

	public static final String[][] nobracketTable = new String[][] { { "ـ[ـ", "" }, { "ـ]ـ", "" }, { "[", "" },
			{ "]", "" }, { "ـ(ـ", "" }, { "ـ(ـ", "" }, { "(", "" }, { ")", "" }, { "ـ{ـ", "" }, { "ـ}ـ", "" },
			{ "{", "" }, { "}", "" } };

	public static final String[][] hamzalessTable = new String[][] { { "ء", "" }, { "آ", "ا" }, { "أ", "ا" },
			{ "ؤ", "و" }, { "إ", "ا" }, { "ئ ", "ى " }, { "ئ", "ٮ" } };

	public static final String[][] nodiacriticsRegExTable = new String[][] { { "ء", "['ء]" }, { "'", "['ء]" },
			{ "ʿ", "[`ʿ]" }, { "`", "[`ʿ" }, { "ẗ", "[t(ẗ)ṯṭ]" }, { "ṯ", "[t(ẗ)ṯṭ]" }, { "ṭ", "[t(ẗ)ṯṭ]" },
			{ "t", "[t(ẗ)ṯṭ]" }, { "ḏ", "[dḏḍ]" }, { "ḍ", "[dḏḍ]" }, { "d", "[dḏḍ]" }, { "ǧ", "[gǧġ]" },
			{ "ġ", "[gǧġ]" }, { "g", "[gǧġ]" }, { "ḥ", "[hḥḫ]" }, { "ḫ", "[hḥḫ]" }, { "h", "[hḥḫ]" }, { "š", "[sšṣ]" },
			{ "ṣ", "[sšṣ]" }, { "s", "[sšṣ]" }, { "ẓ", "[zẓ]" }, { "z", "[zẓ]" }, { "ā", "[aāàă]" }, { "à", "[aāàă]" },
			{ "ă", "[aāàă]" }, { "a", "[aāàă]" }, { "ū", "[uūû]" }, { "û", "[uūû]" }, { "u", "[uūû]" },
			{ "ī", "[iīĭ]" }, { "ĭ", "[iīĭ]" }, { "i", "[iīĭ]" } };

	public static String dotless(String s) {
		return transcode(s, dotlessTable);
	}

	public static String noharakat(String s) {
		return transcode(s, noharakatTable);
	}

	public static String surrogate(String s) {
		return transcode(s, surrogateTable);
	}

	public static String nosurrogate(String s) {
		return transcode(s, nosurrogateTable);
	}

	public static String notatweel(String s) {
		return transcode(s, notatweelTable);
	}

	public static String nobracket(String s) {
		return transcode(s, nobracketTable);
	}

	public static String hamzaless(String s) {
		return transcode(s, hamzalessTable);
	}

	public static String latinize(String s) {
		return transcode(s, latinizeTable);
	}

	public static String latinizeMbrolaTable(String s) {
		return transcode(s, latinizeMbrolaTable);
	}

	public static String nodiacriticsRegEx(String s) {
		return transcode(s, nodiacriticsRegExTable);
	}

	public static String reduplicate(String srcStr, char shadda) {
		int strLength = srcStr.length();
		if (strLength < 1)
			return "";
		StringBuffer strBuff = new StringBuffer(strLength);
		char oldCh = srcStr.charAt(0);
		strBuff.append(oldCh);
		for (int i = 1; i < strLength; i++) {
			char ch = srcStr.charAt(i);
			if (ch == shadda) {
				strBuff.append(oldCh);
			} else {
				strBuff.append(ch);
			}

			oldCh = ch;
		}
		return strBuff.toString();
	}

	public static String transcode(String srcStr, String[][] codeTable) {
		if (srcStr == null)
			return null;
		int strLength = srcStr.length();
		StringBuffer transStrBuff = new StringBuffer(strLength);
		int strPos = 0;
		boolean replaced = false;
		while (strPos < strLength) {
			replaced = false;
			for (int i = 0; i < codeTable.length; i++) {
				if (srcStr.startsWith(codeTable[i][0], strPos)) {
					transStrBuff.append(codeTable[i][1]);
					strPos += codeTable[i][0].length();
					replaced = true;
					break;
				}
			}
			if (!replaced) {
				transStrBuff.append(srcStr.charAt(strPos));
				strPos++;
			}
		}

		return transStrBuff.toString();
	}
}