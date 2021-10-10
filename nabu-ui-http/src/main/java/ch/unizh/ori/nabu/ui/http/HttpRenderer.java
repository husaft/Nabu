package ch.unizh.ori.nabu.ui.http;

import ch.unizh.ori.nabu.ui.Renderer;
import javax.servlet.http.HttpServletRequest;

public interface HttpRenderer extends Renderer {

	String getJspPath();

	void processRequest(HttpServletRequest paramHttpServletRequest, boolean paramBoolean);

	String getFocusKey();

}