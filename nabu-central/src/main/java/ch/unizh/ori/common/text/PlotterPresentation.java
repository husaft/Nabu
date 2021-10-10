package ch.unizh.ori.common.text;

import ch.unizh.ori.nabu.core.DefaultDescriptable;
import ch.unizh.ori.tuppu.Plotter;

public class PlotterPresentation extends DefaultDescriptable implements Presentation {

	private static final long serialVersionUID = -1771160258035645856L;

	private Plotter plotter;
	private String className;
	private Script script;
	private String plotterId;
	private String outTransliteration;

	@SuppressWarnings("deprecation")
	public Plotter getPlotter() {
		if (this.plotter != null)
			return this.plotter;
		if (Plotter.getPlotter(getPlotterId()) != null)
			return this.plotter = Plotter.getPlotter(getPlotterId());
		try {
			return this.plotter = (Plotter) Class.forName(getClassName()).newInstance();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public void setPlotter(Plotter plotter) {
		this.plotter = plotter;
	}

	public String getPlotterId() {
		return this.plotterId;
	}

	public void setPlotterId(String plotterId) {
		this.plotterId = plotterId;
	}

	public String getOutTransliteration() {
		return this.outTransliteration;
	}

	public void setOutTransliteration(String outEnc) {
		this.outTransliteration = outEnc;
	}

	public Script getScript() {
		return this.script;
	}

	public void setScript(Script script) {
		this.script = script;
	}

	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}