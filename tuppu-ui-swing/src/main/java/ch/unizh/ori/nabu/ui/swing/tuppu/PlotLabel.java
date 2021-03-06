package ch.unizh.ori.nabu.ui.swing.tuppu;

import ch.unizh.ori.tuppu.Plottable;
import ch.unizh.ori.tuppu.Plotter;
import ch.unizh.ori.tuppu.StringPlotter;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class PlotLabel extends JPanel {

	private static final long serialVersionUID = -4217636699744612577L;

	private String text;
	private Map<Object, Object> params;
	private Plotter plotter = null;
	private Plottable plottable = null;

	public MyDocumentListener documentListener;

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Dimension s = getSize();
		Dimension d = getPlottable().getSize();
		g2.translate((s.width - d.width) / 2, (s.height - d.height) / 2);
		Plotter.plot(g2, getPlottable());
		g2.drawLine(-10, -10, 10, -10);
	}

	public Dimension getPreferredSize() {
		return getMinimumSize();
	}

	public Dimension getMaximumSize() {
		return getMinimumSize();
	}

	public Dimension getMinimumSize() {
		Dimension dim = getPlottable().getSize();
		return dim;
	}

	public static class MyDocumentListener implements DocumentListener {
		private PlotLabel pl;

		public MyDocumentListener(PlotLabel hl) {
			this.pl = hl;
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
				this.pl.setText(doc.getText(0, doc.getLength()));
			} catch (BadLocationException ex) {
				ex.printStackTrace();
			}
		}
	}

	public PlotLabel() {
		this.documentListener = new MyDocumentListener(this);
	}

	protected void doUpdate() {
		this.plottable = null;
		if (this.plotter != null) {
			try {
				this.plottable = this.plotter.createPlottable(getText(), getParams());
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
			this.plottable.getGraphicsProperties().configure(getParams(), "");
		}
		if (this.plottable == null)
			this.plottable = (Plottable) new Plottable.EmptyPlottable();
		revalidate();
		repaint();
	}

	public Plottable getPlottable() {
		if (this.plottable == null) {
			this.plottable = (Plottable) new Plottable.EmptyPlottable();
		}
		return this.plottable;
	}

	public Plotter getPlotter() {
		return this.plotter;
	}

	public void setPlotter(Plotter plotter) {
		this.plotter = plotter;
		plotter.init();
		doUpdate();
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
		doUpdate();
	}

	public Map<Object, Object> getParams() {
		return this.params;
	}

	public void setParams(Map<Object, Object> params) {
		this.params = params;
		doUpdate();
	}

	public void setAll(Plotter plotter, String text, Map<Object, Object> params) {
		if (plotter != null) {
			this.plotter = plotter;
			plotter.putInitParams(params);
			plotter.init();
		} else {
			this.plotter = (Plotter) new StringPlotter();
		}
		this.text = text;
		this.params = params;
		doUpdate();
	}
}