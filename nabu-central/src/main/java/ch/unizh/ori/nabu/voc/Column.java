package ch.unizh.ori.nabu.voc;

import ch.unizh.ori.nabu.core.Central;
import ch.unizh.ori.nabu.core.Descriptable;

public interface Column extends Descriptable {

	Object map(String[] arrayOfString);

	int getColumn();

	void setColumn(int paramInt);

	void setVoc(Vocabulary vocabulary);

	Vocabulary getVoc();

	void setCentral(Central central);

	Central getCentral();

}