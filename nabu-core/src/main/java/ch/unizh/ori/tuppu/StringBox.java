package ch.unizh.ori.tuppu;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

public class StringBox extends Box {
	public static boolean VISUAL = true;
	public static boolean PAINT_SHAPE = false;
	public static FontRenderContext defaultFrc = new FontRenderContext(null, true, false);

	private String s;

	protected Font font;

	protected FontRenderContext frc;

	public StringBox(String s, Font font, FontRenderContext frc) {
		this.s = s;
		this.font = font;
		this.frc = frc;
	}

	public void calcSize(Dimension d) {
		Rectangle2D bounds;
		TextLayout tl = new TextLayout(this.s, getFont(), this.frc);

		if (VISUAL) {
			GlyphVector v = getFont().createGlyphVector(this.frc, this.s);

			Shape outline = v.getOutline();
			bounds = outline.getBounds2D();
		} else {
			bounds = tl.getBounds();
		}
		d.setSize(bounds.getWidth(), bounds.getHeight());
	}

	public void paint(Graphics2D g, Point pen) {
		Font theFont = getFont();

		g.setFont(theFont);
		Dimension d = getSize();

		GlyphVector v = theFont.createGlyphVector(g.getFontRenderContext(), this.s);
		Rectangle2D bounds = v.getVisualBounds();
		if (PAINT_SHAPE) {
			g.fill(v.getOutline((pen.x + (d.width - (int) bounds.getWidth()) / 2),
					(pen.y - (d.height - (int) bounds.getHeight()) / 2)));
		} else {

			g.drawString(this.s, 10 + pen.x + (d.width - (int) bounds.getWidth()) / 2,
					pen.y - (d.height - (int) bounds.getHeight()) / 2);
		}

		pen.x += d.width;
		pen.y -= d.height;
	}

	protected Font getFont() {
		if (this.font == null) {
			return getGraphicsProperties().getFont();
		}
		return this.font;
	}
}