package ch.unizh.ori.nabu.ui;

import ch.unizh.ori.common.text.PlotterPresentation;
import ch.unizh.ori.common.text.StringPresentation;
import ch.unizh.ori.nabu.core.ListQuestionProducer;
import ch.unizh.ori.nabu.voc.FieldStream;
import ch.unizh.ori.nabu.voc.Filter;
import ch.unizh.ori.nabu.voc.Mode;
import ch.unizh.ori.nabu.voc.ModeField;
import ch.unizh.ori.nabu.voc.StringColumn;
import ch.unizh.ori.nabu.voc.Vocabulary;
import ch.unizh.ori.tuppu.Plottable;
import java.awt.Dimension;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.avalon.framework.logger.Logger;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.fop.apps.Driver;
import org.apache.fop.apps.FOPException;
import org.apache.fop.configuration.Configuration;
import org.apache.fop.messaging.MessageHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class VocabularyXMLExporter {
	private Vocabulary voc;
	private int width = 4;
	private int height = 4;

	private String paper = "a4";

	private Filter filter;

	private Mode mode;

	private List<ModeField> modeFields;

	private String padding = "0.25cm";

	private List<FieldStream> lections = new ArrayList<FieldStream>();

	private Map<String, FieldStream> idsToLessons = new HashMap<String, FieldStream>();

	public Document getXrtDoc() throws ParserConfigurationException, IOException, FontFormatException {
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

		Element root = doc.createElement("vocabulary");
		doc.appendChild(root);

		ListQuestionProducer qp = getProducer();

		this.idsToLessons.clear();
		for (Iterator<FieldStream> iter = getLections().iterator(); iter.hasNext();) {
			FieldStream fs = iter.next();
			this.idsToLessons.put(fs.getId(), fs);
		}

		this.modeFields = this.mode.createModeFields();

		if (!qp.isList()) {
			throw new IllegalArgumentException("No list: " + qp);
		}
		qp.initSession();
		int count = qp.countQuestions();

		int itemsPerPage = this.width * this.height;
		int pages = (int) Math.ceil((count / itemsPerPage));

		root.setAttribute("pages", String.valueOf(pages));
		root.setAttribute("itemsPerPage", String.valueOf(itemsPerPage));
		root.setAttribute("count", String.valueOf(count));
		root.setAttribute("width", String.valueOf(this.width));
		root.setAttribute("height", String.valueOf(this.height));

		root.setAttribute("td-height", (21.0F / this.height) + "cm");
		root.setAttribute("padding", this.padding);

		if (this.paper != null) {
			root.setAttribute("paper", this.paper);
		}
		for (int p = 0; p < pages; p++) {
			Object[] page = new Object[itemsPerPage];

			for (int i = 0; i < page.length; i++) {
				page[i] = qp.produceNext();
				if (page[i] == null) {
					break;
				}
			}

			Element recto = doc.createElement("page");
			root.appendChild(recto);
			recto.setAttribute("recto", "true");
			Element verso = doc.createElement("page");
			root.appendChild(verso);
			verso.setAttribute("verso", "true");
			for (int row = 0; row < this.height; row++) {
				Element rectoRow = doc.createElement("tr");
				recto.appendChild(rectoRow);
				Element versoRow = doc.createElement("tr");
				verso.appendChild(versoRow);
				for (int col = 0; col < this.width; col++) {

					int j = this.width * row + col;
					rectoRow.appendChild(createQuestionNode(doc, page[j], false));
					j = this.width * row + this.width - col - 1;
					versoRow.appendChild(createQuestionNode(doc, page[j], true));
				}
			}
		}

		return doc;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ListQuestionProducer getProducer() {
		List<Map<String, Object>> theVoc = this.voc.createVocList(this.lections);
		if (this.filter != null) {
			for (Iterator<Map<String, Object>> iter = theVoc.iterator(); iter.hasNext();) {
				Map<String, Object> v = iter.next();
				if (!this.filter.accept((Map) v)) {
					iter.remove();
				}
			}
		}
		ListQuestionProducer qp = new ListQuestionProducer(theVoc);
		return qp;
	}

	@SuppressWarnings("rawtypes")
	private Node createQuestionNode(Document doc, Object question, boolean verso)
			throws IOException, ParserConfigurationException, FontFormatException {
		if (question == null) {
			return doc.createElement("empty");
		}
		Map q = (Map) question;
		Element ret = doc.createElement("td");
		ret.setAttribute("voc", this.voc.getName());
		if (this.voc.isDecorate()) {
			FieldStream fs = (FieldStream) this.idsToLessons.get(q.get("lesson"));
			ret.setAttribute("lesson", fs.getName());
		}
		for (Iterator<ModeField> iter = this.modeFields.iterator(); iter.hasNext();) {
			ModeField mf = iter.next();
			if (verso != mf.isAsking())
				continue;
			Object data = q.get(mf.getKey());
			if (mf.isImage()) {
				Element image = doc.createElement("image");
				ret.appendChild(image);
				image.setAttribute("text", data.toString());
				if (mf.getPresentation() instanceof PlotterPresentation) {
					PlotterPresentation pp = (PlotterPresentation) mf.getPresentation();
					String transliteration = ((StringColumn) mf.getColumn()).getTransliteration();
					Object converted = pp.getScript().convert(data.toString(), transliteration,
							pp.getOutTransliteration());
					Node n = createSVG(doc, pp, converted.toString());
					image.appendChild(n);
				}
				continue;
			}
			Element text = doc.createElement("text");
			ret.appendChild(text);
			if (mf.getPresentation() instanceof StringPresentation) {
				StringPresentation sp = (StringPresentation) mf.getPresentation();
				if (sp.getFont() != null)
					text.setAttribute("font", sp.getFont());
				String transliteration = ((StringColumn) mf.getColumn()).getTransliteration();
				String outText = sp.getOutText(data, transliteration);
				text.setAttribute("text", outText);
				continue;
			}
			text.setAttribute("text", data.toString());
		}

		return ret;
	}

	public static Node createSVG(Document document, PlotterPresentation pp, String text)
			throws IOException, ParserConfigurationException, FontFormatException {
		if (document == null) {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		}

		SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(document);
		ctx.setEmbeddedFontsOn(true);
		SVGGraphics2D svgGenerator = new SVGGraphics2D(ctx, true);

		Plottable plottable = pp.getPlotter().createPlottable(text, new HashMap<Object, Object>());
		plottable.plot((Graphics2D) svgGenerator);

		Element ret = svgGenerator.getRoot();
		Dimension size = plottable.getSize();
		ret.setAttribute("width", size.getWidth() + "");
		ret.setAttribute("height", size.getHeight() + "");
		return ret;
	}

	public static void loadHyphenation(String dir) {
		Configuration.put("hyphenation-dir", dir);
	}

	public static void writeXmlFile(Document doc, String filename) throws TransformerException {
		Source source = new DOMSource(doc);

		File file = new File(filename);
		Result result = new StreamResult(file);

		Transformer xformer = TransformerFactory.newInstance().newTransformer();
		xformer.transform(source, result);
	}

	public static void xsl(Node in, String outFilename, String xslFilename) throws FileNotFoundException {
		xsl(in, xslFilename, new StreamResult(new FileOutputStream(outFilename)));
	}

	public static void xsl(Node in, String xslFilename, Result result) {
		try {
			TransformerFactory factory = TransformerFactory.newInstance();

			Templates template = factory.newTemplates(new StreamSource(new File(xslFilename)));

			Transformer xformer = template.newTransformer();

			Source source = new DOMSource(in);

			xformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			/*
			 * SourceLocator locator = e.getLocator(); int col = locator.getColumnNumber();
			 * int line = locator.getLineNumber(); String publicId = locator.getPublicId();
			 * String systemId = locator.getSystemId();
			 */
			e.printStackTrace();
		}
	}

	public static void convertXML2PDF(Node xml, String xsl, OutputStream pdf)
			throws IOException, FOPException, TransformerException {
		Driver driver = new Driver();

		ConsoleLogger consoleLogger = new ConsoleLogger(1);
		driver.setLogger((Logger) consoleLogger);
		MessageHandler.setScreenLogger((Logger) consoleLogger);

		driver.setRenderer(1);

		OutputStream out = pdf;
		try {
			driver.setOutputStream(out);

			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(new StreamSource(xsl));

			Source src = new DOMSource(xml);

			Result res = new SAXResult(driver.getContentHandler());

			transformer.transform(src, res);
		} catch (TransformerException e) {
			e.printStackTrace();

			SourceLocator locator = e.getLocator();
			if (locator != null) {
				int col = locator.getColumnNumber();
				int line = locator.getLineNumber();
				String publicId = locator.getPublicId();
				String systemId = locator.getSystemId();
				System.err.println(col + "/" + line + " : " + publicId + ", " + systemId);
			}
		} finally {
			out.close();
		}
	}

	public Vocabulary getVoc() {
		return this.voc;
	}

	public void setVoc(Vocabulary voc) {
		this.voc = voc;
		setLections(new ArrayList<FieldStream>(voc.getLections()));
		setMode(voc.getModes().values().iterator().next());
	}

	public Filter getFilter() {
		return this.filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public List<FieldStream> getLections() {
		return this.lections;
	}

	public void setLections(List<FieldStream> lections) {
		this.lections = lections;
	}

	public Mode getMode() {
		return this.mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
		setFilter(mode.getFilter());
	}

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public String getPadding() {
		return this.padding;
	}

	public void setPadding(String padding) {
		this.padding = padding;
	}
}