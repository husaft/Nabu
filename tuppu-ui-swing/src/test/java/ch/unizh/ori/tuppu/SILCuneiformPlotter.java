package ch.unizh.ori.tuppu;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.font.FontRenderContext;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

public class SILCuneiformPlotter extends Plotter {

	private static final long serialVersionUID = 3666771056287540433L;

	private static final String DEFAULT_NAME_TO_POSITION_FILENAME = "/Nabu/web/WEB-INF/config/SILnames.properties";
	private static final String DEFAULT_FONTBASE_URL = "file://Nabu/web/WEB-INF/fonts/NEOASS.TTF";
	private Map<Object, Object> nameToPosition = new HashMap<Object, Object>();
	private URL fontbase = null;

	private int fontSize;

	private Font font;

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
		this.nameToPosition = new HashMap<Object, Object>(p);
	}

	public void init() {
		super.init();
		loadNameMappings((String) getInitParam("nameToUnicode.filename", DEFAULT_NAME_TO_POSITION_FILENAME));
		try {
			this.fontbase = new URL((String) getInitParam("fontbase.url", DEFAULT_FONTBASE_URL));
			System.out.println(this.fontbase);
		} catch (MalformedURLException ex1) {
			ex1.printStackTrace();
		}
	}

	public SILCuneiformPlotter() {
		this.fontSize = 74;
	}

	private Font getFont() throws FontFormatException, IOException {
		if (this.font == null) {
			this.font = Font.createFont(0, this.fontbase.openStream());
			this.font = this.font.deriveFont(this.fontSize);
		}
		return this.font;
	}

	public Plottable createPlottable(String text, Map<Object, Object> param) throws IOException, FontFormatException {
		VectorBox ret = new VectorBox();
		FontRenderContext frc = new FontRenderContext(null, true, false);

		StringBox.VISUAL = true;

		ret.direction = VectorBox.LEFT_TO_RIGHT;
		StringTokenizer tok = new StringTokenizer(text, " ");
		Font font = getFont();
		while (tok.hasMoreTokens()) {
			String name = tok.nextToken();
			if (this.nameToPosition.containsKey(name)) {
				StringBox stringBox = new StringBox((String) this.nameToPosition.get(name), font, frc);
				ret.add(stringBox);
				ret.add(new StrutBox(new Dimension(20, 0)));
				continue;
			}
			ret.add(new StringBox(name, font, frc));
		}

		ret.getGraphicsProperties().configure(param, "");
		return ret;
	}

	public int getFontSize() {
		return this.fontSize;
	}

	public void setFontSize(int size) {
		this.fontSize = size;
	}
}