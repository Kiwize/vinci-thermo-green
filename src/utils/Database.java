package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

	//TODO Move to env file
	private String url = "jdbc:mysql://192.168.122.245:3306/thermo_green_demo0";
	private String username = "_gateway";
	private String password = "dev";
	
	private Connection con;

	public Database() throws ClassNotFoundException, SQLException {
		try {
		    Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
		    e.printStackTrace();
		}
		
		this.con = DriverManager.getConnection(url, username, password);
	}
	
	public Connection getCon() {
		return con;
	}
	
	public void close()  {
		try {
			this.con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
