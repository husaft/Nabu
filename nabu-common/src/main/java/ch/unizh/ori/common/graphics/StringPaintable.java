package ch.unizh.ori.common.graphics;

import ch.unizh.ori.common.prefs.Preferences;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Dimension2D;

public class StringPaintable implements Paintable {
	private String param;

	@SuppressWarnings("unused")
	private Preferences prefs;

	public Dimension2D size(FontRenderContext frc) {
		return null;
	}

	public void setPreferences(Preferences prefs) {
		this.prefs = prefs;
	}

	public void paint(Graphics2D g) {
		g.drawString(this.param, 0, 0);
	}

	public void setParam(Object param) {
		this.param = (String) param;
	}
}