package ch.unizh.ori.nabu.input.db;

import javax.sql.DataSource;

public class DummyDBSource extends DBSource {

	private static final long serialVersionUID = -443744709882274591L;

	private DataSource src;

	protected DataSource getDataSource() throws Exception {
		if (this.src == null) {
			this.src = new DummyDataSource();
		}

		return this.src;
	}
}