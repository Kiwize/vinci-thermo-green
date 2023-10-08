package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import control.Config;

public class DatabaseHelper {

	private String url = "";
	private String username = "";
	private String password = "";

	private Connection con;

	public DatabaseHelper() throws ClassNotFoundException, SQLException {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		BufferedReader reader;
		String cdx = "";

		try {
			reader = new BufferedReader(new FileReader(Config.DBENVFILEPATH));
			String line = reader.readLine();

			while (line != null) {
				cdx += line + ";";
				line = reader.readLine();
			}

			reader.close();
		} catch (IOException e) {
			System.err.println("Database configuration file '" + Config.DBENVFILEPATH + "' is missing !");
			System.exit(-1);
		}

		String[] arrc = cdx.split(";");

		url = arrc[0];
		username = arrc[1];
		password = arrc[2];

		this.con = DriverManager.getConnection(url, username, password);
	}

	public Connection getCon() {
		return con;
	}

	public void close() {
		try {
			this.con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
