package ch.unizh.ori.nabu.ui;

import java.io.Serializable;

public interface Renderer extends Serializable {

	boolean isCorrect();

	void setQuestion(Object obj);

	boolean isShowSolution();

	void clear();

}