package ch.unizh.ori.nabu.ui.http;

import ch.unizh.ori.nabu.core.MappingRenderer;
import ch.unizh.ori.nabu.voc.Mode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class HttpMappingRenderer extends MappingRenderer implements HttpRenderer {

	private static final long serialVersionUID = 4773689502165404194L;

	private String jspPath = "/WEB-INF/renderers/MappingRenderer.jsp";
	private Map<Object, Object> lastAnswer;

	public HttpMappingRenderer(Mode mode) {
		super(mode);
	}

	public String getJspPath() {
		return this.jspPath;
	}

	public void setJspPath(String string) {
		this.jspPath = string;
	}

	public void setQuestion(Object q) {
		super.setQuestion(q);
		if (this.lastAnswer == null) {
			this.lastAnswer = new HashMap<Object, Object>();
		} else {
			this.lastAnswer.clear();
		}
	}

	public Map<Object, Object> getLastAnswer() {
		return this.lastAnswer;
	}

	public void setLastAnswer(Map<Object, Object> map) {
		this.lastAnswer.clear();
		this.lastAnswer.putAll(map);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void processRequest(HttpServletRequest request, boolean showSolution) {
		for (Iterator<String> iter = getAnswerKeys().iterator(); iter.hasNext();) {
			String key = iter.next();
			String answer = request.getParameter("q_" + key);
			setUserAnswerValue(key, answer);
		}
		setDirty(!getUserAnswer().equals(getLastAnswer()));

		setLastAnswer((Map) getUserAnswer());
		process(showSolution);
	}
}