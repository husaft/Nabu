package ch.unizh.ori.nabu.ui.http.tuppu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StringPlotServlet extends ImageServlet {

	private static final long serialVersionUID = -3525076126563313932L;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	public void destroy() {
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		super.processRequest(request, response);
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
		return "Plots a String";
	}

	public Rectangle2D getBounds(HttpServletRequest request, HttpServletResponse response) {
		Graphics2D g = getDefaultGraphics();
		Font f = getFont(request);

		TextLayout tl = new TextLayout(getString(request), f, g.getFontRenderContext());

		double width = g.getFontMetrics(f).getStringBounds(getString(request), g).getWidth();
		double height = tl.getBounds().getHeight();

		return new Rectangle2D.Double(0.0D, 0.0D, width, height);
	}

	public Font getFont(HttpServletRequest request) {
		Font font = (Font) request.getAttribute("font");
		if (font == null) {
			int size = 18;
			try {
				size = Integer.parseInt(request.getParameter("size"));
			} catch (NumberFormatException ex) {
			}
			font = new Font("Lucida Sans Regular", 0, size);
			request.setAttribute("font", font);
		}
		return font;
	}

	public void plot(Graphics2D g, HttpServletRequest request, HttpServletResponse response) {
		String str = getString(request);
		Font font = getFont(request);
		g.setColor(Color.blue);
		g.setFont(font);

		TextLayout tl = new TextLayout(getString(request), font, g.getFontRenderContext());
		Rectangle2D bounds = tl.getBounds();

		int y = (int) -bounds.getY();
		g.drawString(str, 0, y);
	}

	protected String getString(HttpServletRequest request) {
		String ret = request.getParameter("string");

		if (ret == null)
			return "";
		return ret;
	}
}