package ch.unizh.ori.nabu.voc;

import java.net.URL;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("rawtypes")
public class EmptySource extends Source {

	private static final long serialVersionUID = -8936193915843284904L;

	public List<?> readLections(URL url) {
		return Collections.EMPTY_LIST;
	}

}