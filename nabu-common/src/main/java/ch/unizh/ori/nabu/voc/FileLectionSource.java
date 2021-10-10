package ch.unizh.ori.nabu.voc;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FileLectionSource extends AbstractFileSource {

	private static final long serialVersionUID = -551301867753659838L;

	private int column = -1;

	@SuppressWarnings("unchecked")
	protected void read(BufferedReader in) throws IOException {
		Map<Object, Object> map = new TreeMap<Object, Object>();
		List<String> keyList = new ArrayList<String>();
		for (String line = in.readLine(); line != null; line = in.readLine()) {
			String[] fields = line.split("\t");

			String key = lesson(fields);
			List<String[]> voc = (List<String[]>) map.get(key);
			if (voc == null) {
				voc = new ArrayList<String[]>();
				map.put(key, voc);
				keyList.add(key);
			}
			voc.add(fields);
		}
		in.close();
		this.fieldstreams = new ArrayList<FieldStream>(keyList.size());
		int i = 0;
		for (Iterator<String> iter = keyList.iterator(); iter.hasNext();) {
			String key = iter.next();
			List<String[]> voc = (List<String[]>) map.get(key);
			ListFieldStream fs = new ListFieldStream(voc);
			fs.setId(key);
			fs.setName(createLessonName(i++, key));
			this.fieldstreams.add(fs);
		}
	}

	protected String lesson(String[] fields) {
		int column = this.column;
		if (column < 0) {
			column += fields.length;
		}
		if (column < 0 || column >= fields.length) {
			column = fields.length - 1;
		}
		String key = fields[column];
		return key;
	}

	public void setColumn(String str) {
		this.column = Integer.parseInt(str);
	}
}