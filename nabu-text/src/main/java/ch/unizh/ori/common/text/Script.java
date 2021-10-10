package ch.unizh.ori.common.text;

import ch.unizh.ori.nabu.core.Descriptable;
import java.util.Locale;
import java.util.Map;

public interface Script extends Descriptable {

	public static final String DEFAULT = "default";

	public static final String UNICODE = "unicode";

	public static final String ASCII = "ascii";

	public static final String TeX = "TeX";

	public static final String ANY_TRANSLITERATION = "any";

	Object convert(Object value, String encoding, String text);

	Map<String, Presentation> getPresentations();

	Presentation getPresentation(String id);

	Locale getLocale();

}