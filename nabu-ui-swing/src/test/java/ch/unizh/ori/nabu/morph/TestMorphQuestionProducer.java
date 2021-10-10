package ch.unizh.ori.nabu.morph;

import ch.unizh.ori.nabu.core.QuestionProducer;

public class TestMorphQuestionProducer extends QuestionProducer {

	private static final long serialVersionUID = 5853479034917269481L;

	private static final String[] roots = new String[] { "domin", "equ", "serv" };
	private static final String[] suff = new String[] { "us", "um", "i", "o", "o", "i", "os", "orum", "is", "is" };
	private static final String[] q = new String[] { "1. sg", "2. sg", "3. sg", "1. pl", "2. pl", "3. pl" };

	public Object produceNext() {
		int r = (int) Math.floor(Math.random() * roots.length);
		int s = (int) Math.floor(Math.random() * suff.length);
		return new TestMorphQuestion(q[s] + " von " + roots[r] + "us", roots[r] + suff[s]);
	}

	public boolean isList() {
		return false;
	}

	public int countQuestions() {
		throw new IllegalArgumentException("Should not be called");
	}

	public void initSession() {
	}

	public void finishSession() {
	}
}