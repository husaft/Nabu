package ch.unizh.ori.nabu.ui.http.taglib;

import ch.unizh.ori.common.text.Script;
import ch.unizh.ori.nabu.core.Central;
import ch.unizh.ori.nabu.ui.http.HttpCentral;
import ch.unizh.ori.nabu.voc.StringSource;
import ch.unizh.ori.nabu.voc.Vocabulary;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.xml.sax.InputSource;

@SuppressWarnings("deprecation")
public class MakeVocTag extends BodyTagSupport {

	private static final long serialVersionUID = 4067079622672313171L;

	private HttpCentral central;
	private String location = null;

	private String filename = null;

	private Properties vars;

	private String separator;

	private boolean dontWrite = false;

	@SuppressWarnings("unchecked")
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
		DiskFileUpload upload = new DiskFileUpload();

		try {
			FileItem voc = null;
			String vocName = null;
			this.vars = new Properties();

			if (request.getContentType().contains("multipart")) {
				List<FileItem> items = upload.parseRequest(request);

				for (Iterator<FileItem> iter = items.iterator(); iter.hasNext();) {
					FileItem fi = iter.next();
					if (fi.isFormField()) {
						this.vars.put(fi.getFieldName(), fi.getString("UTF-8"));
						continue;
					}
					voc = fi;
					vocName = voc.getName();
				}
			} else {

				for (Iterator<String> iter = request.getParameterMap().keySet().iterator(); iter.hasNext();) {
					String par = iter.next();
					this.vars.put(par, request.getParameter(par));
				}
			}
			boolean vocInVars = (this.vars.getProperty("voc") != null);
			if (voc == null && vocInVars) {
				vocName = this.vars.getProperty("vocName");
			}

			if (request.getRemoteUser() != null)
				this.vars.put("change.user", request.getRemoteUser());
			this.vars.put("change.date", (new Date()).toString());
			this.vars.put("change.ip", request.getRemoteHost());

			if ("on".equalsIgnoreCase(this.vars.getProperty("simple"))) {
				String lang1 = this.vars.getProperty("lang1");
				String lang2 = this.vars.getProperty("lang2");
				this.vars.remove("lang1");
				this.vars.remove("lang2");

				String namePart = "";
				if (vocName != null) {
					String s = vocName;
					int len = Math.min(2, s.length());
					s = s.substring(0, len);
					for (int n = 0; n < len; n++) {
						if (!Character.isLetter(s.charAt(n))) {
							len = n;
						}
					}
					if (len > 0) {
						namePart = "_" + s.substring(0, len);
					}
				}
				String str1 = lang1 + namePart, idRoot = str1;
				int i = 1;
				while (this.central.vocs.containsKey(str1)) {
					str1 = idRoot + "_" + i++;
				}
				this.vars.setProperty("id", str1);

				Script s1 = this.central.getScript(lang1);
				Script s2 = this.central.getScript(lang2);
				this.vars.setProperty("name", s1.getName());
				this.vars.setProperty("description", "");

				String[] colLines = { lang1 + this.separator + s1.getName() + this.separator + s1.getId(),
						lang2 + this.separator + s2.getName() + this.separator + s2.getId() };

				String[] colIds = { lang1, lang2 };
				BufferedReader in = null;
				if (vocInVars) {
					in = new BufferedReader(new StringReader(this.vars.getProperty("voc")));
				} else {
					in = new BufferedReader(new InputStreamReader(voc.getInputStream(), "UTF-8"));
				}
				String line = in.readLine();
				if (line != null && line.length() > 0) {
					if (line.charAt(0) == '﻿') {
						line = line.substring(1);
					}
					if (line.startsWith("##")) {
						System.out.println(line);
						line = line.substring(2);
						line = line.trim();
						colLines = line.split("\t");
						colIds = new String[colLines.length];
						for (int n = 0; n < colLines.length; n++) {
							colLines[n] = colLines[n];
							int i1 = colLines[n].indexOf(':');
							colIds[n] = (i1 < 0) ? colLines[n] : colLines[n].substring(0, i1);
						}
					}
				}
				in.close();

				StringBuffer columns = new StringBuffer();
				for (int j = 0; j < colLines.length; j++) {
					columns.append(colLines[j]).append('\n');
				}
				this.vars.setProperty("columns", columns.substring(0, columns.length() - 1));

				StringBuffer modes = new StringBuffer();
				for (int m = colLines.length - 1; m > 0; m--) {
					for (int n = 0; n < m; n++)
						modes.append(colIds[n]).append("=");
					modes.append('?').append(colIds[m]).append('\n');
				}
				modes.append('?').append(colIds[0]);
				for (int k = 1; k < colIds.length; k++)
					modes.append("=").append(colIds[k]);
				this.vars.setProperty("modes", modes.toString());

				this.vars.setProperty("emptyline", "on");
				this.vars.setProperty("enc", "UTF-8");
				this.vars.setProperty("lfmt", "");
				this.vars.setProperty("columnSep", this.separator);
				this.pageContext.setAttribute("showConfig", this.vars.getProperty("showConfig"));
			}

			this.pageContext.setAttribute("vars", this.vars);
			this.pageContext.setAttribute("columns", readTable(this.vars.getProperty("columns")));
			this.pageContext.setAttribute("modes", readTable(this.vars.getProperty("modes")));

			String id = this.vars.getProperty("id");
			this.filename = HttpCentral.fixLocation(getLocation(), id);
			File vocFile = new File(this.filename + ".txt");
			this.pageContext.setAttribute("vocUrl", vocFile.toURL());

			if (voc != null && voc.getSize() > 0L && !isDontWrite()) {
				voc.write(vocFile);
				voc = null;
			}

			this.pageContext.setAttribute("name", this.vars.getProperty("name"));
			this.pageContext.setAttribute("id", this.vars.getProperty("id"));
		} catch (Exception e) {
			throw new JspException(e);
		}

