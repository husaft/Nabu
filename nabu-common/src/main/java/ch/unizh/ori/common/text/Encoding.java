package ch.unizh.ori.common.text;

import ch.unizh.ori.nabu.core.Descriptable;

public interface Encoding extends Descriptable {

	Script getScript();

	Object convert(Text paramText);

	Text create(Object paramObject);

}