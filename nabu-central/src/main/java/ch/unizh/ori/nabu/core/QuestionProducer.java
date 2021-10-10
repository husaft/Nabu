package ch.unizh.ori.nabu.core;

import java.io.Serializable;

public abstract class QuestionProducer implements Serializable {

	private static final long serialVersionUID = 8364616817138836122L;

	public abstract void initSession();

	public abstract void finishSession();

	public abstract Object produceNext();

	public abstract boolean isList();

	public abstract int countQuestions();

}