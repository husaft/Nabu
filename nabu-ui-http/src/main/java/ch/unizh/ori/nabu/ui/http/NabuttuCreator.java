package ch.unizh.ori.nabu.ui.http;

import ch.unizh.ori.nabu.voc.FieldStream;
import ch.unizh.ori.nabu.voc.Mode;
import ch.unizh.ori.nabu.voc.ModeField;
import ch.unizh.ori.nabu.voc.Vocabulary;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NabuttuCreator {
	@SuppressWarnings("rawtypes")
	private Map ret;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map gatherData(String id, File vocFile, Vocabulary voc) throws Exception {
		this.ret = new HashMap<Object, Object>();
		List<String[]> types = new ArrayList<String[]>();
		for (Iterator<String> iter = voc.getModes().keySet().iterator(); iter.hasNext();) {
			Object key = iter.next();
			Mode m = (Mode) voc.getModes().get(key);
			String[] s = new String[3];
			s[0] = m.getName();
			StringBuffer a = new StringBuffer();
			StringBuffer b = new StringBuffer();
			for (Iterator<ModeField> iterator = m.createModeFields().iterator(); iterator.hasNext();) {
				ModeField mf = iterator.next();

				StringBuffer c = mf.isAsking() ? b : a;
				if (c.length() > 0)
					c.append(',');
				c.append(mf.getColumn().getColumn());
			}
			s[1] = a.toString();
			s[2] = b.toString();
			types.add(s);
		}
		outputToRet(types, "vocs/" + id + "/" + id + "_types.txt", 3);

		List<String> names = new ArrayList<String>();
		List<List> vocs = new ArrayList();
		int maxFrameLen = splitIntoLists(
				new BufferedReader(new InputStreamReader(new FileInputStream(vocFile), "UTF-8")), vocs, true);
		int i;
		for (i = 0; i < vocs.size(); i++) {
			String name2 = id + "/" + id + "_" + (i + 1) + ".bvoc";
			outputToRet(vocs.get(i), "vocs/" + name2, maxFrameLen);
			names.add(name2);
		}

		for (i = 0; i < names.size(); i++) {
			String lesson = String.valueOf(i + 1);
			lesson = ((FieldStream) voc.getLections().get(i)).getName();
			String[] s = { lesson, names.get(i) };
			names.set(i, s[0]); // TODO
		}
		outputToRet(names, "vocs/" + id + "/" + id + "_list.txt", 2);

		String name = "index.b";
		String[] header = { voc.getName(), id };

		outputToRet(Collections.singletonList(header), name, 2);
		return this.ret;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void outputToRet(List list, String name, int maxFrameLen) throws Exception {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		writeTable(list, new BufferedOutputStream(bout), maxFrameLen);

		bout.flush();
		this.ret.put(name, new ByteArrayInputStream(bout.toByteArray()));
	}

	@SuppressWarnings("rawtypes")
	public static int splitIntoLists(Reader in1, List<List> vocs, boolean multi) throws IOException {
		BufferedReader in = new BufferedReader(in1);
		List<String[]> voc = new ArrayList<String[]>();
		vocs.add(voc);

		int maxFrameLen = 0;
		String line;
		while ((line = in.readLine()) != null) {
			if (multi && line.trim().length() == 0) {
				voc = new ArrayList<String[]>();
				vocs.add(voc);
				continue;
			}
			String[] fields = line.split("\\t");
			maxFrameLen = Math.max(maxFrameLen, fields.length);
			voc.add(fields);
		}

		return maxFrameLen;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void writeTable(List list, OutputStream out1, int maxFrameLen) {
		DataOutputStream out = null;
		try {
			out = new DataOutputStream(out1);
			out.writeInt(list.size());
			out.writeInt(maxFrameLen);
			for (Iterator<String[]> iter = list.iterator(); iter.hasNext();) {
				String[] element = iter.next();
				int i;
				for (i = 0; i < element.length; i++) {
					out.writeUTF(element[i]);
				}
				for (i = 0; i < maxFrameLen - element.length; i++) {
					out.writeUTF("");
				}
			}
			out.flush();
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
		}
	}
}