package ch.unizh.ori.tuppu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.Map;
import java.util.StringTokenizer;

public class GraphicsProperties implements Cloneable {
	private Color background = Color.white;

	private Color color = Color.black;

	private Font font;

	private AffineTransform transform;

	private boolean antialias = false;

	private Insets insets = null;

	public static final String BACKGROUND = "background";

	public static final String COLOR = "color";

	public static final String FONT = "font";

	public static final String FONT_NAME = "fontname";

	public static final String FONT_SIZE = "fontsize";

	public static final String ANTIALIAS = "antialias";

	public static final String SCALE = "scale";

	public static final String INSETS = "insets";

	public GraphicsProperties() {
	}

	public GraphicsProperties(Graphics2D g2) {
		override(g2);
	}

	public void copy(Graphics2D g2) {
		if (this.background == null)
			this.background = g2.getBackground();
		if (this.color == null)
			this.color = g2.getColor();
		if (this.font == null)
			this.font = g2.getFont();
		this.antialias = g2.getFontRenderContext().isAntiAliased();
		this.transform = g2.getFontRenderContext().getTransform();
	}

	public void copyFrom(GraphicsProperties gp) {
		this.background = gp.getBackground();
		this.color = gp.getColor();
		this.font = gp.getFont();
		this.antialias = gp.isAntialias();
		this.transform = gp.getTransform();
		this.insets = gp.getInsets();
	}

	public void clear() {
		this.background = null;
		this.color = null;
		this.font = null;
		this.antialias = false;
		this.insets = null;
		this.transform = null;
	}

	public void override(Graphics2D g2) {
		clear();
		if (g2 != null)
			copy(g2);
	}

	public void fillIn(Graphics2D g2) {
		if (this.background != null)
			g2.setBackground(this.background);
		if (this.color != null)
			g2.setColor(this.color);
		if (this.font != null)
			g2.setFont(this.font);
		if (this.transform != null)
			g2.transform(this.transform);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				this.antialias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
	}

	public void configure(Map<Object, Object> m, String prefix) {
		Object v = m.get(prefix + "background");
		if (v != null) {
			this.background = Color.decode((String) v);
		}
		v = m.get(prefix + "color");
		if (v != null) {
			this.color = Color.decode((String) v);
		}
		this.font = configureFont(m, prefix);

		v = m.get(prefix + "antialias");
		if (v != null) {
			this.antialias = ("true".equalsIgnoreCase((String) v) || "yes".equalsIgnoreCase((String) v)
					|| "on".equalsIgnoreCase((String) v));
		}

		v = m.get(prefix + "scale");
		if (v != null) {
			double d = Double.parseDouble((String) v);
			this.transform = AffineTransform.getScaleInstance(d, d);
		}

		v = m.get(prefix + "insets");
		if (v != null) {
			try {
				StringTokenizer tok = new StringTokenizer((String) v, ", ");
				this.insets = new Insets(Integer.parseInt(tok.nextToken()), Integer.parseInt(tok.nextToken()),
						Integer.parseInt(tok.nextToken()), Integer.parseInt(tok.nextToken()));

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static Font configureFont(Map<Object, Object> m, String prefix) {
		Font ret;
		Object v = m.get(prefix + "font");
		if (v != null) {
			ret = (Font) v;
		} else {
			String name = "Dialog";
			int size = 14;

			v = m.get(prefix + "fontname");
			if (v != null)
				name = (String) v;
			v = m.get(prefix + "fontsize");
			if (v != null)
				try {
					size = Integer.parseInt((String) v);
				} catch (Exception e) {
				}
			ret = new Font(name, 0, size);
		}
		return ret;
	}

	public FontRenderContext getFrc() {
		return new FontRenderContext(this.transform, this.antialias, false);
	}

	public boolean isAntialias() {
		return this.antialias;
	}

	public void setAntialias(boolean antialias) {
		this.antialias = antialias;
	}

	public Color getBackground() {
		return this.background;
	}

	public void setBackground(Color background) {
		this.background = background;
	}

	public Color getColor() {
		return this.color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Font getFont() {
		return this.font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public Insets getInsets() {
		return this.insets;
	}

	public void setInsets(Insets insets) {
		this.insets = insets;
	}

	public AffineTransform getTransform() {
		return this.transform;
	}

	public void setTransform(AffineTransform transform) {
		this.transform = transform;
	}
}