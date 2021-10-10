package ch.unizh.ori.tuppu.hieroglyph;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FontMapper {

	private static final String FONT_BASE = "HieroFonte";
	private URL base;
	private Map<String, Font> baseFonts = new HashMap<String, Font>();
	public final float SUB_SIZE = 0.5F;
	private int size = 30;
	private FontRenderContext frc;
	private Map<String, Font> fonts = new HashMap<String, Font>();

	public FontMapper(FontRenderContext frc, int size, boolean isSub, URL base) {
		this.frc = frc;
		this.base = base;
		if (isSub) {
			this.size = (int) (size * 0.5F);
		} else {
			this.size = size;
		}
	}

	public Font getBaseFont(String f) {
		Font baseFont = this.baseFonts.get(f);
		if (baseFont == null) {
			InputStream in = null;
			try {
				URL url = new URL(this.base, "hieroFontes/" + FONT_BASE + f + ".ttf");

				in = new BufferedInputStream(url.openStream());
				baseFont = Font.createFont(0, in);
			} catch (Exception ex) {
				ex.printStackTrace();
				baseFont = new Font(FONT_BASE + f, 0, 1);
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (Exception ex) {
					}
				}
			}
			this.baseFonts.put(f, baseFont);
		}
		return baseFont;
	}

	private void initFont(String f) {
		Font font = getBaseFont(f).deriveFont(this.size);
		this.fonts.put(f, font);
	}

	public Font getFont(HieroglyphicSigns.Donne d) {
		return getFont(d.font);
	}

	public Font getFont(String f) {
		Font font = this.fonts.get(f);
		if (font == null) {
			initFont(f);
			font = this.fonts.get(f);
		}
		return font;
	}

	public FontRenderContext getFrc() {
		return this.frc;
	}

	public void setFrc(FontRenderContext frc) {
		this.frc = frc;
	}
}