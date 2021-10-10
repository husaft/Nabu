package ch.unizh.ori.nabu.catalog;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class FileUrlLocation extends UrlLocation {

	@SuppressWarnings("deprecation")
	public List<URL> locations(final String suffix) throws MalformedURLException {
		File dir = new File(this.baseUrl.getFile());
		if (!dir.isDirectory())
			try {
				return Collections.singletonList(dir.toURL());
			} catch (MalformedURLException e) {
				e.printStackTrace();

				return null;
			}
		String[] files = dir.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(suffix);
			}
		});
		return locations(files);
	}
}