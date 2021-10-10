package ch.unizh.ori.nabu.ui;

import ch.unizh.ori.nabu.core.QuestionIterator;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DefaultRendererChooser extends RendererChooser implements Serializable {
	private static final long serialVersionUID = -8724146653579158656L;

	private Map<Object, Object> rendererMap = new HashMap<Object, Object>();

	public Map<Object, Object> getRendererMap() {
		return this.rendererMap;
	}

	public void setRendererMap(Map<Object, Object> rendererMap) {
		this.rendererMap = rendererMap;
	}

	public void put(Object key, Object renderer) {
		getRendererMap().put(key, renderer);
	}

	public Renderer chooseRenderer(QuestionIterator iter) {
		Renderer r = (Renderer) getRendererMap().get(iter.getProducer());
		if (r == null) {
			Object q = iter.getQuestion();
			if (q != null) {
				r = (Renderer) getRendererMap().get(q.getClass());
			}
		}
		return r;
	}
}