		return 2;
	}

	@SuppressWarnings("rawtypes")
	private List readTable(String string) throws IOException {
		if (string == null || string.length() == 0) {
			return Collections.EMPTY_LIST;
		}
		List<String[]> ret = new ArrayList<String[]>();
		BufferedReader in = new BufferedReader(new StringReader(string));
		for (String l = in.readLine(); l != null; l = in.readLine()) {
			if (l.length() != 0) {

				String[] arr = l.split(this.separator);
				ret.add(arr);
			}
		}
		return ret;
	}

	public int doEndTag() throws JspException {
		String vocXml = getBodyContent().getString();

		if (!isDontWrite()) {
			try {
				writeOtherVoc(this.filename, vocXml, this.vars);
			} catch (IOException e) {
				throw new JspException(e);
			}
		} else {
			Vocabulary v;
			try {
				v = (Vocabulary) this.central.parseObject(new InputSource(new StringReader(vocXml)));
				v.setCentral((Central) this.central);
				v.setSrc(new StringSource(this.vars.getProperty("voc")));
			} catch (Exception e) {
				throw new JspException(e);
			}
			this.pageContext.getSession().setAttribute("myVoc", v);
		}

		this.pageContext.removeAttribute("vars");
		this.pageContext.removeAttribute("columns");
		this.pageContext.removeAttribute("modes");

		if (this.central != null && !this.dontWrite) {

			this.central.reloadVoc(this.vars.getProperty("id"));
		}

		this.filename = null;
		this.vars = null;
		this.central = null;

		this.location = null;

		this.separator = null;
		this.dontWrite = false;
		return 6;
	}

	private void writeOtherVoc(String file, String vocXml, Properties vars) throws IOException {
		System.err.println("UploadVoc: " + file);

		PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file + ".xml"), "UTF-8"));
		out.write(vocXml);
		out.close();

		vars.store(new FileOutputStream(file + ".properties"), "the variables for this voc");
	}

	public String getLocation() {
		if (this.location == null && this.central != null) {
			return this.central.getUploadVocLocation();
		}
		return this.location;
	}

	public void setLocation(String string) {
		this.location = string;
	}

	public void setCentral(HttpCentral central) {
		this.central = central;
	}

	public String getSeparator() {
		return this.separator;
	}

	public void setSeparator(String string) {
		this.separator = string;
	}

	public boolean isDontWrite() {
		return this.dontWrite;
	}

	public void setDontWrite(boolean dontWrite) {
		this.dontWrite = dontWrite;
	}
}