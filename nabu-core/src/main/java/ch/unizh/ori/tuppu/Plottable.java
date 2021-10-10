package ch.unizh.ori.tuppu;

import java.awt.Dimension;
import java.awt.Graphics2D;

public interface Plottable {
	Dimension getSize();

	void plot(Graphics2D paramGraphics2D);

	GraphicsProperties getGraphicsProperties();

	void setGraphicsProperties(GraphicsProperties paramGraphicsProperties);

	public static class EmptyPlottable implements Plottable {
		public Dimension getSize() {
			return new Dimension(0, 0);
		}

		public void plot(Graphics2D g) {
		}

		public void setGraphicsProperties(GraphicsProperties graphicsProperties) {
		}

		public GraphicsProperties getGraphicsProperties() {
			return null;
		}
	}
}