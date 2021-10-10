package ch.unizh.ori.tuppu;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;

public abstract class Box implements Plottable {
	private Dimension d;
	private GraphicsProperties graphicsProperties = new GraphicsProperties();

	public static final boolean DEBUG = false;
	public boolean FRAME = true;

	protected abstract void calcSize(Dimension paramDimension);

	public Dimension getSize() {
		if (this.d == null) {
			this.d = new Dimension();
			calcSize(this.d);
			Insets insets = getGraphicsProperties().getInsets();
			if (insets != null) {
				this.d.width += insets.left + insets.right;
				this.d.height += insets.top + insets.bottom;
			}
		}
		return this.d;
	}

	public void plot(Graphics2D g) {
		Point pen = new Point(0, getSize().height);
		Insets insets = getGraphicsProperties().getInsets();
		if (insets != null) {
			pen.x += insets.left;
			pen.y += insets.top;
		}
		paint(g, pen);
		if (this.FRAME) {
			Dimension s = getSize();
			g.drawRect(0, 0, (int) s.getWidth() - 1, (int) s.getHeight() - 1);
		}
	}

	public abstract void paint(Graphics2D paramGraphics2D, Point paramPoint);

	public GraphicsProperties getGraphicsProperties() {
		return this.graphicsProperties;
	}

	public void setGraphicsProperties(GraphicsProperties graphicsProperties) {
		this.graphicsProperties = graphicsProperties;
	}
}