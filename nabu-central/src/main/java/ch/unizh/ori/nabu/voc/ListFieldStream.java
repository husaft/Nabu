package ch.unizh.ori.nabu.voc;

import java.util.Collection;
import java.util.Iterator;

public class ListFieldStream extends AbstractFieldStream {

	private static final long serialVersionUID = 5187397902608492011L;

	private Collection<String[]> items;

	public ListFieldStream(Collection<String[]> items) {
		this.items = items;
	}

	public Object start() throws Exception {
		return this.items.iterator();
	}

	@SuppressWarnings("unchecked")
	public String[] next(Object param) throws Exception {
		Iterator<String[]> iter = (Iterator<String[]>) param;
		if (iter.hasNext()) {
			return iter.next();
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public void stop(Object param) throws Exception {
		Iterator<Object> iter = (Iterator<Object>) param;
		iter = null;
	}

	public int getCount() {
		if (this.items == null) {
			return 0;
		}
		return this.items.size();
	}
}