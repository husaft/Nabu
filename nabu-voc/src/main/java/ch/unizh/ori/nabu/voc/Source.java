package ch.unizh.ori.nabu.voc;

import java.io.Serializable;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;
import org.apache.log4j.Logger;

public abstract class Source<T extends FieldStream> implements Serializable {

	private static final long serialVersionUID = 3645623641243039247L;

	private String src;
	private String enc;
	private String grouping;
	private String header;
	protected String label;
	protected MessageFormat[] fmts;

	public abstract List<T> readLections(URL paramURL) throws Exception;

	public String getEnc() {
		return this.enc;
	}

	public String getGrouping() {
		return this.grouping;
	}

	public String getSrc() {
		return this.src;
	}

	public void setEnc(String encoding) {
		this.enc = encoding;
	}

	public void setGrouping(String grouping) {
		this.grouping = grouping;
	}

	public void setSrc(String source) {
		this.src = source;
	}

	protected String createLessonName(int id, String name) {
		createFmts();
		if (this.label == null) {
			return "Lesson " + name;
		}
		Object[] args = { name };
		return this.fmts[Math.min(id, this.fmts.length - 1)].format(args);
	}

	protected void createFmts() {
		if (this.label == null) {
			return;
		}
		String[] arr = this.label.split(",");
		if (arr.length == 0) {
			this.label = null;
			return;
		}
		this.fmts = new MessageFormat[arr.length];
		for (int i = 0; i < this.fmts.length; i++) {
			this.fmts[i] = new MessageFormat(arr[i]);
		}
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void addHeaderString(String header) {
		if (this.header == null || this.header.equals("")) {
			setHeader(header);
		} else {
			setHeader(getHeader() + "\n" + header);
		}
		Logger.getLogger(Source.class).debug("Header: " + getHeader());
	}

	public String getHeader() {
		return this.header;
	}

	public void setHeader(String header) {
		this.header = header;
	}
}