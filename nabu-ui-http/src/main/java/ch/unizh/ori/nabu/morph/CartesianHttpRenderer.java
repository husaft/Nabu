package ch.unizh.ori.nabu.morph;

import ch.unizh.ori.nabu.ui.http.HttpRenderer;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public abstract class CartesianHttpRenderer implements HttpRenderer {

	private static final long serialVersionUID = 8020847311791724844L;

	private CartesianMorphQuestionProducer producer;

	public CartesianHttpRenderer(CartesianMorphQuestionProducer producer) {
		this.producer = producer;
	}

	public String[] getCoordinateNames() {
		return this.producer.getCoordinateNames();
	}

	public String getCoordinateName(int i) {
		return getCoordinateNames()[i];
	}

	public String[][] getSpace() {
		return this.producer.getSpace();
	}

	public String getCoordinateLabel(int i) {
		return this.producer.getCoordinateLabel(i);
	}

	public String getValueLabel(int i, int j) {
		return this.producer.getValueLabel(i, j);
	}

	public String[] getCoordinates() {
		return getQuestion().getCoords();
	}

	private static CartesianMorphQuestion getQuestion() {
		return null;
	}

	public String getCoordinate(int i) {
		return getCoordinates()[i];
	}

	public String getRoot() {
		return getQuestion().getRoot();
	}

	public String getForm() {
		return getQuestion().getForm();
	}

	public static class Analyse extends CartesianHttpRenderer {

		private static final long serialVersionUID = -1103189980826838722L;

		private Answer answer = new Answer(null, new HashMap<Object, Object>());

		public Analyse(CartesianMorphQuestionProducer producer) {
			super(producer);
		}

		@SuppressWarnings("unchecked")
		public void copyAnswer() {
			CartesianMorphQuestion q = CartesianHttpRenderer.getQuestion();
			this.answer.ansRoot = q.getRoot();
			for (int i = (getCoordinateNames()).length - 1; i >= 0; i--) {
				this.answer.ansCoord.put(getCoordinateName(i), q.getCoord(i));
			}
		}

		public void clear() {
			this.answer.ansRoot = null;
			this.answer.ansCoord.clear();
		}

		@SuppressWarnings("unchecked")
		public void populateFromRequest(HttpServletRequest request) {
			String newAnsRoot = request.getParameter("ansRoot");
			this.answer.ansRoot = (newAnsRoot == null) ? "" : newAnsRoot;

			for (int i = (getCoordinateNames()).length - 1; i >= 0; i--) {
				String n = getCoordinateName(i);
				String nc = request.getParameter("ans_" + n);
				nc = (nc == null) ? "" : nc;
				this.answer.ansCoord.put(n, nc);
			}
		}

		public String getJspPath() {
			return "/WEB-INF/renderers/CartesianAnalyseRenderer.jsp";
		}

		public boolean isCorrect(Object question) {
			CartesianMorphQuestion q = (CartesianMorphQuestion) question;
			if (!q.getRoot().equals(getRoot()))
				return false;
			for (int i = (getCoordinateNames()).length - 1; i >= 0; i--) {
				String c = getCoordinateName(i);
				String a = (String) this.answer.ansCoord.get(c);
				if (c == null || !a.equals(q.getCoord(i)))
					return false;
			}
			return true;
		}

		public String getAnsRoot() {
			return this.answer.ansRoot;
		}

		public String getAnsCoord(String coordName) {
			return (String) this.answer.ansCoord.get(coordName);
		}

		public String getFocusKey() {
			return "ansRoot";
		}

		public Object getAnswer() {
			return this.answer.copy();
		}

		public static class Answer {
			private String ansRoot;
			@SuppressWarnings("rawtypes")
			private Map ansCoord;

			@SuppressWarnings("rawtypes")
			public Answer(String ansRoot, Map ansCoord) {
				this.ansRoot = ansRoot;
				this.ansCoord = ansCoord;
			}

			public Answer(Answer a) {
				this.ansRoot = a.ansRoot;
				this.ansCoord = a.ansCoord;
			}

			public boolean equals(Object o) {
				if (!(o instanceof Answer))
					return false;
				Answer a = (Answer) o;
				if ((this.ansRoot != null) ? (a.ansRoot == null) : this.ansRoot.equals(a.ansRoot))
					return false;
				return (this.ansCoord == null) ? ((a.ansCoord == null)) : this.ansCoord.equals(a.ansCoord);
			}

			@SuppressWarnings("unchecked")
			public Answer copy() {
				return new Answer(this.ansRoot, new HashMap<Object, Object>(this.ansCoord));
			}
		}

		public boolean isCorrect() {
			return false;
		}

		public void processRequest(HttpServletRequest request, boolean showSolution) {
		}

		public void setQuestion(Object q) {
		}

		public boolean isShowSolution() {
			return false;
		}

		public void afterTest() {
		}
	}

	public static class Construct extends CartesianHttpRenderer {

		private static final long serialVersionUID = 3656025111783483985L;

		private Object answer;

		public Construct(CartesianMorphQuestionProducer producer) {
			super(producer);

			this.answer = null;
		}

		public String getJspPath() {
			return "/WEB-INF/renderers/CartesianConstructRenderer.jsp";
		}

		public void populateFromRequest(HttpServletRequest request) {
			setAnswer(request.getParameter("ans"));
		}

		public void clear() {
			setAnswer(null);
		}

		public void copyAnswer() {
			setAnswer(CartesianHttpRenderer.getQuestion().toString());
		}

		public Object getAnswer() {
			return this.answer;
		}

		public void setAnswer(Object answer) {
			this.answer = answer;
		}

		public boolean isCorrect() {
			return false;
		}

		public void processRequest(HttpServletRequest request, boolean showSolution) {
		}

		public String getFocusKey() {
			return null;
		}

		public void setQuestion(Object q) {
		}

		public boolean isShowSolution() {
			return false;
		}

		public void afterTest() {
		}
	}
}