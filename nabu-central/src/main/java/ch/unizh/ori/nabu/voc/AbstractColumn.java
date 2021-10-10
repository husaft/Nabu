package ch.unizh.ori.nabu.voc;

import ch.unizh.ori.nabu.core.Central;
import ch.unizh.ori.nabu.core.DefaultDescriptable;

public abstract class AbstractColumn extends DefaultDescriptable implements Column {

	private static final long serialVersionUID = -950935332969988999L;

	private Vocabulary voc;
	private Central central;
	private int column = -1;

	public int getColumn() {
		return this.column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public Central getCentral() {
		return this.central;
	}

	public void setCentral(Central central) {
		this.central = central;
	}

	public Vocabulary getVoc() {
		return this.voc;
	}

	public void setVoc(Vocabulary vocabulary) {
		this.voc = vocabulary;
	}
}