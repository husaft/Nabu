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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AkkadianHelper extends DefaultDescriptable implements Transliteration {
	private static final long serialVersionUID = -4675315121985525600L;

	private Script script;

	public static String ascii2unicode(String in) {
		char[] arr = in.toCharArray();
		for (int i = 0; i < arr.length; i++) {
			switch (arr[i]) {
			case '.':
				if (i + 1 < arr.length && "st".indexOf(arr[i + 1]) >= 0) {
					arr[i] = arr[i + 1];
					arr[i + 1] = '̣';
				}
				break;

			case '_':
				if (i + 1 < arr.length) {
					if ("aeiou".indexOf(arr[i + 1]) >= 0) {
						arr[i] = arr[i + 1];
						arr[i + 1] = '̄';
						break;
					}
					if (arr[i + 1] == 'h') {
						arr[i] = 'h';
						arr[i + 1] = '̱';
					}
				}
				break;

			case '^':
				if (i + 1 < arr.length) {
					if ("aeiou".indexOf(arr[i + 1]) >= 0) {
						arr[i] = arr[i + 1];
						arr[i + 1] = '̂';
						break;
					}
					if (arr[i + 1] == 's') {
						arr[i] = 's';
						arr[i + 1] = '̌';
					}
				}
				break;
			}

		}
		return new String(arr);
	}

	public static String unicode2ascii(String in) {
		char[] arr = in.toCharArray();
		for (int i = 1; i < arr.length; i++) {
			switch (arr[i]) {
			case '̣':
				if ("st".indexOf(arr[i - 1]) >= 0) {
					arr[i] = arr[i - 1];
					arr[i - 1] = '.';
				}
				break;

			case '̄':
				if ("aeiou".indexOf(arr[i - 1]) >= 0) {
					arr[i] = arr[i - 1];
					arr[i - 1] = '_';
				}
				break;

			case '̠':
				if (arr[i - 1] == 'h') {
					arr[i] = '_';
					arr[i - 1] = 'h';
				}
				break;

			case '̂':
				if ("aeiou".indexOf(arr[i - 1]) >= 0) {
					arr[i] = arr[i - 1];
					arr[i - 1] = '^';
				}
				break;
			case '̌':
				if (arr[i - 1] == 's') {
					arr[i] = 's';
					arr[i - 1] = '^';
				}
				break;
			}

		}
		return new String(arr);
	}

	public static OldText string2Text(String str) {
		return (OldText) new OldStringText(ascii2unicode(str));
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

		private static final long serialVersionUID = -7287150711546803533L;

		private JTextField inTF = new JTextField();
		private JTextField asciiL = new JTextField();
		private JTextField unicodeL = new JTextField();

		public static void main(String[] args) {
			JFrame f = new GuiTest();
			f.setSize(400, 200);
			f.setLocation(20, 50);
			f.setVisible(true);
		}

		public void actionPerformed(ActionEvent e) {
			String in = this.inTF.getText();
			this.asciiL.setText(AkkadianHelper.unicode2ascii(in));
			this.unicodeL.setText(AkkadianHelper.ascii2unicode(in));
		}

		public GuiTest() {
			super("Accadian-Helper");

			JPanel p = new JPanel(new GridLayout(3, 2));
			p.add(new JLabel("Input:"));
			p.add(this.inTF);
			p.add(new JLabel("Unicode:"));
			p.add(this.unicodeL);
			p.add(new JLabel("Ascii"));
			p.add(this.asciiL);
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

	public Object toStandard(Object foreign) {
		return unicode2ascii((String) foreign);
	}

	public Object toForeign(Object standard) {
		return ascii2unicode((String) standard);
	}

	public Script getScript() {
		return this.script;
	}

	public void setScript(Script script) {
		this.script = script;
	}
}