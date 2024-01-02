package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import config.ConfigManager;

public class DatabaseHelper {

	private String url = "";
	private String username = "";
	private String password = "";
	
	private final Connection con;

	private ArrayList<Statement> activeStatements;
	private final int STATEMENT_COUNT = 3;

	public DatabaseHelper(ConfigManager configManager) throws ClassNotFoundException, SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		}

		url = configManager.getProperties().getProperty("db.url");
		username = configManager.getProperties().getProperty("db.username");
		password = configManager.getProperties().getProperty("db.password");
		System.out.println(password);

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
