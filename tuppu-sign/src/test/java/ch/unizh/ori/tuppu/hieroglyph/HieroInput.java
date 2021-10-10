package ch.unizh.ori.tuppu.hieroglyph;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ToolTipManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class HieroInput extends JFrame implements DocumentListener, ActionListener {

	private static final long serialVersionUID = -7941228327326676796L;

	private HieroLabel hieroL = new HieroLabel();
	private JTextField inputTF = new JTextField();

	public static final int SIZE = 46;

	private Map<String, String> classnames = null;
	private List<Code> codes = null;

	public static class InsertAction extends AbstractAction {

		private static final long serialVersionUID = 8899617787423847164L;

		private JTextField field;
		private String str;

		public InsertAction(JTextField field, String str, String name) {
			super(name);
			this.field = field;
			this.str = str;
		}

		public void actionPerformed(ActionEvent e) {
			int offset = 0, len = 0;
			offset = this.field.getSelectionStart();
			len = this.field.getSelectionEnd() - offset;
			try {
				if (len > 0)
					this.field.getDocument().remove(offset, len);
				this.field.getDocument().insertString(offset, this.str, null);
				this.field.requestFocus();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public static class Code {
		public String entree;
		public String phon;
		public String caractere;
		public String symbole;

		public Code(String e, String p, String c, String s) {
			this.entree = e;
			this.phon = p;
			this.caractere = c;
			this.symbole = s;
		}

		public Code(String[] args) {
			int i = 0;
			this.entree = args[i++];
			this.phon = args[i++];
			this.caractere = args[i++];
			this.symbole = args[i++];
		}
	}

	public HieroInput() {
		super("HieroInput");
		getContentPane().setLayout(new BorderLayout());

		JPanel textP = new JPanel(new GridLayout(0, 1));
		textP.add(this.hieroL);
		this.hieroL.setPreferredSize(new Dimension(100, 92));
		textP.add(this.inputTF);
		getContentPane().add(textP, "North");

		getContentPane().add(getTabbedPane(), "Center");

		pack();
		setExtendedState(6);

		this.inputTF.addActionListener(this);
		this.inputTF.getDocument().addDocumentListener(this);
	}

	public JComponent getTabbedPane() {
		JTabbedPane tab = new JTabbedPane(2);
		Map<String, String> map = readClassnames();
		Map<String, JPanel> tabs = new HashMap<String, JPanel>();
		Iterator<String> iter = (new TreeSet<String>(map.keySet())).iterator();
		while (iter.hasNext()) {
			String cl = iter.next();
			String name = map.get(cl);
			JPanel pan = new JPanel(new GridLayout(0, 6));
			tabs.put(cl, pan);
			tab.addTab(cl + ": " + name, new JScrollPane(pan, 22, 31));
		}

		Map<String, JButton> itemMap = new HashMap<String, JButton>();
		List<Code> list = readCodes();
		Iterator<Code> cIter = list.iterator();
		while (iter.hasNext()) {
			Code c = cIter.next();
			String key = c.caractere + c.symbole;

			JButton butt = itemMap.get(key);
			if (butt == null) {

				ActionListener action = new InsertAction(this.inputTF, c.entree, c.entree);
				butt = new JButton();
				itemMap.put(key, butt);
				butt.addActionListener(action);

				butt.setFont((Font) null);
				butt.setText(String.valueOf((char) (Integer.parseInt(c.symbole) + 31)));
				butt.setToolTipText(c.entree);
				tabs.get(c.caractere).add(butt);

				continue;
			}
			butt.setToolTipText(butt.getToolTipText() + ", " + c.entree);
		}

		ToolTipManager.sharedInstance().setInitialDelay(0);
		return tab;
	}

	protected Map<String, String> readClassnames() {
		if (this.classnames != null) {
			return this.classnames;
		}
		String filename = "classnames.txt";
		Map<String, String> map = new HashMap<String, String>();

		try {
			BufferedReader in = new BufferedReader(
					new InputStreamReader(HieroInput.class.getResourceAsStream(filename)));

			char ch = 'A';
			String line;
			while ((line = in.readLine()) != null) {
				if (line.length() >= 1) {
					map.put(String.valueOf(ch), line);
				}
				ch = (char) (ch + 1);
			}
			ch = (char) (ch - 1);
			map.put("Aa", map.remove(String.valueOf(ch)));
			in.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return this.classnames = map;
	}

	protected List<Code> readCodes() {
		if (this.codes != null) {
			return this.codes;
		}
		List<Code> list = new ArrayList<Code>();
		String filename = "seshSource.txt";

		try {
			BufferedReader in = new BufferedReader(
					new InputStreamReader(HieroInput.class.getResourceAsStream(filename)));
			String line;
			while ((line = in.readLine()) != null) {
				String[] fields = line.split("\\s+");
				Code c = new Code(fields);
				list.add(c);
			}
			in.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return this.codes = list;
	}

	public void changedUpdate(DocumentEvent e) {
		updateLabel();
	}

	public void insertUpdate(DocumentEvent e) {
		updateLabel();
	}

	public void removeUpdate(DocumentEvent e) {
		updateLabel();
	}

	public void actionPerformed(ActionEvent e) {
		updateLabel();
	}

	public void updateLabel() {
		String text = this.inputTF.getText();
		this.hieroL.setText(text);
		this.hieroL.repaint();
	}

	public static void main(String[] args) {
		HieroInput hi = new HieroInput();
		hi.setBounds(100, 100, 300, 100);
		hi.setVisible(true);

		hi.setDefaultCloseOperation(3);
	}
}