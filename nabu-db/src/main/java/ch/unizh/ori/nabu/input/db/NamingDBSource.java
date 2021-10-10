package ch.unizh.ori.nabu.input.db;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class NamingDBSource extends DBSource {

	private static final long serialVersionUID = -4101921020465632215L;

	private DataSource dataSource;
	private String res = "jdbc/NabuDB";

	protected DataSource getDataSource() throws Exception {
		if (this.dataSource == null) {
			try {
				Context initCtx = new InitialContext();
				Context envCtx = (Context) initCtx.lookup("java:comp/env");
				this.dataSource = (DataSource) envCtx.lookup(getRes());
			} catch (Exception e) {
			}
		}

		return this.dataSource;
	}

	public String getRes() {
		return this.res;
	}

	public void setDataSource(DataSource source) {
		this.dataSource = source;
	}

	public void setRes(String string) {
		this.res = string;
	}
}