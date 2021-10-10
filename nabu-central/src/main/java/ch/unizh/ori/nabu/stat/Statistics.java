package ch.unizh.ori.nabu.stat;

import ch.unizh.ori.nabu.core.QuestionIterator;
import java.io.Serializable;
import java.text.MessageFormat;

public class Statistics implements Serializable {

	private static final long serialVersionUID = 5384243366878653983L;

	private int asked = 0;

	private int total = -1;

	private int toAsk;
	private int problems = 0;
	private int completed = 0;

	private int tries = 0;
	private long lastStart;
	private long totalTime = 0L;

	private QuestionIterator iter;

	private MessageFormat messageFormat;

	public Statistics() {
	}

	public Statistics(QuestionIterator iter) {
		this();
		setIter(iter);
	}

	public void startQuestion() {
		this.lastStart = System.currentTimeMillis();
	}

	public void unsuccessfulTry() {
		this.tries++;
	}

	public void finishQuestion(boolean correct, int prevProblems) {
		this.totalTime += System.currentTimeMillis() - this.lastStart;
		this.asked++;
		if (correct) {
			this.toAsk--;
			if (prevProblems <= 1) {
				this.completed++;
				if (prevProblems == 1) {
					this.problems--;
				}
			}

		} else if (prevProblems <= 0) {
			this.toAsk += 3;
			this.problems++;
		} else {
			this.toAsk++;
		}
	}

	public String toString() {
		if (this.messageFormat == null) {
			this.messageFormat = new MessageFormat(
					"total: {0}, asked: {1}, completed: {2}, toAsk: {3}, problems: {4} - totalTime: {5}, perAsked: {6}, perCompleted: {7}");
		}

		Object[] messages = { getTotal() + "", getAsked() + "", getCompleted() + "", getToAsk() + "",
				getProblems() + "", (getTotalTime() / 1000L) + "", (getTimePerAsked() / 1000L) + "",
				(getTimePerCompleted() / 1000L) + "" };

		return this.messageFormat.format(messages);
	}

	public int getAsked() {
		return this.asked;
	}

	public int getTotal() {
		return this.total;
	}

	public void setTotal(int total) {
		this.total = total;
		this.toAsk = total;
	}

	public QuestionIterator getIter() {
		return this.iter;
	}

	public void setIter(QuestionIterator iter) {
		this.iter = iter;
		setTotal(iter.countQuestions() + 1);
	}

	public int getCompleted() {
		return this.completed;
	}

	public long getLastStart() {
		return this.lastStart;
	}

	public int getProblems() {
		return this.problems;
	}

	public int getToAsk() {
		return this.toAsk;
	}

	public long getTotalTime() {
		return this.totalTime;
	}

	public int getTries() {
		return this.tries;
	}

	public void setToAsk(int i) {
		this.toAsk = i;
	}

	public long getTimePerCompleted() {
		if (this.completed == 0) {
			return 0L;
		}
		return getTotalTime() / this.completed;
	}

	public long getTimePerAsked() {
		if (this.asked == 0) {
			return 0L;
		}
		return getTotalTime() / this.asked;
	}
}