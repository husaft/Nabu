package ch.unizh.ori.tuppu;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.Map;

public class StringPlotter extends Plotter {

	private static final long serialVersionUID = -4177271750066808025L;

	private String fontname = "Scheherazade";

	private int fontsize = 80;

	public Plottable createPlottable(String text, Map<Object,Object> param) {
		FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, false);

		Font font = null;
		if (this.fontname != null) {
			font = new Font(this.fontname, 0, this.fontsize);
		}
		text = "اليعسه ع عشس";
		StringBox sb = new StringBox(text, font, frc);
		return sb;
	}

	public String getFontname() {
		return this.fontname;
	}

	public void setFontname(String fontname) {
		this.fontname = fontname;
	}
}