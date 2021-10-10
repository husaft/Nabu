package ch.unizh.ori.tuppu;

import ch.unizh.ori.nabu.core.DefaultDescriptable;
import java.awt.Dimension;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class Plotter extends DefaultDescriptable {

	private static final long serialVersionUID = -6934890799304792610L;

	private static Map<String, Plotter> plotters = new HashMap<String, Plotter>();

	protected GraphicsProperties graphicsProperties = new GraphicsProperties();

	private Map<Object, Object> initParams = new HashMap<Object, Object>();

	public void init() {
	}

	public void setId(String string) {
		super.setId(string);
		plotters.put(getId(), this);
	}

	public static Plotter getPlotter(String plotterId) {
		return (Plotter) plotters.get(plotterId);
	}

	public abstract Plottable createPlottable(String paramString, Map<Object, Object> paramMap)
			throws IOException, FontFormatException;

	public Collection<String> getCodes() {
		return null;
	}

	public BufferedImage plotToImage(String text, Map<Object, Object> params) throws IOException, FontFormatException {
		Plottable p = createPlottable(text, params);
		Dimension size = p.getSize();
		size.width = Math.max(size.width, 1);
		size.height = Math.max(size.height, 1);
		BufferedImage img = new BufferedImage(size.width, size.height, 1);

		Graphics2D g = img.createGraphics();
		plot(g, p);
		g.dispose();
		return img;
	}

	public static void plot(Graphics2D g, Plottable p) {
		Dimension size = p.getSize();
		GraphicsProperties gp = p.getGraphicsProperties();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (gp != null)
			gp.fillIn(g);
		g.clearRect(0, 0, size.width, size.height);
		p.plot(g);
	}

	public static void paint(Graphics2D g, Box r) {
		paint(g, r, 0, 0);
	}

	public static void paint(Graphics2D g, Box r, int x, int y) {
		Point pen = new Point(x, y + (r.getSize()).height);
		r.paint(g, pen);
	}

	public Object getInitParam(String key) {
		return this.initParams.get(key);
	}

	public Object getInitParam(String key, String defaultValue) {
		Object val = this.initParams.get(key);
		return (val != null && !val.equals("")) ? val : defaultValue;
	}

	public void setInitParam(String key, Object value) {
		this.initParams.put(key, value);
	}

	public void putInitParams(Map<Object, Object> params) {
		this.initParams.putAll(params);
	}
}