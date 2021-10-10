package ch.unizh.ori.nabu.voc;

public class FileDictionarySource extends FileLectionSource {

	private static final long serialVersionUID = 3288661519631910339L;

	protected String lesson(String[] fields) {
		String lesson = super.lesson(fields);
		if (lesson != null && lesson.length() >= 1) {
			lesson = lesson.substring(0, 1).toLowerCase();
		}
		return lesson;
	}

}