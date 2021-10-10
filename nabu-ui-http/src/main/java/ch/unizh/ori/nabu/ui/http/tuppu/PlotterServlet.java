package ch.unizh.ori.nabu.ui.http.tuppu;

import ch.unizh.ori.tuppu.Plotter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PlotterServlet extends HttpServlet {

	private static final long serialVersionUID = -4651816248310260842L;

	private Plotter plotter;

	@SuppressWarnings({ "unchecked", "deprecation" })
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String className = config.getInitParameter("plotter");
		try {
			this.plotter = (Plotter) Class.forName(className).newInstance();
		} catch (Exception ex) {
			throw new ServletException("Problem with Plotter " + className, ex);
		}
		Enumeration<String> enumeration = config.getInitParameterNames();
		while (enumeration.hasMoreElements()) {
			String param = enumeration.nextElement();
			if ("plotter".equals(param) || "overwriteable".equals(param))
				continue;
			String value = config.getInitParameter(param);
			if (param.equals("plotterId")) {
				this.plotter.setId(value);
				continue;
			}
			if (param.startsWith("realPath:")) {
				this.plotter.setInitParam(param.substring("realPath:".length()),
						getServletContext().getRealPath(value));
				continue;
			}
			if (param.startsWith("realPathUrl:")) {
				System.out.println(
						param.substring("realPathUrl:".length()) + "file:" + getServletContext().getRealPath(value));

				this.plotter.setInitParam(param.substring("realPathUrl:".length()),
						"file:" + getServletContext().getRealPath(value));
				continue;
			}
			this.plotter.setInitParam(param, value);
		}

		this.plotter.init();
	}

	public void destroy() {
		this.plotter = null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		BufferedImage img;
		request.setCharacterEncoding("UTF-8");
		boolean png = (request.getHeader("Accept").indexOf("image/png") >= 0);
		response.setContentType(png ? "image/png" : "image/jpeg");
		String text = request.getParameter("text");

		Map<Object, Object> params = Collections.EMPTY_MAP;
		String v = getInitParameter("overwriteable");
		if (v != null) {
			Enumeration<String> enumeration = "*".equals(v) ? request.getParameterNames()
					: (Enumeration) new StringTokenizer(v, ",");

			params = new HashMap<Object, Object>();
			while (enumeration.hasMoreElements()) {
				String key = enumeration.nextElement();
				key = key.trim();
				if (request.getParameter(key) != null) {
					params.put(key, request.getParameter(key));
				}
			}
		}
		try {
			img = this.plotter.plotToImage(text, params);
		} catch (Exception ex) {
			throw new ServletException(ex);
		}

		ImageIO.write(img, png ? "png" : "jpeg", (OutputStream) response.getOutputStream());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	public String getServletInfo() {
		return "Plots a Plottable created from the plotter " + this.plotter;
	}
}