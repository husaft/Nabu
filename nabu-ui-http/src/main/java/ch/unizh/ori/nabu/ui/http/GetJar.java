package ch.unizh.ori.nabu.ui.http;

import ch.unizh.ori.nabu.voc.Vocabulary;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetJar extends HttpServlet {

	private static final long serialVersionUID = -4931529307434181575L;

	public void init() throws ServletException {
	}

	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<Object, Object> files = new HashMap<Object, Object>();

		String id = request.getParameter("id");
		HttpCentral central = (HttpCentral) getServletContext().getAttribute("central");
		if (!central.getUploadVocList().contains(id)) {
			throw new ServletException("There's no such voc available");
		}

		Vocabulary voc = (Vocabulary) central.getVocs().get(id);
		String srcFilename;
		String loc = central.getUploadVocLocation();
		String midletName = null;
		if (!"midlet".equals(request.getParameter("type"))) {
			srcFilename = "/WEB-INF/Nabu-template.jar";
			files.put("etc/voc.xml", new File(loc, id + ".xml"));
			files.put("etc/" + id + ".txt", new File(loc, id + ".txt"));
		} else {
			srcFilename = "/WEB-INF/Nabuttu-template.jar";
			try {
				files.putAll((new NabuttuCreator()).gatherData(id, new File(loc, id + ".txt"), voc));
				midletName = "Nabuttu " + voc.getName();
			} catch (Exception ex) {
				throw new ServletException("Problem producing MIDlet", ex);
			}
		}

		String realFilename = getServletContext().getRealPath(srcFilename);
		BufferedInputStream src = new BufferedInputStream(new FileInputStream(realFilename));

		response.setContentType("application/java-archive");
		copyJar(src, files, (OutputStream) response.getOutputStream(), midletName, true);
	}

	private transient byte[] buf = new byte[10240];

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void copyJar(InputStream src, Map files, OutputStream dest, String midletName, boolean excludeJava)
			throws IOException, FileNotFoundException {
		JarInputStream in = null;
		JarOutputStream out = null;
		try {
			in = new JarInputStream(src);
			Manifest manifest = in.getManifest();
			if (midletName != null) {
				manifest.getMainAttributes().putValue("MIDlet-Name", midletName);
			}
			out = new JarOutputStream(dest, manifest);

			for (Iterator<String> iter = files.keySet().iterator(); iter.hasNext();) {
				String name = iter.next();
				JarEntry jarEntry = new JarEntry(name);
				InputStream eIn = null;
				try {
					Object val = files.get(name);
					if (val instanceof File) {
						jarEntry.setTime(((File) val).lastModified());
						eIn = new BufferedInputStream(new FileInputStream((File) val));
					} else {

						eIn = (InputStream) val;
					}
					out.putNextEntry(jarEntry);
					int len;
					while ((len = eIn.read(this.buf, 0, this.buf.length)) > 0) {
						out.write(this.buf, 0, len);
					}
					out.closeEntry();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (eIn != null)
						eIn.close();
				}
			}
			JarEntry entry;
			while ((entry = in.getNextJarEntry()) != null) {
				if (files.containsKey(entry.getName()) || (excludeJava && entry.getName().endsWith(".java"))) {
					in.closeEntry();
					continue;
				}
				entry = new JarEntry(entry.getName());
				out.putNextEntry(entry);
				int len;
				while ((len = in.read(this.buf, 0, this.buf.length)) > 0) {
					out.write(this.buf, 0, len);
				}
				out.closeEntry();
				in.closeEntry();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
			} finally {
				if (out != null)
					out.close();
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}