package ch.unizh.ori.nabu.ui;

import ch.unizh.ori.nabu.core.QuestionIterator;

public abstract class RendererChooser {
	private Object lastAnswer;
	private boolean firstAnswer;

	public abstract Renderer chooseRenderer(QuestionIterator iterator);

	public Object getLastAnswer() {
		return this.lastAnswer;
	}

	public void setLastAnswer(Object lastAnswer) {
		this.lastAnswer = lastAnswer;
	}

	public boolean isFirstAnswer() {
		return this.firstAnswer;
	}

	public void setFirstAnswer(boolean firstAnswer) {
		this.firstAnswer = firstAnswer;
	}
}