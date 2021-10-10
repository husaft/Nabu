package ch.unizh.ori.nabu.voc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public abstract class AbstractFileSource extends Source<FieldStream> {

	private static final long serialVersionUID = -190688403979663194L;

	protected List<FieldStream> fieldstreams;

	public List<FieldStream> readLections(URL base) throws UnsupportedEncodingException, IOException {
		this.fieldstreams = new ArrayList<FieldStream>();
		URL url = new URL(base, getSrc());
		Logger.getLogger(AbstractFileSource.class).info(url);
		System.out.println(url);
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), getEnc()));

		read(in);
		return this.fieldstreams;
	}

	protected abstract void read(BufferedReader reader) throws IOException;
}