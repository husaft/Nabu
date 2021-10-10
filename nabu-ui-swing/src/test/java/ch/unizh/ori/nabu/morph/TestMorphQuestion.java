package ch.unizh.ori.nabu.morph;

import ch.unizh.ori.nabu.core.QuestionIterator;

public class TestMorphQuestion {
	private String q;
	private String a;

	public TestMorphQuestion(String q, String a) {
		this.q = q;
		this.a = a;
	}

	public String getQuestionString() {
		return this.q;
	}

	public boolean isCorrect(Object answer) {
		return this.a.equals(answer);
	}

	public String getAnswerString() {
		return this.a;
	}

	public void finishPass(boolean answerWasCorrect, QuestionIterator qi) {
	}
}