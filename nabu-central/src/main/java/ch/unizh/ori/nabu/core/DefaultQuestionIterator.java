package ch.unizh.ori.nabu.core;

import ch.unizh.ori.nabu.stat.Statistics;
import ch.unizh.ori.nabu.ui.Renderer;
import ch.unizh.ori.nabu.ui.RendererChooser;
import ch.unizh.ori.nabu.core.api.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DefaultQuestionIterator implements QuestionIterator, Serializable {

	private static final long serialVersionUID = 1606570427198216282L;

	public List<QuestionProducer> listProducers = new ArrayList<QuestionProducer>();
	public List<QuestionProducer> infiniteProducers = new ArrayList<QuestionProducer>();

	private Object question;

	private Renderer renderer;
	private Statistics statistics = new Statistics(this);

	private QuestionProducer producer;

	private RendererChooser rendererChooser;

	private User user;

	private ProblemProducer problemProducer = new ProblemProducer();

	private List<Object> problemsList = new LinkedList<Object>();

	private boolean problemsOnly;

	private Renderer lastRenderer;

	public DefaultQuestionIterator() {
		addProducer(this.problemProducer);
	}

	public void next() {
		QuestionProducer p = chooseProducer();
		setProducer(p);
		if (p == null) {
			setQuestion(null);

			this.lastRenderer = getRenderer();
			setRenderer(null);
			return;
		}
		Object q = p.produceNext();
		setQuestion(q);
		if (p.isList() && p != this.problemProducer && p.countQuestions() <= 0) {
			this.listProducers.remove(p);
		}

		Renderer r = null;
		if (p == this.problemProducer && q != null) {
			r = this.problemProducer.getRendererFor(q);
		} else if (p != null) {
			r = getRendererChooser().chooseRenderer(this);
		}

		setRenderer(r);

		getStatistics().startQuestion();
	}

	public int countQuestions() {
		if (this.infiniteProducers.size() >= 1)
			return -1;
		int sum = 0;
		for (Iterator<QuestionProducer> i = this.listProducers.iterator(); i.hasNext();) {
			sum += ((QuestionProducer) i.next()).countQuestions();
		}
		return sum;
	}

	protected QuestionProducer chooseProducer() {
		boolean hasProblems = (this.problemProducer.countQuestions() > 0);

		if (isProblemsOnly()) {
			return hasProblems ? this.problemProducer : null;
		}
		double r = Math.random();
		int numL = this.listProducers.size();
		if (!hasProblems)
			numL--;
		int numI = this.infiniteProducers.size();

		if (numL == 0 && numI == 0) {
			return null;
		}

		double d = numL / (numL + numI);

		if (r < d) {

			int[] counts = new int[this.listProducers.size()];
			int sum = 0;
			for (int i = 0; i < counts.length; i++) {
				QuestionProducer qp = this.listProducers.get(i);
				sum += qp.countQuestions();
				counts[i] = sum;
			}
			int index = (int) Math.floor(Math.random() * sum);

			for (int j = 0; j < counts.length; j++) {
				if (index < counts[j]) {
					return this.listProducers.get(j);
				}
			}
		} else {

			int i = (int) Math.floor(Math.random() * this.infiniteProducers.size());
			return this.infiniteProducers.get(i);
		}
		return null;
	}

	protected void accountProblems(boolean answerWasCorrect) {
		Object question = getQuestion();
		int i = getTimesForProblem(question);

		if (answerWasCorrect) {
			if (i > 0) {
				if (i == 1) {
					removeProblem(question);
				} else {
					setTimesForProblem(question, i - 1);
				}
			}
		} else if (i <= 0) {
			addProblem(question, getRenderer(), 3);
		} else {
			setTimesForProblem(question, i + 1);
		}

		getStatistics().finishQuestion(answerWasCorrect, i);
	}

	protected void addProblem(Object question, Renderer renderer) {
		addProblem(question, renderer, 3);
	}

	protected void addProblem(Object question, Renderer renderer, int times) {
		this.problemProducer.add(question, renderer, times);
		this.problemsList.add(question);
	}

	public int getTimesForProblem(Object question) {
		return this.problemProducer.getTimesFor(question);
	}

	protected void setTimesForProblem(Object question, int times) {
		this.problemProducer.setTimesFor(question, times);
	}

	protected void removeProblem(Object question) {
		this.problemProducer.remove(question);
	}

	public void addProducer(QuestionProducer qp) {
		if (qp.isList()) {
			this.listProducers.add(qp);
		} else {
			this.infiniteProducers.add(qp);
		}
	}

	public void removeProducer(QuestionProducer qp) {
		if (qp == this.problemProducer)
			return;
		if (qp.isList()) {
			this.listProducers.remove(qp);
		} else {
			this.infiniteProducers.remove(qp);
		}
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public RendererChooser getRendererChooser() {
		return this.rendererChooser;
	}

	public void setRendererChooser(RendererChooser rendererChooser) {
		this.rendererChooser = rendererChooser;
	}

	public QuestionProducer getProducer() {
		return this.producer;
	}

	public Object getQuestion() {
		return this.question;
	}

	public Renderer getRenderer() {
		return this.renderer;
	}

	protected void setProducer(QuestionProducer producer) {
		this.producer = producer;
	}

	protected void setQuestion(Object question) {
		this.question = question;
	}

	protected void setRenderer(Renderer renderer) {
		if (this.renderer != null) {
			this.renderer.clear();
		}
		this.renderer = renderer;
		if (renderer != null) {
			renderer.setQuestion(getQuestion());
		}
	}

	public boolean isProblemsOnly() {
		return this.problemsOnly;
	}

	public void setProblemsOnly(boolean problemsOnly) {
		this.problemsOnly = problemsOnly;
	}

	public int countProblems() {
		return this.problemProducer.countQuestions();
	}

	public static class ProblemProducer extends QuestionProducer {

		private static final long serialVersionUID = -7767566829011485791L;

		private Map<Object, Integer> problemTimes = new HashMap<Object, Integer>();
		private Map<Object, Renderer> problemRenderers = new HashMap<Object, Renderer>();
		private List<Object> problems = new ArrayList<Object>();

		public int countQuestions() {
			return this.problems.size();
		}

		public void initSession() {
		}

		public boolean isList() {
			return true;
		}

		public Object produceNext() {
			if (countQuestions() == 0)
				return null;
			int i = (int) Math.floor(Math.random() * countQuestions());
			return this.problems.get(i);
		}

		@SuppressWarnings("deprecation")
		public void add(Object q, Renderer renderer, int times) {
			this.problems.add(q);
			this.problemTimes.put(q, new Integer(times));
			this.problemRenderers.put(q, renderer);
		}

		public int getTimesFor(Object question) {
			Integer i = (Integer) this.problemTimes.get(question);
			if (i == null)
				return -1;
			return i.intValue();
		}

		public Renderer getRendererFor(Object question) {
			return (Renderer) this.problemRenderers.get(question);
		}

		@SuppressWarnings("deprecation")
		public void setTimesFor(Object question, int times) {
			this.problemTimes.put(question, new Integer(times));
		}

		public void remove(Object q) {
			this.problems.remove(q);
			this.problemTimes.remove(q);
			this.problemRenderers.remove(q);
		}

		public void finishSession() {
			this.problemTimes.clear();
			this.problemRenderers.clear();
		}
	}

	public Statistics getStatistics() {
		return this.statistics;
	}

	public boolean doEvaluate() {
		Renderer r = getRenderer();
		boolean correct = r.isCorrect();
		boolean showSolution = r.isShowSolution();
		if (!showSolution) {

			if (correct) {
				accountProblems(true);
				next();
				return true;
			}
			getStatistics().unsuccessfulTry();
		} else {

			accountProblems(false);
		}
		return false;
	}

	public void init() {
		int total = 0;
		for (Iterator<QuestionProducer> iterator1 = this.listProducers.iterator(); iterator1.hasNext();) {
			QuestionProducer qp = iterator1.next();
			qp.initSession();
			total += qp.countQuestions();
		}
		for (Iterator<QuestionProducer> iter = this.infiniteProducers.iterator(); iter.hasNext();) {
			QuestionProducer qp = iter.next();
			qp.initSession();
		}
		getStatistics().setTotal(total);
	}

	public void destroy() {
		for (Iterator<QuestionProducer> iterator1 = this.listProducers.iterator(); iterator1.hasNext();) {
			QuestionProducer qp = iterator1.next();
			qp.finishSession();
		}
		for (Iterator<QuestionProducer> iter = this.infiniteProducers.iterator(); iter.hasNext();) {
			QuestionProducer qp = iter.next();
			qp.finishSession();
		}
	}

	public List<Object> getProblemsList() {
		return this.problemsList;
	}

	public Renderer getLastRenderer() {
		return this.lastRenderer;
	}
}