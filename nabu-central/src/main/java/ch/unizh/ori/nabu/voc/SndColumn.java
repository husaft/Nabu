package ch.unizh.ori.nabu.voc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SndColumn extends AbstractColumn implements Sotm {

	private static final long serialVersionUID = -2145935108892864858L;

	private String ad;
	private List<Voice> voices = new ArrayList<Voice>();

	public Object map(String[] arr) {
		if (getColumn() >= arr.length) {
			return null;
		}
		return arr[getColumn()];
	}

	public String getUtterance(String toSay, Map<Object, Object> question) {
		return (String) question.get(getId());
	}

	public void addVoice(Voice v) {
		this.voices.add(v);
	}

	public String getAd() {
		return this.ad;
	}

	public void setAd(String string) {
		this.ad = string;
	}

	public List<Voice> getVoices() {
		return this.voices;
	}
}