package ch.unizh.ori.tuppu.hieroglyph;

import ch.unizh.ori.tuppu.Box;
import ch.unizh.ori.tuppu.Plotter;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

public class HieroLabel extends JPanel {

	private static final long serialVersionUID = -979263751692488450L;

	private String text;
	private HieroPlotter plotter = null;
	private Box renderer = null;

	private int fontSize = 20;
	public MyDocumentListener documentListener;

	private HieroPlotter getPlotter(Graphics2D fontGraphics) {
		if (this.plotter == null) {
			this.plotter = new HieroPlotter(fontGraphics, getFontSize());
		}
		return this.plotter;
	}

	private Box getRenderer(Graphics2D fontGraphics) {
		if (this.renderer == null && getText() != null) {
			this.renderer = getPlotter(fontGraphics).constructBox(fontGraphics, getText(), null, null);
		}
		return this.renderer;
	}

	private void initComponents() {
		setLayout(new BorderLayout());
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (getText() == null)
			return;
		Plotter.paint(g2, getRenderer(g2));
	}

	public Dimension getPreferredSize() {
		return getMinimumSize();
	}

	public Dimension getMaximumSize() {
		return getMinimumSize();
	}

	public Dimension getMinimumSize() {
		if (getText() == null) {
			return new Dimension(1, 1);
		}
		Graphics2D g2 = (Graphics2D) getGraphics();
		Dimension dim = getRenderer(g2).getSize();
		g2.dispose();
		return dim;
	}

	public static class MyDocumentListener implements DocumentListener {
		private HieroLabel hl;

		public MyDocumentListener(HieroLabel hl) {
			this.hl = hl;
		}

		public void changedUpdate(DocumentEvent documentEvent) {
			change(documentEvent);
		}

		public void insertUpdate(DocumentEvent documentEvent) {
			change(documentEvent);
		}

		public void removeUpdate(DocumentEvent documentEvent) {
			change(documentEvent);
		}

		public void change(DocumentEvent documentEvent) {
			Document doc = documentEvent.getDocument();
			try {
				this.hl.setText(doc.getText(0, doc.getLength()));
			} catch (BadLocationException ex) {
				ex.printStackTrace();
			}
		}
	}

	public HieroLabel() {
		this.documentListener = new MyDocumentListener(this);
		initComponents();
	}

	public void addTextField(JTextComponent tc) {
		tc.getDocument().addDocumentListener(this.documentListener);
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		if (text == null || !text.equals(this.text)) {
			Graphics2D g2 = (Graphics2D) getGraphics();
			if (g2 != null) {
				this.renderer = getPlotter(g2).constructBox(g2, text);
			}
		}
		this.text = text;
		repaint();
	}

	public int getFontSize() {
		return this.fontSize;
	}

	public void setFontSize(int fontSize) {
		if (fontSize != this.fontSize) {
			this.plotter = null;
			this.renderer = null;
		}
		this.fontSize = fontSize;
	}

	public static void main(String[] args) {
		String code = "sDm-n:f";
		if (args.length == 1) {
			code = args[0];
		}
		JFrame f = new JFrame("Hiero: " + code);
		final HieroLabel l = new HieroLabel();
		final JTextField tf = new JTextField(code);
		f.getContentPane().add(l);
		f.getContentPane().add(tf, "North");
		f.setSize(400, 400);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		f.setLocation((screenSize.width - 400) / 2, (screenSize.height - 400) / 2);
		f.setDefaultCloseOperation(3);

		tf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				l.setText(tf.getText());
			}
		});

		f.setVisible(true);

		l.setBackground(Color.white);
		l.setFontSize(40);
		l.setText(code);
	}
}