package ch.unizh.ori.common.text.helper;

import ch.unizh.ori.common.text.OldStringText;
import ch.unizh.ori.common.text.OldText;
import ch.unizh.ori.common.text.Script;
import ch.unizh.ori.common.text.Transliteration;
import ch.unizh.ori.nabu.core.DefaultDescriptable;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings({ "deprecation", "unchecked" })
public class SanskritHelper extends DefaultDescriptable implements Transliteration {

	private static final long serialVersionUID = 4267774004767190603L;

	private static final Object LOCK = new Object();

	@SuppressWarnings("unused")
	private static final int DEVA_BASE = 2304;

	@SuppressWarnings("unused")
	private static final char VIRAMA = '्';

	private static final String[] ASCII = new String[] { null, "~", "M", "H", null, "a", "aa", "i", "ii", "u", "uu",
			"R", "L", "e?", "e??", "e", "ai", "o?", "o??", "o", "au", "k", "kh", "g", "gh", "~n", "c", "ch", "j", "jh",
			"~N", "T", "Th", "D", "Dh", "N", "t", "th", "d", "dh", "n", "n?", "p", "ph", "b", "bh", "m", "y", "r", "rr",
			"l", "ll", "lll", "v", "sh", "S", "s", "h", null, null, ".", "'" };

	public static final String[] TR = new String[] { null, "~", "ṃ", "ḥ", null, "a", "ā", "i", "ī", "u", "ū", "ṛ",
			"ḷ", "e?", "e??", "e", "ai", "o?", "o??", "o", "au", "k", "kh", "g", "gh", "ṅ", "c", "ch", "j", "jh",
			"ñ", "ṭ", "ṭh", "ḍ", "ḍh", "ṇ", "t", "th", "d", "dh", "n", "n?", "p", "ph", "b", "bh", "m", "y", "r",
			"rr", "l", "ḷ", "lll", "v", "ś", "ṣ", "s", "h", null, null, ".", "'" };

	public static final String[] TZ = new String[] { null, ":m", ".m", ".h", null, "a", "-a", "i", "-i", "u", "-u",
			".r", ".l", "e?", "e??", "e", "ai", "o?", "o??", "o", "au", "k", "kh", "g", "gh", "~n", "c", "ch", "j",
			"jh", "~N", ".t", ".th", ".d", ".dh", ".n", "t", "th", "d", "dh", "n", "n?", "p", "ph", "b", "bh", "m", "y",
			"r", "rr", "l", "ll", "lll", "v", "/s", ".s", "s", "h", null, null, ".", "'", null, null, null, null, null,
			null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
			null, null, null, "q", "qh", "x", "z", ":r", ":rh", "f", "yy" };

	private String[] lookup;

	@SuppressWarnings("rawtypes")
	private Map inverse;

	public SanskritHelper() {
	}

	public SanskritHelper(String id, String name) {
		super(id, name);
	}

	private SanskritHelper(String[] lookup) {
		this.lookup = lookup;
	}

	@SuppressWarnings({ "rawtypes" })
	private Map getInverseMap() {
		if (this.inverse == null) {
			synchronized (LOCK) {
				if (this.inverse != null)
					return this.inverse;
				this.inverse = new HashMap<Object, Object>(this.lookup.length);
				for (int i = 0; i < this.lookup.length; i++) {
					char c = (char) (2304 + i);
					if (this.lookup[i] != null) {
						this.inverse.put(this.lookup[i], new Character(c));
					}
				}
			}
		}
		return this.inverse;
	}

	@SuppressWarnings("rawtypes")
	private char getInverse(String str, int start) {
		Map ai = getInverseMap();
		int len = 1;
		char c = Character.MIN_VALUE;
		while (start + len <= str.length()) {
			Object o = ai.get(str.substring(start, start + len));
			if (o != null) {
				c = ((Character) o).charValue();
				len++;
			}
		}

		return c;
	}

	private int getLength(char c) {
		int i = c - 2304;
		if (0 <= i && i < this.lookup.length) {
			String str = this.lookup[i];
			return (str == null) ? 0 : str.length();
		}
		return 0;
	}

	public static boolean isConsonant(char ch) {
		return (('क' <= ch && ch <= 'ह') || ('क़' <= ch && ch <= 'य़'));
	}

	public static boolean isVocal(char ch) {
		return ('अ' <= ch && ch <= 'औ');
	}

	public void initTables() {
		getInverseMap();
	}

	public String toUnicode(String in) {
		StringBuffer out = new StringBuffer(in.length());
		boolean wasCons = false;
		for (int i = 0; i < in.length(); i++) {
			char ch = getInverse(in, i);
			if (ch == '\000') {
				out.append(in.charAt(i));
				wasCons = false;
			} else {

				int len = getLength(ch);
				if (len == 0) {
					System.err.println("SanskritHelper: len=0 !");
					len = 1;
				}

				if (isConsonant(ch)) {
					wasCons = true;
					out.append(ch);
					char next = getInverse(in, i + len);
					if (next == 'अ') {
						len++;
						wasCons = false;
					} else if (!isVocal(next)) {
						out.append('्');
					}
				} else if (isVocal(ch)) {
					if (wasCons) {
						ch = (char) (ch + 56);
					}
					out.append(ch);
					wasCons = false;
				} else {
					wasCons = false;
					out.append(ch);
				}
				i += len - 1;
			}
		}
		return out.toString();
	}

