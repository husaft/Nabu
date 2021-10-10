package ch.unizh.ori.nabu.input.db;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

public class DummyDataSource implements DataSource {
	static {
		init();
	}

	public static void init() {
		try {
			String driverName = "org.gjt.mm.mysql.Driver";
			Class.forName(driverName);
		} catch (ClassNotFoundException e) {
		}
	}

	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	public void setLoginTimeout(int seconds) throws SQLException {
	}

	public PrintWriter getLogWriter() throws SQLException {
		return new PrintWriter(System.err);
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
	}

	public Connection getConnection() throws SQLException {
		return getConnection("nabu_admin", "mypassword");
	}

	public Connection getConnection(String username, String password) throws SQLException {
		Connection connection = null;

		try {
			String url = "jdbc:mysql://localhost:3306/nabu?autoReconnect=true";
			connection = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> arg0) throws SQLException {
		return null;
	}
}