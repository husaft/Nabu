package ch.unizh.ori.nabu.input.db;

import ch.unizh.ori.nabu.voc.AbstractFieldStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import javax.sql.DataSource;

public class DBFieldStream extends AbstractFieldStream {

	private static final long serialVersionUID = -8333362406895666319L;

	private String sql;
	private DataSource source;
	private int count;

	public Object start() throws Exception {
		Connection conn = this.source.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(this.sql);
		return rs;
	}

	public String[] next(Object param) throws Exception {
		ResultSet rs = (ResultSet) param;
		if (!rs.next()) {
			return null;
		}
		ResultSetMetaData meta = rs.getMetaData();
		String[] ret = new String[meta.getColumnCount()];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = rs.getString(i + 1);
		}
		return ret;
	}

	public void stop(Object param) throws Exception {
		ResultSet rs = (ResultSet) param;
		Connection conn = null;
		Statement st = null;
		try {
			st = rs.getStatement();
			if (st != null) {
				conn = st.getConnection();
			}
		} catch (Exception e) {
			System.err.println("DBFieldStream.stop(): " + e.getMessage());
		}
		rs.close();
		if (st != null) {
			st.close();
		}
		if (conn != null) {
			conn.close();
		}
	}

	public DataSource getSource() {
		return this.source;
	}

	public String getSql() {
		return this.sql;
	}

	public void setSource(DataSource source) {
		this.source = source;
	}

	public void setSql(String string) {
		this.sql = string;
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(int i) {
		this.count = i;
	}
}