package ch.unizh.ori.common.text;

import ch.unizh.ori.nabu.core.DefaultDescriptable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class DefaultTextScript extends DefaultDescriptable implements TextScript {

	private static final long serialVersionUID = -3306678667706869708L;

	private List<String> encodings = new ArrayList<String>();
	private Map<String, Presentation> presentations = new TreeMap<String, Presentation>();
	private Map<String, Encoding> converters = new HashMap<String, Encoding>();

	private Locale locale;

	private String ex;

	private String exEnc;

	public DefaultTextScript() {
	}

	public DefaultTextScript(String id, List<String> encodings, Map<String, Encoding> converters) {
		setId(id);
		this.encodings = encodings;
		this.converters = converters;
	}

	public Object convert(String enc, Text text) {
		return getEncoding(enc).convert(text);
	}

	public Text create(String enc, Object param) {
		return getEncoding(enc).create(param);
	}

	public Presentation getPresentation(String id) {
		return (Presentation) getPresentations().get(id);
	}

	public List<String> getEncodings() {
		return this.encodings;
	}

	protected Encoding getEncoding(String enc) {
		return (Encoding) this.converters.get(enc);
	}

	public Map<String, Encoding> getConverters() {
		return this.converters;
	}

	public String toString() {
		return getId() + "/" + getName() + ": " + this.encodings + " / " + this.converters;
	}

	public void setConverters(Map<String, Encoding> map) {
		this.converters = map;
	}

	public void setEncodings(List<String> list) {
		this.encodings = list;
	}

	public String getEx() {
		return this.ex;
	}

	public String getExEnc() {
		return this.exEnc;
	}

	public void setEx(String string) {
		this.ex = string;
	}

	public void setExEnc(String string) {
		this.exEnc = string;
	}

	public void addEncoding(Encoding enc) {
		String id = enc.getId();
		this.encodings.add(id);
		this.converters.put(id, enc);
	}

	public Map<String, Presentation> getPresentations() {
		return this.presentations;
	}

	public void setPresentations(Map<String, Presentation> presentations) {
		this.presentations = presentations;
	}

	public Object convert(Object in, String inEnc, String outEnc) {
		return null;
	}

	public Locale getLocale() {
		return this.locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}