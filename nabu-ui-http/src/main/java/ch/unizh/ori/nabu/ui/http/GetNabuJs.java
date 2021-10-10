package ch.unizh.ori.nabu.ui.http;

import ch.unizh.ori.nabu.voc.Mode;
import ch.unizh.ori.nabu.voc.ModeField;
import ch.unizh.ori.nabu.voc.Vocabulary;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetNabuJs extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		HttpCentral central = (HttpCentral) getServletContext().getAttribute("central");
		if (!central.getUploadVocList().contains(id)) {
			throw new ServletException("There's no such voc available");
		}

		Vocabulary voc = (Vocabulary) central.getVocs().get(id);
		String loc = central.getUploadVocLocation();

		StringBuffer vocs = new StringBuffer();
		BufferedReader in = new BufferedReader(
				new InputStreamReader(new FileInputStream(new File(loc, id + ".txt")), "UTF-8"));

		boolean multi = false;

		String line;
		while ((line = in.readLine()) != null) {
			if (multi && line.trim().length() == 0)
				continue;
			if (line.trim().length() == 0) {
				continue;
			}
			line = escapeJs(line);
			line = line.replace("\t", "', '");
			vocs.append("['").append(line).append("'],");
		}

		if (vocs.length() > 0)
			vocs.setLength(vocs.length() - 1);
		request.setAttribute("vocString", vocs);

		StringBuffer modes = new StringBuffer();
		for (Iterator<Mode> iterator = voc.getModes().values().iterator(); iterator.hasNext();) {
			Mode m = iterator.next();
			modes.append("[").append(getModeString(m)).append("],");
		}
		modes.setLength(modes.length() - 1);
		request.setAttribute("modesString", modes);

		getServletContext().getRequestDispatcher("/WEB-INF/renderers/nabujs.jsp").forward((ServletRequest) request,
				(ServletResponse) response);
	}

	private static String escapeJs(String line) {
		line = line.replace("'", "\\'");
		return line;
	}

	public static String getModeString(Mode m) {
		StringBuffer a = new StringBuffer();
		StringBuffer b = new StringBuffer();
		for (Iterator<ModeField> iterator = m.createModeFields().iterator(); iterator.hasNext();) {
			ModeField mf = iterator.next();

			StringBuffer c = mf.isAsking() ? b : a;
			if (c.length() > 0)
				c.append(',');
			c.append(mf.getColumn().getColumn());
		}
		return "'" + escapeJs(m.getName()) + "', [" + a + "], [" + b + "]";
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}