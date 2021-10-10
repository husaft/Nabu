package ch.unizh.ori.nabu.voc;

import ch.unizh.ori.nabu.core.Central;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class VocabularyExport {
	public static void main(String[] args) throws IOException {
		String dirname = args[0];
		export(dirname, args[1], args[2], new OutputStreamWriter(System.out));
	}

	public static void export(String dirname, String id, String colString, Writer out) throws IOException {
		Central c = new Central();
		c.readDir(dirname);
		Vocabulary voc = c.getVocs().get(id);
		List<Map<String, Object>> l = voc.createVocList(voc.getLections());
		String[] cols = colString.split(",");
		for (Iterator<Map<String, Object>> iter = l.iterator(); iter.hasNext();) {
			Map<String, Object> m = iter.next();
			for (int i = 0; i < cols.length; i++) {
				Object v = m.get(cols[i]);
				v = (v == null) ? "" : v.toString();
				out.write((String) v);
				out.write("\t");
			}
			out.write("\n");
		}
	}
}