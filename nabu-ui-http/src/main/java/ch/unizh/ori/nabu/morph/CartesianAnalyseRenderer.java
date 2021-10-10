package ch.unizh.ori.nabu.morph;

import ch.unizh.ori.nabu.ui.http.HttpRenderer;
import javax.servlet.http.HttpServletRequest;

public class CartesianAnalyseRenderer implements HttpRenderer {

	private static final long serialVersionUID = -1779382969009215765L;

	@SuppressWarnings("unused")
	private CartesianMorphQuestionProducer producer;
	private Object answer;

	public CartesianAnalyseRenderer(CartesianMorphQuestionProducer producer) {
		this.producer = producer;
	}

	public String getJspPath() {
		return "/WEB-INF/renderers/CartesianAnalyseRenderer.jsp";
	}

	public void populateFromRequest(HttpServletRequest request) {
		String ans = request.getParameter("ans");
		setAnswer((ans == null) ? "" : ans);
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

	public void clear() {
	}
}