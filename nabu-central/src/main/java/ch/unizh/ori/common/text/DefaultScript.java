package ch.unizh.ori.common.text;

import ch.unizh.ori.nabu.core.DefaultDescriptable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DefaultScript extends DefaultDescriptable implements Script {

	private static final long serialVersionUID = 9101926741368519806L;
	public static final String STANDARD_TRANSLITERATION = "standard";

	private Map<String, Presentation> presentations = new HashMap<String, Presentation>();
	private Map<String, Transliteration> transliterations = new HashMap<String, Transliteration>();

	private Locale locale;

	private String title;

	private String example;

	private String exampleTranslit;

	private String defaultEditablePresentation;
	private String defaultViewOnlyPresentation;
	private String defaultFont;

	public Object convert(Object in, String inTranslit, String outTranslit) {
		if (outTranslit != null && outTranslit.equals(inTranslit)) {
			return in;
		}
		Transliteration inConv = getTransliteration(inTranslit);
		Transliteration outConv = getTransliteration(outTranslit);
		Object standard = inConv.toStandard(in);
		Object out = outConv.toForeign(standard);
		return out;
	}

	public Presentation getPresentation(String id) {
		if (id == null) {
			StringPresentation stringPresentation = new StringPresentation("standard", "Standard Transliteration");
			stringPresentation.setScript(this);
			if (this.defaultFont != null)
				stringPresentation.setFont(this.defaultFont);
			stringPresentation.setOutTransliteration("standard");
			return stringPresentation;
		}
		if (getPresentations().get(id) == null) {
			StringPresentation stringPresentation = new StringPresentation(id, id + " Transliteration");
			stringPresentation.setScript(this);
			if (this.defaultFont != null)
				stringPresentation.setFont(this.defaultFont);
			stringPresentation.setOutTransliteration(id);
			addPresentation(stringPresentation);
		}
		return (Presentation) getPresentations().get(id);
	}

	public Transliteration getTransliteration(String id) {
		if (id == null || id.equals("") || id.equals("standard")) {
			return this.identityTransliteration;
		}
		return (Transliteration) getTransliterations().get(id);
	}

	public void addPresentation(Presentation p) {
		getPresentations().put(p.getId(), p);
	}

	public void addTransliteration(Transliteration c) {
		getTransliterations().put(c.getId(), c);
	}

	public void setLocaleName(String localeName) {
		Locale locale = convertToLocale(localeName);
		setLocale(locale);
	}

	public static Locale convertToLocale(String localeName) {
		String language = "";
		String country = "";
		String variant = "";
		int i = localeName.indexOf('_');
		if (i < 0) {
			language = localeName;
		} else {
			language = localeName.substring(0, i);
			int j = localeName.indexOf('_', i + 1);
			if (j < 0) {
				country = localeName.substring(i + 1);
			} else {
				country = localeName.substring(i + 1, j);
				variant = localeName.substring(j + 1);
			}
		}
		Locale locale = new Locale(language, country, variant);
		return locale;
	}

	public void setId(String string) {
		super.setId(string);
		if (this.locale == null)
			setLocaleName(string);
	}

	public Locale getLocale() {
		return this.locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Map<String, Transliteration> getTransliterations() {
		return this.transliterations;
	}

	public void setTransliterations(Map<String, Transliteration> transliterations) {
		this.transliterations = transliterations;
	}

	public Map<String, Presentation> getPresentations() {
		return this.presentations;
	}

	public void setPresentations(Map<String, Presentation> presentations) {
		this.presentations = presentations;
	}

	public String getExample() {
		return this.example;
	}

	public void setExample(String example) {
		this.example = example;
	}

	public String getExampleTranslit() {
		return this.exampleTranslit;
	}

	public void setExampleTranslit(String exampleTranslit) {
		this.exampleTranslit = exampleTranslit;
	}

	public String getDefaultEditablePresentation() {
		return this.defaultEditablePresentation;
	}

	public void setDefaultEditablePresentation(String defaultEditablePresentation) {
		this.defaultEditablePresentation = defaultEditablePresentation;
	}

	public String getDefaultViewOnlyPresentation() {
		return this.defaultViewOnlyPresentation;
	}

	public void setDefaultViewOnlyPresentation(String defaultViewOnlyPresentation) {
		this.defaultViewOnlyPresentation = defaultViewOnlyPresentation;
	}

	public Transliteration identityTransliteration = new IdentityTransliteration(this, "standard");

	public static class IdentityTransliteration extends DefaultDescriptable implements Transliteration {

		private static final long serialVersionUID = 3634141721687847857L;

		private Script script;

		public IdentityTransliteration() {
		}

		public IdentityTransliteration(Script script) {
			this.script = script;
		}

		public IdentityTransliteration(Script script, String id, String name) {
			super(id, name);
			this.script = script;
		}

		public IdentityTransliteration(Script script, String id) {
			this(script, id, id + " Transliteration");
		}

		public Object toStandard(Object foreign) {
			return foreign;
		}

		public Object toForeign(Object standard) {
			return standard;
		}

		public Script getScript() {
			return this.script;
		}

		public void setScript(Script script) {
			this.script = script;
		}
	}

	public String getDefaultFont() {
		return this.defaultFont;
	}

	public void setDefaultFont(String defaultFont) {
		this.defaultFont = defaultFont;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}