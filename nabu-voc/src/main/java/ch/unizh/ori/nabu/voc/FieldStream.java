package ch.unizh.ori.nabu.voc;

import ch.unizh.ori.nabu.core.Descriptable;

public interface FieldStream extends Descriptable {

	Object start() throws Exception;

	int getCount();

	String[] next(Object obj) throws Exception;

	void stop(Object obj) throws Exception;

}