package ch.unizh.ori.nabu.core;

import ch.unizh.ori.common.text.DefaultScript;
import ch.unizh.ori.common.text.PlotterPresentation;
import ch.unizh.ori.common.text.Script;
import ch.unizh.ori.common.text.StringPresentation;
import ch.unizh.ori.nabu.voc.EmptyLineSource;
import ch.unizh.ori.nabu.voc.Filter;
import ch.unizh.ori.nabu.voc.ImgColumn;
import ch.unizh.ori.nabu.voc.Mode;
import ch.unizh.ori.nabu.voc.PrefixSotm;
import ch.unizh.ori.nabu.voc.SndColumn;
import ch.unizh.ori.nabu.voc.Sotm;
import ch.unizh.ori.nabu.voc.StringColumn;
import ch.unizh.ori.nabu.voc.Vocabulary;
import ch.unizh.ori.nabu.voc.Voice;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.digester.Digester;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Central extends DefaultDescriptable implements Serializable {

	private static final long serialVersionUID = -6317836176737886566L;

	public Map<String, Vocabulary> vocs = new TreeMap<String, Vocabulary>();
	public Map<String, Script> scripts = new TreeMap<String, Script>();
	public Map<Object, Object> preferences = new TreeMap<Object, Object>();

	public Map<String, Sotm> sotm = new TreeMap<String, Sotm>();
	private transient Digester digester;
	private URL readerUrl;

	public Central() {
		clear();
	}

	public void clear() {
		this.vocs = new TreeMap<String, Vocabulary>();
		this.scripts = new TreeMap<String, Script>();
		this.preferences = new TreeMap<Object, Object>();
		this.sotm = new TreeMap<String, Sotm>();
	}

	public static class TestSotm extends DefaultDescriptable implements Sotm {

		private static final long serialVersionUID = -1800291359554867092L;

		private List<Voice> list;

		public TestSotm(String id) {
			this(id, "http://orientx.unizh.ch:9080/nabutil/cgi-bin/fr1?text=");
		}

		public TestSotm(String id, String prefix) {
			setId(id);
			setName("Test Sotm");
			Voice voice = new Voice();
			voice.setPrefix(prefix);
			voice.setName("Default Voice");
			this.list = Collections.singletonList(voice);
		}

		public String getUtterance(String toSay, Map<Object, Object> question) {
			return toSay;
		}

		public List<Voice> getVoices() {
			return this.list;
		}
	}

	public static void configureDigester(Digester digester) {
		configureDigester(digester, true);
	}

	public static void configureDigester(Digester digester, boolean putInCentral) {
		digester.setValidating(false);

		String pat = "items/vocabulary";
		digester.addObjectCreate(pat, "class", Vocabulary.class);
		configureDigesterDesc(digester, pat);
		if (putInCentral) {
			digester.addSetTop(pat, "setCentral");
			digester.addSetNext(pat, "addVoc");
		}

		pat = "items/vocabulary/src";
		configureSource(digester, pat);
		digester.addSetNext(pat, "setSrc");

		pat = "items/vocabulary/mapping/col";
		digester.addObjectCreate(pat, "class", StringColumn.class);
		configureDigesterDesc(digester, pat);
		digester.addSetTop(pat, "setVoc");
		digester.addSetNext(pat, "addCol");

		pat = "items/vocabulary/mapping/img";
		digester.addObjectCreate(pat, "class", ImgColumn.class);
		configureDigesterDesc(digester, pat);
		digester.addSetTop(pat, "setVoc");
		digester.addSetNext(pat, "addCol");

		pat = "items/vocabulary/mapping/snd";
		digester.addObjectCreate(pat, "class", SndColumn.class);
		configureDigesterDesc(digester, pat);
		digester.addSetTop(pat, "setVoc");
		digester.addSetNext(pat, "addSnd");

		pat = "items/vocabulary/mapping/snd/voice";
		digester.addObjectCreate(pat, "class", Voice.class);
		configureDigesterDesc(digester, pat);
		digester.addSetNext(pat, "addVoice");

		pat = "items/vocabulary/modes/mode";
		digester.addObjectCreate(pat, "class", Mode.class);
		configureDigesterDesc(digester, pat);
		digester.addSetTop(pat, "setVoc");
		digester.addSetNext(pat, "addMode");

		pat = "items/vocabulary/modes/mode/filter";
		digester.addObjectCreate(pat, "class", Filter.class);
		configureDigesterDesc(digester, pat);

		digester.addSetNext(pat, "setFilter");

		pat = "items/sotm";
		digester.addObjectCreate(pat, "class", PrefixSotm.class);
		configureDigesterDesc(digester, pat);
		digester.addSetNext(pat, "addSotm");

		pat = "items/sotm/voice";
		digester.addObjectCreate(pat, "class", Voice.class);
		configureDigesterDesc(digester, pat);
		digester.addSetNext(pat, "addVoice");

		pat = "items/script";
		digester.addObjectCreate(pat, "class", DefaultScript.class);
		configureDigesterDesc(digester, pat);
		digester.addSetNext(pat, "addScript");

		pat = "items/script/transliteration";
		digester.addObjectCreate(pat, "class", DefaultScript.IdentityTransliteration.class);
		configureDigesterDesc(digester, pat);
		digester.addSetNext(pat, "addTransliteration");
		digester.addSetTop(pat, "setScript");

		pat = "items/script/presentation";
		digester.addObjectCreate(pat, "class", StringPresentation.class);
		configureDigesterDesc(digester, pat);
		digester.addSetNext(pat, "addPresentation");
		digester.addSetTop(pat, "setScript");

		pat = "items/script/plotter";
		digester.addObjectCreate(pat, "class", PlotterPresentation.class);
		configureDigesterDesc(digester, pat);
		digester.addSetNext(pat, "addPresentation");
		digester.addSetTop(pat, "setScript");
	}

	public static void configureSource(Digester digester, String pat) {
		digester.addObjectCreate(pat, "class", EmptyLineSource.class);
		configureDigesterDesc(digester, pat);
	}

	public static void configureDigesterDesc(Digester digester, String prefix) {
		digester.addSetProperties(prefix);
		digester.addBeanPropertySetter(prefix + "/id");
		digester.addBeanPropertySetter(prefix + "/name");
		digester.addBeanPropertySetter(prefix + "/description");
	}

	@SuppressWarnings("deprecation")
	public void digestXML(String filename) throws SAXException, IOException {
		URL base = (new File(filename)).toURL();
		digestXML(base);
	}

	public void digestXML(URL base) throws SAXException, IOException {
		if (base == null) {
			log("null base: " + base);
			return;
		}
		getDigester();
		this.readerUrl = base;
		this.digester.parse(base.openStream());
		this.readerUrl = null;
	}

	public Object parseObject(InputSource input) throws IOException, SAXException {
		Digester digester = new Digester();
		configureDigester(digester, false);
		digester.parse(input);
		return digester.getRoot();
	}

	public Digester getDigester() throws MalformedURLException {
		if (this.digester == null) {
			this.digester = new Digester();
			configureDigester(this.digester);
		}
		this.digester.clear();
		this.digester.push(this);
		return this.digester;
	}

	public void addVoc(Vocabulary voc) {
		this.vocs.put(voc.getId(), voc);
		voc.setBase(this.readerUrl);
	}

	public void addScript(Script script) {
		getScripts().put(script.getId(), script);
	}

	public Map<Object, Object> getPreferences() {
		return this.preferences;
	}

	public Map<String, Script> getScripts() {
		return this.scripts;
	}

	public Map<String, Vocabulary> getVocs() {
		return this.vocs;
	}

	public void setPreferences(Map<Object, Object> map) {
		this.preferences = map;
	}

	public void setScripts(Map<String, Script> map) {
		this.scripts = map;
	}

	public void setVocs(Map<String, Vocabulary> map) {
		this.vocs = map;
	}

	public Map<String, Sotm> getSotm() {
		return this.sotm;
	}

	public Sotm getSotm(String key) {
		return this.sotm.get(key);
	}

	public Script getScript(String id) {
		if (id == null)
			return null;
		return (Script) getScripts().get(id);
	}

	public void setSotm(Map<String, Sotm> sotm) {
		this.sotm = sotm;
	}

	public void addSotm(Sotm aSotm) {
		this.sotm.put(aSotm.getId(), aSotm);
	}

	public void readDirs(String dirs) {
		String[] s = dirs.split(System.getProperty("path.separator"));
		for (int i = 0; i < s.length; i++) {
			readDir(s[i]);
		}
	}

	public void readDir(String dir) {
		log("Reading dir: " + dir);
		File[] fs = (new File(dir)).listFiles();
		if (fs == null) {
			log("There are no files in: " + dir);
			return;
		}
		for (int i = 0; i < fs.length; i++) {
			File f = fs[i];
			if (f.getName().endsWith(".xml")) {
				try {
					digestXML(f.getAbsolutePath());
				} catch (SAXException e) {
					log("readDir", e);
				} catch (IOException e) {
					log("readDir", e);
				}
			}
		}
	}

	public void readResource() {
		readResource("/etc/voc.xml");
		InputStream inStr = Central.class.getResourceAsStream("/index.txt");
		if (inStr != null) {
			BufferedReader in = new BufferedReader(new InputStreamReader(inStr));
			try {
				String line;
				while ((line = in.readLine()) != null)
					readResource(line);
			} catch (IOException ex) {
				log("readResource()", ex);
			}
		}
	}

	public void readResource(String name) {
		try {
			digestXML(Central.class.getResource(name));
		} catch (SAXException e) {
			log("readResource", e);
		} catch (IOException e) {
			log("readResource", e);
		}
	}

	public void log(String str, Throwable t) {
		System.err.println(str);
		t.printStackTrace(System.err);
	}

	public void log(String str) {
		System.err.println(str);
	}
}