package ch.unizh.ori.tuppu.hieroglyph;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class HieroglyphicSigns {
	private static HieroglyphicSigns _default = null;
	private URL base;

	public static HieroglyphicSigns getDefault() {
		if (_default == null) {
			_default = new HieroglyphicSigns();
		}
		return _default;
	}

	private Map<String, Donne> donnes;
	private Map<String, List<Donne>> phons;
	private Map<String, List<Donne>> codes;
	private Map<String, List<Donne>> classes;

	public HieroglyphicSigns() {
		this("/seshSource.txt");
	}

	public HieroglyphicSigns(String filename) {
		this(find(filename));
	}

	public HieroglyphicSigns(URL filename) {
		this.base = filename;
		readEntries(filename);
	}

	@SuppressWarnings("deprecation")
	public static URL find(String name) {
		URL ret = null;
		ret = HieroglyphicSigns.class.getResource(name);
		if (ret == null) {
			File f = new File(name);
			if (f.exists() && f.canRead()) {
				try {
					ret = f.toURL();
				} catch (MalformedURLException ex) {
					ex.printStackTrace();
				}
			}
		}
		return ret;
	}

	public static class Donne {
		public String entry;
		public String phone;
		public String font;
		public char ch;

		public String getGardiner() {
			return this.font + (this.ch - 31);
		}

		public String toString() {
			return getGardiner() + " " + this.entry + " " + this.phone;
		}
	}

	public Map<String, Donne> getDonnes() {
		return this.donnes;
	}

	public Donne getDonne(String e) {
		return getDonnes().get(e);
	}

	public List<Donne> getPhon(String phon) {
		return this.phons.get(phon);
	}

	private List<Donne> addPhon(String phon, Donne d) {
		List<Donne> l = getPhon(phon);
		if (l == null) {
			l = new ArrayList<Donne>();
			this.phons.put(phon, l);
		}
		l.add(d);
		return l;
	}

	public List<Donne> getCodes(Donne d) {
		return getCodes(d.getGardiner());
	}

	public List<Donne> getCodes(String gard) {
		return this.codes.get(gard);
	}

	private List<Donne> addCode(Donne d) {
		List<Donne> l = getCodes(d);
		if (l == null) {
			l = new ArrayList<Donne>();
			this.codes.put(d.getGardiner(), l);
		}
		l.add(d);
		return l;
	}

	public List<Donne> getClasses(Donne d) {
		return getClasses(d.font);
	}

	public List<Donne> getClasses(String s) {
		return this.classes.get(s);
	}

	private void addClasses(Donne d) {
		List<Donne> l = getClasses(d);
		if (l == null) {
			l = new ArrayList<Donne>();
			this.classes.put(d.font, l);
			l.add(d);
		} else {
			Donne last = l.get(l.size() - 1);
			if (last.ch == d.ch) {
				l.set(l.size() - 1, d);
			} else {
				l.add(d);
			}
		}
	}

	private void readEntries(URL filename) {
		this.donnes = new HashMap<String, Donne>();
		this.phons = new HashMap<String, List<Donne>>();
		this.codes = new HashMap<String, List<Donne>>();
		this.classes = new HashMap<String, List<Donne>>();
		InputStream in = null;

		try {
			in = filename.openStream();
			BufferedReader r = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = r.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, "\t");
				Donne d = new Donne();
				d.entry = nextToken(st).trim();
				d.phone = nextToken(st);
				d.font = nextToken(st);
				try {
					d.ch = (char) (Integer.parseInt(nextToken(st)) + 31);
				} catch (NumberFormatException ex) {
				}
				if (d.entry != null) {
					this.donnes.put(d.entry, d);
					if (d.phone != null) {
						addThePhons(d);
						addCode(d);
						addClasses(d);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception ex) {
				}
			}
		}
	}

	private void addThePhons(Donne d) {
		StringTokenizer st = new StringTokenizer(d.phone, "|");
		while (st.hasMoreElements()) {
			String p = st.nextToken();
			addPhon(p, d);
		}
	}

	private static String nextToken(StringTokenizer st) {
		if (!st.hasMoreTokens())
			return null;
		return st.nextToken();
	}

	public URL getBase() {
		return this.base;
	}
}