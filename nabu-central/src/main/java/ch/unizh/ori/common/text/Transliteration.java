package ch.unizh.ori.common.text;

import ch.unizh.ori.nabu.core.Descriptable;

public interface Transliteration extends Descriptable {

	Object toStandard(Object obj);

	Object toForeign(Object obj);

	Script getScript();

	void setScript(Script obj);

}