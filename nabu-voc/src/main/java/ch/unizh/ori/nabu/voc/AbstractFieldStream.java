package ch.unizh.ori.nabu.voc;

import ch.unizh.ori.nabu.core.DefaultDescriptable;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class AbstractFieldStream extends DefaultDescriptable implements FieldStream {

	private static final long serialVersionUID = 6452853115320400225L;

	public abstract Object start() throws Exception;

	public abstract int getCount();

	public abstract String[] next(Object obj) throws Exception;

	public abstract void stop(Object obj) throws Exception;

	public Collection<Object> getCollection() throws Exception {
		return new SourceCollectionAndIterator(this);
	}

	private static class SourceCollectionAndIterator extends AbstractCollection<Object> implements Iterator<Object> {
		private FieldStream fs;

		private Object param;

		private String[] next;

		public SourceCollectionAndIterator(FieldStream fs) throws Exception {
			this.fs = fs;
			this.param = fs.start();
		}

		public int size() {
			System.err.println("SourceCollectionAndIterator.size()");
			throw new RuntimeException("We don't implement this!");
		}

		public Iterator<Object> iterator() {
			return this;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public boolean hasNext() {
			if (this.next != null) {
				return true;
			}
			return nextIntern();
		}

		private boolean nextIntern() {
			try {
				this.next = this.fs.next(this.param);
				if (this.next == null) {
					this.fs.stop(this.param);
					return false;
				}
				return true;
			} catch (Exception ex) {
				throw new RuntimeException(ex);
			}
		}

		public Object next() {
			if (hasNext()) {
				Object ret = this.next;
				this.next = null;
				return ret;
			}
			throw new NoSuchElementException();
		}
	}
}