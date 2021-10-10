package ch.unizh.ori.nabu.ui.http.tuppu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ImageServlet extends HttpServlet {

	private static final long serialVersionUID = 2223228713529381385L;

	private BufferedImage defaultImg;
	private Graphics2D defaultGraphics;
	private Insets insets;
	@SuppressWarnings("unused")
	private Insets defaultInsets = new Insets(2, 2, 2, 2);
	private Dimension size;
	@SuppressWarnings("unused")
	private Dimension defaultSize;
	private Color background;
	@SuppressWarnings("unused")
	private Color defaultBackground;
	private Color foreground;
	@SuppressWarnings("unused")
	private Color defaultForeground;
	@SuppressWarnings("unused")
	private Font font;
	private boolean drawFrame;
	@SuppressWarnings("unused")
	private boolean defaultDrawFrame;

	@SuppressWarnings("deprecation")
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.defaultImg = new BufferedImage(10, 10, 1);
		this.defaultGraphics = this.defaultImg.createGraphics();

		this.defaultBackground = Color.decode(config.getInitParameter("background"));
		this.defaultForeground = Color.decode(config.getInitParameter("foreground"));
		this.defaultDrawFrame = (new Boolean(config.getInitParameter("drawFrame"))).booleanValue();
	}

	protected void sendPlot(HttpServletRequest request, HttpServletResponse response) throws IOException {
		boolean png = (request.getHeader("Accept").indexOf("image/png") >= 0);
		response.setContentType(png ? "image/png" : "image/jpeg");

		Rectangle2D bounds = getBounds(request, response);
		int width = (int) Math.ceil(bounds.getWidth()) + (getInsets()).left + (getInsets()).right;

		int height = (int) Math.ceil(bounds.getHeight()) + (getInsets()).top + (getInsets()).bottom;

		BufferedImage img = new BufferedImage(Math.max(width, 1), Math.max(height, 1), 1);

		Graphics2D g2 = img.createGraphics();

		g2.setColor(getBackground(request, response));
		g2.fillRect(0, 0, width, height);

		g2.setColor(Color.lightGray);
		g2.drawRect(0, 0, width - 1, height - 1);
		g2.translate((getInsets()).left, (getInsets()).top);

		plot(g2, request, response);

		g2.dispose();

		ImageIO.write(img, png ? "png" : "jpeg", (OutputStream) response.getOutputStream());
	}

	public abstract void plot(Graphics2D paramGraphics2D, HttpServletRequest paramHttpServletRequest,
			HttpServletResponse paramHttpServletResponse);

	public abstract Rectangle2D getBounds(HttpServletRequest paramHttpServletRequest,
			HttpServletResponse paramHttpServletResponse);

	public Color getBackground(HttpServletRequest request, HttpServletResponse response) {
		return Color.white;
	}

	public Font getFont(HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

	public void destroy() {
		this.defaultGraphics.dispose();
		this.defaultGraphics = null;
		this.defaultImg = null;
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		sendPlot(request, response);
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
		return "A plotting servlet";
	}

	protected Graphics2D getDefaultGraphics() {
		return this.defaultGraphics;
	}

	public Insets getInsets() {
		return this.insets;
	}

	public void setInsets(Insets insets) {
		this.insets = insets;
		if (insets == null) {
			this.insets = new Insets(0, 0, 0, 0);
		}
	}

	public Color getBackground() {
		return this.background;
	}

	public void setBackground(Color background) {
		this.background = background;
	}

	public boolean isDrawFrame() {
		return this.drawFrame;
	}

	public void setDrawFrame(boolean drawFrame) {
		this.drawFrame = drawFrame;
	}

	public Color getForeground() {
		return this.foreground;
	}

	public void setForeground(Color foreground) {
		this.foreground = foreground;
	}

	public Dimension getSize() {
		return this.size;
	}

	public void setSize(Dimension size) {
		this.size = size;
	}
}