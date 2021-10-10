package ch.unizh.ori.nabu.ui.http;

import ch.unizh.ori.common.text.PlotterPresentation;
import ch.unizh.ori.nabu.core.Central;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import javax.servlet.ServletContext;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

public class HttpCentral extends Central {

	private static final long serialVersionUID = -1853494906484318228L;

	private transient ServletContext application;
	private static final Logger LOGGER = Logger.getLogger(HttpCentral.class);

	private String uploadVocLocation;

	public static final String DEFAULT_UPLOAD_LOCATION = "/WEB-INF/uploadvocs/";
	public static final String PROPERTIES_SUFFIX = ".properties";

	public HttpCentral() {
		throw new RuntimeException("Never use this constructor!");
	}

	public HttpCentral(ServletContext application, String name) {
		this.application = application;
		application.setAttribute(name, this);
		if (this.uploadVocLocation == null) {
			this.uploadVocLocation = application.getRealPath("/WEB-INF/uploadvocs/");
		}
	}

	public void read() {
		LOGGER.debug("reading: " + getName());
		readUploadVocs();
	}

	public void reloadVoc(String id) {
		if (!(new File(getUploadVocLocation(), id + ".properties")).exists())
			throw new RuntimeException("Doesn't exist or is not reloadable: " + id + ".properties");
		getVocs().remove(id);
		try {
			digestXML((new File(getUploadVocLocation(), id + ".xml")).getAbsolutePath());
		} catch (SAXException ex) {
			LOGGER.error("Reloading " + id, ex);
		} catch (IOException ex) {
			LOGGER.error("Reloading " + id, ex);
		}
	}

	public void readUploadVocs() {
		readDir(getUploadVocLocation());
		readDir(this.application.getRealPath("/WEB-INF/config"));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void readWebAppFiles(String dir) {
		Set paths = this.application.getResourcePaths(dir);
		for (Iterator<String> iter = paths.iterator(); iter.hasNext();) {
			String element = iter.next();
			if (element.endsWith(".xml")) {
				String path = this.application.getRealPath(element);
				try {
					log("digesting: " + path);
					digestXML(path);
				} catch (SAXException e) {
					this.application.log("readFiles", e);
				} catch (IOException e) {
					this.application.log("readFiles", e);
				}
			}
		}
	}

	public void log(String str, Throwable t) {
		this.application.log(str, t);
	}

	public void log(String str) {
		this.application.log(str);
	}

	public String fixLocation(String name) {
		return fixLocation(getUploadVocLocation(), name);
	}

	public static String fixLocation(String location, String name) {
		int index = name.lastIndexOf(File.separator);
		index = Math.max(0, index);
		return location + File.separator + name.substring(index);
	}

	public List<String> getUploadVocList() {
		List<String> ret = new ArrayList<String>();
		String[] files = (new File(getUploadVocLocation())).list();
		for (int i = 0; i < files.length; i++) {
			String file = files[i];
			if (file.endsWith(".properties")) {
				String id = file.substring(0, file.length() - ".properties".length());
				ret.add(id);
			}
		}
		return ret;
	}

	public static Properties loadProps(HttpCentral c, String name) throws Exception {
		return c.loadProps(name);
	}

	public Properties loadProps(String name) throws Exception {
		Properties ret = null;
		if (name != null) {
			String path = fixLocation(name + ".properties");
			ret = new Properties();
			ret.load(new FileInputStream(path));
		}
		return ret;
	}

	public String getPlotterUrl(PlotterPresentation p, String scriptId, Object value, String transliteration) {
		value = getScript(scriptId).convert(value, transliteration, p.getOutTransliteration());
		String prefix = "/plotter?plotter=" + p.getPlotterId() + "&text=";
		if ("egy".equals(p.getPlotterId())) {
			prefix = "/hiero?text=";
		} else if ("sux".equals(p.getPlotterId())) {
			prefix = "/cuneiform?text=";
		}
		return prefix + value;
	}

	public String getUploadVocLocation() {
		return this.uploadVocLocation;
	}

	public void setUploadVocLocation(String string) {
		this.uploadVocLocation = string;
	}
}