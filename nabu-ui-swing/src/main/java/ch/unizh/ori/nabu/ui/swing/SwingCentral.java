package ch.unizh.ori.nabu.ui.swing;

import ch.unizh.ori.nabu.core.Central;

public class SwingCentral extends Central {

	private static final long serialVersionUID = 4966558019003803224L;

	private static SwingCentral sw;

	public static SwingCentral getDefault() {
		if (sw == null) {
			sw = new SwingCentral();
		}
		return sw;
	}

	public void readFiles() {
		readDir("etc");
	}
}