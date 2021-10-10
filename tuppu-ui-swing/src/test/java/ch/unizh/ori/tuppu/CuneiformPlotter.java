package ch.unizh.ori.tuppu;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.font.FontRenderContext;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

public class CuneiformPlotter extends Plotter {

	private static final long serialVersionUID = -6812610750384791L;

	private static final String DEFAULT_NAME_TO_POSITION_FILENAME = "/Nabu/resources/cuneiform/akkadian.properties";
	private static final String DEFAULT_FONTBASE_URL = "file://TeX/naaktds/fonts/type1/public/cuneiform/naakaa.pfb";
	private Map<Object, Object> nameToPosition = new HashMap<Object, Object>();
	private URL fontbase = null;

	public void setTestFiles() {
		loadNameMappings(DEFAULT_NAME_TO_POSITION_FILENAME);
		try {
			this.fontbase = new URL(DEFAULT_FONTBASE_URL);
		} catch (MalformedURLException ex1) {
			ex1.printStackTrace();
		}
	}

	private void loadNameMappings(String filename) {
		Properties p = new Properties();
		try {
			p.load(new BufferedInputStream(new FileInputStream(filename)));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		for (Iterator<Object> iter = p.keySet().iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			this.nameToPosition.put(key, new Position(p.getProperty(key)));
		}
	}

	public void init() {
		super.init();
		loadNameMappings((String) getInitParam("nameToPosition.filename", DEFAULT_NAME_TO_POSITION_FILENAME));
		try {
			this.fontbase = new URL((String) getInitParam("fontbase.url", DEFAULT_FONTBASE_URL));
		} catch (MalformedURLException ex1) {
			ex1.printStackTrace();
		}
	}

	private char fontstyle = 'a';
	public static final int A = 97;
	private int size = 100;

	public Plottable createPlottable(String text, Map<Object, Object> param) throws IOException, FontFormatException {
		VectorBox ret = new VectorBox();
		FontRenderContext frc = new FontRenderContext(null, true, false);

		StringBox.VISUAL = true;

		ret.direction = VectorBox.LEFT_TO_RIGHT;
		StringTokenizer tok = new StringTokenizer(text, " ");
		Font[] fonts = fonts(this.fontstyle, this.size);
		while (tok.hasMoreTokens()) {
			String name = tok.nextToken();
			if (this.nameToPosition.containsKey(name)) {
				Position position = (Position) this.nameToPosition.get(name);
				StringBox stringBox = new StringBox(String.valueOf((char) position.ch), fonts[position.font], frc);
				ret.add(stringBox);
				continue;
			}
			Position d = new Position(name);
			ret.add(new StringBox(String.valueOf((char) d.ch), fonts[d.font], frc));
		}

		ret.getGraphicsProperties().configure(param, "");
		return ret;
	}

	protected Font[] fonts(char fontstyle, int size) throws IOException, FontFormatException {
		Font[] ret = new Font[3];
		for (int i = 0; i < ret.length; i++) {

			URL url = new URL(this.fontbase, "naak" + fontstyle + (char) (97 + i) + ".pfb");
			Font font = Font.createFont(1, new BufferedInputStream(url.openStream()));
			ret[i] = font.deriveFont(size);
		}
		return ret;
	}

	public static class Position {
		public int font;
		public int ch;

		public Position() {
		}

		public Position(String s) {
			this.font = s.charAt(0) - 97;
			this.ch = Integer.parseInt(s.substring(1));
		}
	}
}