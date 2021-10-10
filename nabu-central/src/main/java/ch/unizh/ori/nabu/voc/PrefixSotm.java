package ch.unizh.ori.nabu.voc;

import ch.unizh.ori.common.text.DefaultScript;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PrefixSotm extends DefaultScript implements Sotm {

	private static final long serialVersionUID = -327935023497106089L;

	private List<Voice> voices = new ArrayList<Voice>();

	public String getUtterance(String toSay, Map<Object, Object> question) {
		return toSay;
	}

	public List<Voice> getVoices() {
		return this.voices;
	}

	public void addVoice(Voice v) {
		this.voices.add(v);
	}
}