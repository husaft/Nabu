package ch.unizh.ori.nabu.ui.swing;

import ch.unizh.ori.nabu.ui.Renderer;
import javax.swing.JComponent;

public interface SwingRenderer extends Renderer {

	void showSolution();

	void activate();

	JComponent getComponent();

	void setSession(NabuSession session);

	void process(boolean flag);

}