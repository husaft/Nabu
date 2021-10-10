package ch.unizh.ori.tuppu.hieroglyph;

import ch.unizh.ori.tuppu.Box;
import ch.unizh.ori.tuppu.GraphicsProperties;
import ch.unizh.ori.tuppu.Plottable;
import ch.unizh.ori.tuppu.Plotter;
import ch.unizh.ori.tuppu.StringBox;
import ch.unizh.ori.tuppu.VectorBox;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.util.Map;
import java.util.StringTokenizer;

public class HieroPlotter extends Plotter {

	private static final long serialVersionUID = -4880045426775206173L;

	static final double BLA = 1.3D;
	public static final String PREFIX = "hiero.";
	public static final String SMALL = "hiero.small.";
	private int size = 20;

	private HieroglyphicSigns hgSigns;

	private FontMapper fontMapper;

	private FontMapper subFontMapper;

	public HieroPlotter() {
	}

	public HieroPlotter(Graphics2D fontGraphics, int size) {
		this.size = size;
	}

	public void init() {
		super.init();

		String base = (String) getInitParam("hiero.base");
		if (base != null) {
			this.hgSigns = new HieroglyphicSigns(base);
		} else {
			this.hgSigns = HieroglyphicSigns.getDefault();
		}
		Object size = getInitParam("hiero.size");
		if (size != null)
			try {
				this.size = Integer.parseInt((String) size);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		FontRenderContext frc = new FontRenderContext(null, true, false);
		initMappers(this.size, frc);
	}

	private void initMappers(int size, FontRenderContext frc) {
		this.fontMapper = new FontMapper(frc, size, false, this.hgSigns.getBase());
		this.subFontMapper = new FontMapper(frc, size, true, this.hgSigns.getBase());
	}

	public Plottable createPlottable(String text, Map<Object, Object> param) {
		Font subNormalFont, normalFont = GraphicsProperties.configureFont(param, "hiero.");

		if (param.containsKey("hiero.small.fontname")) {
			subNormalFont = GraphicsProperties.configureFont(param, "hiero.small.");
		} else {
			this.subFontMapper.getClass();
			subNormalFont = normalFont.deriveFont(normalFont.getSize() * 0.5F);
		}
		Box ret = constructBox(text, normalFont, subNormalFont);
		ret.getGraphicsProperties().configure(param, "");
		return (Plottable) ret;
	}

	public Box constructBox(Graphics2D g2, String code) {
		return constructBox(g2, code, (Font) null, (Font) null);
	}

	public Box constructBox(Graphics2D g2, String code, Font normalFont, Font subNormalFont) {
		if (normalFont == null) {
			normalFont = g2.getFont();
		}
		if (subNormalFont == null) {
			this.subFontMapper.getClass();
			subNormalFont = normalFont.deriveFont(normalFont.getSize() * 0.5F);
		}
		return constructBox(code, normalFont, subNormalFont);
	}

	public Box constructBox(String code, Font normalFont, Font subNormalFont) {
		VectorBox ret = new VectorBox();
		ret.direction = VectorBox.LEFT_TO_RIGHT;

		StringTokenizer caratST = new StringTokenizer(code, "-");
		while (caratST.hasMoreTokens()) {
			String carat = caratST.nextToken();
			boolean lonely = (carat.indexOf(':') < 0);
			if (lonely) {
				HieroglyphicSigns.Donne d = this.hgSigns.getDonne(carat.trim());
				if (d == null) {
					ret.add((Box) new StringBox(carat, normalFont, this.fontMapper.getFrc()));
					continue;
				}
				ret.add((Box) new HieroBox(d, this.fontMapper));
				continue;
			}
			VectorBox column = new VectorBox();
			column.direction = VectorBox.TOP_TO_DOWN;
			ret.add((Box) column);

			StringTokenizer subRowST = new StringTokenizer(carat, ":");
			while (subRowST.hasMoreTokens()) {
				String subRow = subRowST.nextToken();

				VectorBox row = new VectorBox();
				row.direction = VectorBox.LEFT_TO_RIGHT;
				column.add((Box) row);

				StringTokenizer subST = new StringTokenizer(subRow, "*");
				while (subST.hasMoreTokens()) {
					String sub = subST.nextToken();

					HieroglyphicSigns.Donne d = this.hgSigns.getDonne(sub.trim());
					if (d == null) {
						row.add((Box) new StringBox(sub, subNormalFont, this.subFontMapper.getFrc()));
						continue;
					}
					row.add((Box) new HieroBox(d, this.subFontMapper));
				}
			}
		}

		return (Box) ret;
	}

	public static class HieroBox extends StringBox {
		public HieroBox(HieroglyphicSigns.Donne d, FontMapper fm) {
			super(String.valueOf(d.ch), fm.getFont(d.font), fm.getFrc());
		}

		public void calcSize(Dimension d) {
			super.calcSize(d);
			d.height = (int) (d.height * 1.3D);
		}
	}
}