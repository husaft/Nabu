package ch.unizh.ori.common.text;

import java.io.Serializable;

public interface OldText extends Serializable {

	String getUnicodeString();

	String getAsciiString();

	String getImageURL();

	OldScript getScript();

	OldText getTransliteration();

}