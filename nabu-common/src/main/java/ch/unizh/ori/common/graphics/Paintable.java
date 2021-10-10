package ch.unizh.ori.common.graphics;

import ch.unizh.ori.common.prefs.Preferences;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.Dimension2D;

public interface Paintable {

	Dimension2D size(FontRenderContext paramFontRenderContext);

	void paint(Graphics2D paramGraphics2D);

	void setParam(Object paramObject);

	void setPreferences(Preferences paramPreferences);

}