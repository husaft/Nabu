package ch.unizh.ori.nabu.core;

import ch.unizh.ori.nabu.stat.Statistics;
import ch.unizh.ori.nabu.ui.Renderer;

public interface QuestionIterator {

	Object getQuestion();

	QuestionProducer getProducer();

	Renderer getRenderer();

	Statistics getStatistics();

	void init();

	void destroy();

	boolean doEvaluate();

	void next();

	int countQuestions();

	int countProblems();

	int getTimesForProblem(Object paramObject);

	boolean isProblemsOnly();

	void setProblemsOnly(boolean paramBoolean);

}