	public String fromUnicode(String in) {
		StringBuffer out = new StringBuffer(in.length());
		for (int i = 0; i < in.length(); i++) {
			char ch = in.charAt(i);
			if (ch != '्') {

				if (isVocal((char) (ch - 56))) {
					ch = (char) (ch - 56);
				}
				int ch_i = ch - 2304;
				if (0 > ch_i || ch_i > this.lookup.length) {
					out.append(ch);
				} else {

					String s = this.lookup[ch_i];
					out.append(s);
					if (isConsonant(ch))
						if (i == in.length() - 1) {
							out.append(this.lookup[5]);
						} else {

							char next = in.charAt(i + 1);
							if (next != '्' && !isVocal((char) (next - 56)))
								out.append(this.lookup[5]);
						}
				}
			}
		}
		return out.toString();
	}

	public void setId(String string) {
		super.setId(string);
		if ("ascii".equals(string)) {
			this.lookup = ASCII;
		} else if ("pretty".equals(string)) {
			this.lookup = TR;
		} else if ("tz".equals(string)) {
			this.lookup = TZ;
		} else {
			throw new IllegalArgumentException("Unknown transliteration: " + string);
		}
	}

	public Object toStandard(Object foreign) {
		return toUnicode((String) foreign);
	}

	public Object toForeign(Object standard) {
		return fromUnicode((String) standard);
	}

	private static SanskritHelper asciiHelper = new SanskritHelper(ASCII);
	private static SanskritHelper translitHelper = new SanskritHelper(TR);
	private static SanskritHelper tzHelper = new SanskritHelper(TZ);

	static {
		tzHelper.getInverseMap().put("-", new Character('-'));
		tzHelper.getInverseMap().put("/", new Character('/'));
		tzHelper.getInverseMap().put(".", new Character('.'));
		tzHelper.getInverseMap().put(":", new Character(':'));
		tzHelper.getInverseMap().put(";", new Character(';'));
		tzHelper.getInverseMap().put("?", new Character('?'));
	}
	private Script script;

	public static String ascii2unicode(String in) {
		return asciiHelper.toUnicode(in);
	}

	public static String unicode2ascii(String in) {
		return asciiHelper.fromUnicode(in);
	}

	public static String translit2unicode(String in) {
		return translitHelper.toUnicode(in);
	}

	public static String unicode2translit(String in) {
		return translitHelper.fromUnicode(in);
	}

	public static String tz2unicode(String in) {
		String ret = tzHelper.toUnicode(in);
		if (ret != null) {
			ret = ret.replaceAll("\\:इ", "इ");
			ret = ret.replaceAll("\\:ई", "ई");
			ret = ret.replaceAll("\\:उ", "उ");
			ret = ret.replaceAll("\\:ऊ", "ऊ");
		}
		return ret;
	}

	public static String unicode2tz(String in) {
		return tzHelper.fromUnicode(in);
	}

	public static OldText string2Text(String str) {
		String uni = ascii2unicode(str);
		return (OldText) new OldStringText(uni + " (" + str + ", " + unicode2translit(uni) + ")");
	}

	public static void main(String[] args) {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			String line;
			while ((line = in.readLine()) != null) {
				String l2 = ascii2unicode(line);
				System.out.println(l2);
				String l3 = unicode2ascii(l2);
				System.out.println(l3);
				System.out.println(l3.equals(line));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static class GuiTest extends JFrame implements ActionListener {

		private static final long serialVersionUID = 3309698163698726239L;

		private JTextField inTF = new JTextField();
		private JTextField asciiCodeL = new JTextField();
		private JTextField asciiUnicodeL = new JTextField();
		private JTextField translitCodeL = new JTextField();
		private JTextField transalitUnicodeL = new JTextField();
		private JTextField tzCodeL = new JTextField();
		private JTextField tzUnicodeL = new JTextField();

		public static void main(String[] args) {
			JFrame f = new GuiTest();
			f.setSize(400, 200);
			f.setLocation(20, 50);
			f.setVisible(true);
		}

		public void actionPerformed(ActionEvent e) {
			String in = this.inTF.getText();
			this.asciiCodeL.setText(SanskritHelper.unicode2ascii(in));
			this.asciiUnicodeL.setText(SanskritHelper.ascii2unicode(in));
			this.translitCodeL.setText(SanskritHelper.unicode2translit(in));
			this.transalitUnicodeL.setText(SanskritHelper.translit2unicode(in));
			this.tzCodeL.setText(SanskritHelper.unicode2tz(in));
			this.tzUnicodeL.setText(SanskritHelper.tz2unicode(in));
		}

		public GuiTest() {
			super("Indian-Helper");

			JPanel p = new JPanel(new GridLayout(0, 3));
			p.add(new JLabel("Input:"));
			p.add(this.inTF);
			p.add(new JPanel());

			p.add(new JLabel("Ascii:"));
			p.add(this.asciiCodeL);
			p.add(this.asciiUnicodeL);
			p.add(new JLabel("Translit:"));
			p.add(this.translitCodeL);
			p.add(this.transalitUnicodeL);
			p.add(new JLabel("TZ:"));
			p.add(this.tzCodeL);
			p.add(this.tzUnicodeL);
			getContentPane().add(p, "Center");

			p = new JPanel();
			JButton b = new JButton("Convert");
			p.add(b);
			getContentPane().add(p, "South");

			this.inTF.addActionListener(this);
			b.addActionListener(this);

			setDefaultCloseOperation(3);
		}
	}

	public Script getScript() {
		return this.script;
	}

	public void setScript(Script script) {
		this.script = script;
	}
}