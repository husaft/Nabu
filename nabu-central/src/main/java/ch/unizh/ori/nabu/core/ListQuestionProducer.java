package ch.unizh.ori.nabu.core;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class ListQuestionProducer extends QuestionProducer {

	private static final long serialVersionUID = -4053334151880225410L;

	private Collection<Map<String, Object>> coll;
	private transient Iterator<Map<String, Object>> iter = null;
	private int count = 0;

	public ListQuestionProducer(Collection<Map<String, Object>> coll) {
		this.coll = coll;
	}

	public void initSession() {
		this.iter = this.coll.iterator();
		this.count = this.coll.size();
	}

	public Object produceNext() {
		if (this.iter.hasNext()) {
			this.count--;
			return this.iter.next();
		}
		return null;
	}

	public boolean isList() {
		return true;
	}

	public int countQuestions() {
		return this.count;
	}

	public void finishSession() {
		this.iter = null;
		this.count = 0;
	}
}