package ch.unizh.ori.nabu.morph;

import java.util.Arrays;

public abstract class CartesianMorphQuestion {
	protected String root;
	protected String[] coords;
	protected String form;

	public CartesianMorphQuestion(String root, String[] coords, String form) {
		this.root = root;
		this.coords = coords;
		this.form = form;
	}

	public String getForm() {
		return this.form;
	}

	public String[] getCoords() {
		return this.coords;
	}

	public String getCoord(int i) {
		return getCoords()[i];
	}

	public String getRoot() {
		return this.root;
	}

	public abstract Object getQuestion();

	public abstract String getQuestionString();

	public abstract boolean isCorrect(Object paramObject);

	public abstract String getAnswerString();

	public abstract Object getAnswer();

	public static class Analyse extends CartesianMorphQuestion {
		public Analyse(String root, String[] coords, String form) {
			super(root, coords, form);
		}

		public Object getAnswer() {
			return this.coords;
		}

		public String getAnswerString() {
			return Arrays.<Object>asList((Object[]) getAnswer()).toString();
		}

		public Object getQuestion() {
			return this.form;
		}

		public String getQuestionString() {
			return (String) getQuestion();
		}

		public boolean isCorrect(Object answer) {
			if (!(answer instanceof String[])) {
				return false;
			}
			return Arrays.equals((Object[]) this.coords, (Object[]) answer);
		}
	}

	public static class Construct extends CartesianMorphQuestion {
		public Construct(String root, String[] coords, String form) {
			super(root, coords, form);
		}

		public Object getAnswer() {
			return this.form;
		}

		public String getAnswerString() {
			return (String) getAnswer();
		}

		public Object getQuestion() {
			return this.coords;
		}

		public String getQuestionString() {
			return this.root + ": " + Arrays.<Object>asList((Object[]) getQuestion()).toString();
		}

		public boolean isCorrect(Object answer) {
			return this.form.equals(answer);
		}
	}
}