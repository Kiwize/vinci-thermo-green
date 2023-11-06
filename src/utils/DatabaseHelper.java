package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import control.Config;

public class DatabaseHelper {

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
		
		

		BufferedReader reader;
		StringBuilder cdx = new StringBuilder();

		try {
			reader = new BufferedReader(new FileReader(Config.DBENVFILEPATH));
			String line = reader.readLine();

			while (line != null) {
				cdx.append(line).append(";");
				line = reader.readLine();
			}

			reader.close();
			
		} catch (final IOException e) {
			System.err.println("Database configuration file '" + Config.DBENVFILEPATH + "' is missing !");
			System.exit(-1);
		}

		final String[] arrc = cdx.toString().split(";");

		url = arrc[0];
		username = arrc[1];
		password = arrc[2];

		con = DriverManager.getConnection(url, username, password);
		
		activeStatements = new ArrayList<>();
		activeStatements.add(con.createStatement());
		activeStatements.add(con.createStatement());
		activeStatements.add(con.createStatement());
		
		for(int i = 0; i < STATEMENT_COUNT; i++) {
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
			for(Statement st : activeStatements) {
				st.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}
