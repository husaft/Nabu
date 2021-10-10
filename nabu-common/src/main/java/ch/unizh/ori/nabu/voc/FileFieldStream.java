package ch.unizh.ori.nabu.voc;

import ch.unizh.ori.nabu.core.Utilities;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

public class FileFieldStream extends AbstractFieldStream {

	private static final long serialVersionUID = -3902028934252678202L;

	private URL url;
	private String enc;
	private String seperator;
	private int count = -1;

	public Object start() throws Exception {
		BufferedReader in;
		if (this.enc == null) {
			in = new BufferedReader(new InputStreamReader(this.url.openStream()));
		} else {
			in = new BufferedReader(new InputStreamReader(this.url.openStream(), this.enc));
		}
		return in;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String[] next(Object param) throws Exception {
		BufferedReader in = (BufferedReader) param;
		String line = in.readLine();
		if (line == null) {
			return null;
		}

		List dummy = Utilities.split(line, this.seperator, null);
		return (String[]) dummy.toArray((Object[]) new String[dummy.size()]);
	}

	public void stop(Object param) throws Exception {
		BufferedReader in = (BufferedReader) param;
		in.close();
	}

	public String getEnc() {
		return this.enc;
	}

	public String getSeperator() {
		return this.seperator;
	}

	public URL getUrl() {
		return this.url;
	}

	public void setEnc(String string) {
		this.enc = string;
	}

	public void setSeperator(String string) {
		this.seperator = string;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(int i) {
		this.count = i;
	}
}