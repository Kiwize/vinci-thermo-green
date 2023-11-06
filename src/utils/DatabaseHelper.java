package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import control.Config;

public class DatabaseHelper {

	private Config cfg;

	private String url = "";
	private String username = "";
	private String password = "";

	private final Connection con;

	private ArrayList<Statement> activeStatements;
	private final int STATEMENT_COUNT = 3;

	public DatabaseHelper() throws ClassNotFoundException, SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		}
		this.cfg = new Config();

		Properties properties = cfg.loadConfigFile(Config.DBENVFILEPATH);

		url = properties.getProperty("db.url");
		username = properties.getProperty("db.username");
		password = properties.getProperty("db.password");

		con = DriverManager.getConnection(url, username, password);

		activeStatements = new ArrayList<>();
		activeStatements.add(con.createStatement());
		activeStatements.add(con.createStatement());
		activeStatements.add(con.createStatement());

		for (int i = 0; i < STATEMENT_COUNT; i++) {
			activeStatements.add(con.createStatement());
		}
	}

	public Connection getCon() {
		return con;
	}

	public Statement getStatement(int statementID) {
		return activeStatements.get(statementID);
	}

	public void close() {
		try {
			con.close();
		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}

	public void closeStatements() {
		try {
			for (Statement st : activeStatements) {
				st.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
