package ch.unizh.ori.nabu.ui.http.tuppu;

import ch.unizh.ori.tuppu.Box;
import ch.unizh.ori.tuppu.Plotter;
import ch.unizh.ori.tuppu.hieroglyph.HieroPlotter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HieroPlotServlet extends ImageServlet {

	private static final long serialVersionUID = 4603525715606499232L;

	public static final int SIZE = 65;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	public void destroy() {
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String text = request.getParameter("text");
		HieroPlotter hp = new HieroPlotter(getDefaultGraphics(), 65);
		Box r = hp.constructBox(getDefaultGraphics(), text, new Font("Dialog", 0, 65), null);

		request.setAttribute("hp", hp);
		request.setAttribute("r", r);
		super.processRequest(request, response);
	}

	public String getServletInfo() {
		return "Plots a hieroglyphic String";
	}

	public Rectangle2D getBounds(HttpServletRequest request, HttpServletResponse response) {
		Box r = (Box) request.getAttribute("r");
		Dimension d = r.getSize();
		return new Rectangle2D.Double(0.0D, 0.0D, d.width, d.height);
	}

	public void plot(Graphics2D g, HttpServletRequest request, HttpServletResponse response) {
		g.setPaint(Color.black);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Box r = (Box) request.getAttribute("r");
		Plotter.paint(g, r);
	}
}