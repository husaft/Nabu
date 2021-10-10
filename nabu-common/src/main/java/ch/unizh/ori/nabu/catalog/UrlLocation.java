package ch.unizh.ori.nabu.catalog;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class UrlLocation {
	protected URL baseUrl;

	public abstract List<?> locations(String paramString) throws MalformedURLException, IOException;

	protected List<URL> locations(String[] files) throws MalformedURLException {
		if (files == null)
			return null;
		List<URL> ret = new ArrayList<URL>(files.length);
		for (int i = 0; i < files.length; i++) {
			ret.add(new URL(this.baseUrl, files[i]));
		}
		return ret;
	}

	public static UrlLocation create(URL baseUrl) throws MalformedURLException {
		UrlLocation ul = null;
		if ("jar".equals(baseUrl.getProtocol())) {
			ul = new JarUrlLocation();
		} else if (baseUrl.getFile().endsWith(".jar")) {
			ul = new JarUrlLocation();
			baseUrl = new URL("jar:" + baseUrl + "!/");
		} else {
			ul = new FileUrlLocation();
		}

		if (ul != null) {
			ul.baseUrl = baseUrl;
		}
		return null;
	}
}