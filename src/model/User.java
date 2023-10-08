package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import control.Controller;
import utils.DatabaseHelper;

public class User implements IModel{

	private int id;
	private String name;
	private String password;

	public User(int id) {
		try {
			Statement st = Controller.INSTANCE.getDB().getCon().createStatement();
			ResultSet result = st.executeQuery(
					"SELECT AppUser.username, AppUser.password FROM AppUser WHERE AppUser.id_user = " + id + ";");

			if (!result.next()) {
				this.id = id;
				this.name = null;
				this.password = null;
			} else {
				this.id = id;
				this.name = result.getString("username");
				this.password = result.getString("password");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public User(String name, String password, int id) {
		this.name = name;
		this.password = password;
		this.id = id;
	}

	public boolean save() {
		try {
			Statement st = Controller.INSTANCE.getDB().getCon().createStatement();
			boolean res =  st.execute("UPDATE AppUser SET AppUser.username = '" + this.name + "', AppUser.password = '"
					+ this.password + "' where AppUser.id_user = " + this.id + ";");

			return res;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public int getId() {
		return id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean insert() {
		// TODO Auto-generated method stub
		return false;
	}

}
