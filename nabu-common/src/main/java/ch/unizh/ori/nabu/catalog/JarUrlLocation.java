package ch.unizh.ori.nabu.catalog;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarUrlLocation extends UrlLocation {

	public List<String> locations(String suffix) throws MalformedURLException, IOException {
		List<String> ret = new ArrayList<String>();
		JarURLConnection jarConnection = (JarURLConnection) this.baseUrl.openConnection();
		JarFile jarFile = jarConnection.getJarFile();
		String prefix = jarConnection.getEntryName();
		Enumeration<JarEntry> enumeration = jarFile.entries();
		while (enumeration.hasMoreElements()) {
			JarEntry je = enumeration.nextElement();
			String name = je.getName();
			if (name.startsWith(prefix) && name.endsWith(suffix)) {
				ret.add(name);
			}
		}
		return ret;
	}
}