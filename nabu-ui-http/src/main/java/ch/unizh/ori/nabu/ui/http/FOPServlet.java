package ch.unizh.ori.nabu.ui.http;

import ch.unizh.ori.nabu.ui.VocabularyXMLExporter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ELException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import org.apache.fop.apps.Options;
import org.apache.fop.configuration.Configuration;
import org.w3c.dom.Document;
import org.xml.sax.helpers.XMLReaderFactory;

@SuppressWarnings("deprecation")
public class FOPServlet extends HttpServlet {

	private static final long serialVersionUID = 6567504862558710731L;

	public void init() throws ServletException {
		try {
			String name = XMLReaderFactory.createXMLReader().getClass().getName();

			System.setProperty("org.xml.sax.parser", name);
			new Options(new File(getServletContext().getRealPath("/WEB-INF/lib/fontmetrics/userconfig.xml")));
			VocabularyXMLExporter.loadHyphenation(getServletContext().getRealPath("WEB-INF/lib/hyphenation"));
			Configuration.put("fontBaseDir", getServletContext().getRealPath("/WEB-INF/lib/fontmetrics/"));
		} catch (Exception ex) {
			throw new ServletException("Problem reading FOP-configurations", ex);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Document xml;

		PageContext page = JspFactory.getDefaultFactory().getPageContext((Servlet) this, (ServletRequest) request,
				(ServletResponse) response, "", true, response.getBufferSize(), false);

		try {
			xml = (Document) page.getExpressionEvaluator().evaluate(getInitParameter("xml"), Document.class,
					page.getVariableResolver(), null);

		} catch (ELException ex1) {
			throw new ServletException(ex1);
		}
		try {
			VocabularyXMLExporter.writeXmlFile(xml, getServletContext().getRealPath("/WEB-INF/xslt/out.xml"));
		} catch (TransformerException ex1) {
			ex1.printStackTrace();
		}

		String xsl = getInitParameter("xsl");
		VocabularyXMLExporter.xsl(xml, getServletContext().getRealPath(xsl),
				new StreamResult(new File(getServletContext().getRealPath("/WEB-INF/xslt/out.fo.xml"))));
		try {
			response.setContentType("application/pdf");
			VocabularyXMLExporter.convertXML2PDF(xml, getServletContext().getRealPath(xsl),
					(OutputStream) response.getOutputStream());
		} catch (Exception ex) {
			response.setContentType("text/html");
			throw new ServletException(ex);
		}
		response.getOutputStream().close();
	}
}