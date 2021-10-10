package ch.unizh.ori.nabu.voc;

import java.io.BufferedReader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class StringSource extends Source {

	private static final long serialVersionUID = -7007376049172488175L;

	private String theVoc;

	public StringSource(String theVoc) {
		this.theVoc = theVoc;
	}

	@SuppressWarnings({ "unchecked" })
	public List readLections(URL base) throws Exception {
		List fieldstreams = new ArrayList();
		EmptyLineSource.read(new BufferedReader(new StringReader(this.theVoc)), fieldstreams, this);
		return fieldstreams;
	}
}