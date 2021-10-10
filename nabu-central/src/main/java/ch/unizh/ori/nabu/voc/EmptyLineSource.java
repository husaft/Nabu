package ch.unizh.ori.nabu.voc;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmptyLineSource extends AbstractFileSource {

	private static final long serialVersionUID = 3261729134951815793L;

	protected void read(BufferedReader in) throws IOException {
		read(in, this.fieldstreams, this);
	}

	public static void read(BufferedReader in, List<FieldStream> fieldstreams, Source<FieldStream> src)
			throws IOException {
		List<String[]> voc = new ArrayList<String[]>();
		ListFieldStream fs = null;
		boolean startingLesson = true;
		int index = 1;
		int defaultLesson = 1;
		for (String line = ""; line != null; line = in.readLine()) {
			if (line.length() > 0 && line.charAt(0) == '﻿') {
				line = line.substring(1);
			}
			if (line.trim().length() == 0) {
				voc = new ArrayList<String[]>();
				fs = new ListFieldStream(voc);
				fs.setId(String.valueOf(index));
				fs.setName(src.createLessonName(defaultLesson - 1, String.valueOf(defaultLesson)));
				fieldstreams.add(fs);
				index++;
				defaultLesson++;
				startingLesson = true;
			} else if (line.charAt(0) == '#') {
				if (line.startsWith("##")) {
					src.addHeaderString(line.substring(2));
				} else if (startingLesson) {
					fs.setName(line.substring(1).trim());
					defaultLesson--;
					startingLesson = false;
				}

			} else {

				String[] fields = line.split("\t");
				voc.add(fields);
			}
		}
		in.close();
	}
}