package ch.unizh.ori.nabu.input.db;

import ch.unizh.ori.nabu.voc.Source;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public abstract class DBSource extends Source<DBFieldStream> {

	private static final long serialVersionUID = -3426245963969139870L;

	private String tableName;
	private String lessonFieldName;
	private String sql;
	private MessageFormat loadSql;

	public List<DBFieldStream> readLections(URL base) throws Exception {
		List<DBFieldStream> ret = new ArrayList<DBFieldStream>();

		DataSource ds = getDataSource();
		Connection conn = ds.getConnection();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(getSql());
		int i = 0;
		while (rs.next()) {
			DBFieldStream fs = new DBFieldStream();
			String lesson = rs.getString(1);
			int count = rs.getInt(2);
			fs.setId(lesson);
			fs.setName(createLessonName(i++, lesson));
			fs.setSource(getDataSource());
			fs.setSql(formatLoadSQL(lesson));
			fs.setCount(count);
			ret.add(fs);
		}

		return ret;
	}

	protected abstract DataSource getDataSource() throws Exception;

	public String getSql() {
		if (this.sql == null) {
			this.sql = "SELECT DISTINCT " + this.lessonFieldName + ", COUNT(*) FROM " + this.tableName + " GROUP BY "
					+ this.lessonFieldName + ";";
		}

		return this.sql;
	}

	public void setLesson(String string) {
		this.lessonFieldName = string;
	}

	public void setSql(String string) {
		this.sql = string;
	}

	public void setTable(String string) {
		this.tableName = string;
	}

	protected MessageFormat getLoadSql0() {
		if (this.loadSql == null) {
			setLoadSql("SELECT * FROM {0} WHERE {1}=''{2}'';");
		}
		return this.loadSql;
	}

	protected String formatLoadSQL(String lesson) {
		MessageFormat mf = getLoadSql0();
		String[] params = { this.tableName, this.lessonFieldName, lesson };
		String ret = mf.format(params);
		return ret;
	}

	public void setLoadSql(String format) {
		this.loadSql = new MessageFormat(format);
	}
}