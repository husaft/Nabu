package ch.unizh.ori.nabu.core;

import ch.unizh.ori.common.text.StringPresentation;
import ch.unizh.ori.nabu.ui.Renderer;
import ch.unizh.ori.nabu.voc.Mode;
import ch.unizh.ori.nabu.voc.ModeField;
import ch.unizh.ori.nabu.voc.StringColumn;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public abstract class MappingRenderer implements Renderer {

	private static final long serialVersionUID = 9158103708127838563L;

	private boolean showSolution = false;
	private boolean onlySubset = false;
	private Mode mode;
	private List<ModeField> modeFields = new ArrayList<ModeField>();

	private List<ModeField> answerModeFields = new ArrayList<ModeField>();
	private List<String> answerKeys = new ArrayList<String>();

	private String focusKey;

	private Map<String, Object> question;
	private Map<String, Object> presentedQuestion = new HashMap<String, Object>();

	private Map<String, String> userAnswer = new HashMap<String, String>();
	private Map<String, String> correctAnswer = new HashMap<String, String>();

	private boolean dirty = false;

	public MappingRenderer(Mode mode) {
		this.mode = mode;
		this.modeFields = mode.createModeFields();
		for (Iterator<ModeField> iter = this.modeFields.iterator(); iter.hasNext();) {
			ModeField mf = iter.next();
			if (mf.isAsking()) {
				this.answerModeFields.add(mf);
				this.answerKeys.add(mf.getKey());
				if (this.focusKey == null) {
					this.focusKey = mf.getKey();
				}
			}
		}
		setQuestion(null);
	}

	@SuppressWarnings("unchecked")
	public void setQuestion(Object q) {
		this.question = (Map<String, Object>) q;

		this.userAnswer.clear();

		this.correctAnswer.clear();
		this.presentedQuestion.clear();
		setShowSolution(false);
		setDirty(false);

		if (q != null) {
			for (Iterator<ModeField> iterator = this.modeFields.iterator(); iterator.hasNext();) {
				ModeField mf = iterator.next();
				String key = mf.getKey();
				Object value = this.question.get(key);
				if (mf.getPresentation() instanceof StringPresentation && mf.getColumn() instanceof StringColumn) {

					StringPresentation sp = (StringPresentation) mf.getPresentation();
					StringColumn sc = (StringColumn) mf.getColumn();
					String outText = sp.getOutText(value, sc.getTransliteration());
					this.presentedQuestion.put(key, outText);
					continue;
				}
				if (mf.getPresentation() == null) {
					this.presentedQuestion.put(key, value);
				}
			}
			for (Iterator<String> iter = getAnswerKeys().iterator(); iter.hasNext();) {
				String key = iter.next();
				Object value = ((Map<?, ?>) q).get(key);
				if (this.presentedQuestion.containsKey(key)) {
					value = this.presentedQuestion.get(key);
				}
				if (value != null && value instanceof String && ((String) value).length() > 0) {
					this.correctAnswer.put(key, (String) value);
					continue;
				}
				this.correctAnswer.remove(key);
			}
		}
	}

	public Map<String, Object> getQuestion() {
		return this.question;
	}

	public boolean isShowSolution() {
		return this.showSolution;
	}

	public void clear() {
		setQuestion(null);
	}

	public Map<String, String> getCorrectAnswer() {
		return this.correctAnswer;
	}

	public String getFocusKey() {
		return this.focusKey;
	}

	public Mode getMode() {
		return this.mode;
	}

	public boolean isOnlySubset() {
		return this.onlySubset;
	}

	public Map<String, String> getUserAnswer() {
		return this.userAnswer;
	}

	public void setCorrectAnswer(Map<String, String> map) {
		this.correctAnswer = map;
	}

	public void setFocusKey(String string) {
		this.focusKey = string;
	}

	public void setOnlySubset(boolean b) {
		this.onlySubset = b;
	}

	public void setShowSolution(boolean b) {
		this.showSolution = b;
	}

	public void setUserAnswer(Map<String, String> map) {
		this.userAnswer = map;
	}

	public List<ModeField> getModeFields() {
		return this.modeFields;
	}

	public String toString() {
		return getUserAnswer() + " vs. " + getCorrectAnswer();
	}

	public boolean isCorrect() {
		boolean ret = true;
		for (Iterator<ModeField> iter = this.answerModeFields.iterator(); iter.hasNext();) {
			ModeField m = iter.next();
			if (!correct(m)) {
				ret = false;
				break;
			}
		}
		if (!ret && !isDirty()) {
			setShowSolution(true);
		}
		setDirty(false);
		return ret;
	}

	private boolean correct(ModeField m) {
		Set<?> user = toSet(m, this.userAnswer.get(m.getKey()));
		Set<?> corr = toSet(m, this.presentedQuestion.get(m.getKey()));
		if (this.onlySubset) {
			return (user.size() >= 1 && corr.containsAll(user));
		}
		return user.equals(corr);
	}

	@SuppressWarnings("unchecked")
	private Set<String> toSet(ModeField m, Object o) {
		String str = null;
		if (o instanceof String) {
			str = (String) o;
		} else if (o instanceof ch.unizh.ori.common.text.StringText) {
			str = o.toString();
		}
		String del = ((StringColumn) m.getColumn()).getDel();
		if (del == null) {
			del = ",;/";
		}
		List<String> l = Utilities.split(str, del);
		if (l == null) {
			return (Set<String>) Collections.EMPTY_SET;
		}
		Set<String> ret = new HashSet<String>(l.size());
		for (Iterator<String> iter = l.iterator(); iter.hasNext();) {
			String s = iter.next();
			if (s != null && s.trim().length() >= 1) {
				ret.add(s.trim());
			}
		}
		return ret;
	}

	public List<String> getAnswerKeys() {
		return this.answerKeys;
	}

	private static Logger userInputLogger = Logger.getLogger(MappingRenderer.class.getName() + ".[userInput]");

	protected void process(boolean showSolution) {
		setShowSolution(showSolution);
		userInputLogger.info("getCorrectAnswer(): " + getCorrectAnswer());
		userInputLogger.info("getUserAnswer(): " + getUserAnswer());
		userInputLogger.info("isShowSolution(): " + isShowSolution());
		userInputLogger.info("getPresentedQuestion(): " + getPresentedQuestion());
	}

	protected void setUserAnswerValue(String key, String answer) {
		if (answer != null && answer.length() > 0) {
			getUserAnswer().put(key, answer);
		} else {
			getUserAnswer().remove(key);
		}
	}

	public boolean isDirty() {
		return this.dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public Map<String, Object> getPresentedQuestion() {
		return this.presentedQuestion;
	}

	public void setPresentedQuestion(Map<String, Object> presentedQuestion) {
		this.presentedQuestion = presentedQuestion;
	}
}