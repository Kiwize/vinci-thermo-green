package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import control.Controller;
import utils.BCrypt;

public class User implements IModel {

	private int id;
	private String name;
	private String password;

	private final Statement firstStatement = Controller.INSTANCE.getDB().getStatement(0);
	private final Statement thirdStatement = Controller.INSTANCE.getDB().getStatement(2);

	public User() {
	}

	public User(int id) {
		try {
			final ResultSet result = firstStatement.executeQuery(
					"SELECT AppUser.username, AppUser.password FROM AppUser WHERE AppUser.id_user = " + id + ";");

			if (!result.next()) {
				throw new IllegalArgumentException();
			} else {
				this.id = id;
				name = result.getString("username");
				password = result.getString("password");
			}
		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}

	public int getUserStadiumsCount() {
		try {
			return firstStatement.executeQuery("SELECT Count(*) FROM Stadium WHERE Stadium.ID_User = " + id + ";")
					.getInt("count");
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public void getUserStadiums(IStadiumUserMatch action) {
		try {
			final ResultSet stadiumSet = thirdStatement
					.executeQuery("SELECT Stadium.ID_Stadium FROM Stadium WHERE Stadium.id_user = " + id + ";");

			while (stadiumSet.next()) {
				action.onMatch(stadiumSet.getString("ID_Stadium"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean save() {
		try {
			return firstStatement.execute("UPDATE AppUser SET AppUser.username = '" + name + "', AppUser.password = '"
					+ password + "' where AppUser.id_user = " + id + ";");

		} catch (final SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean updatePassword(String newPassword) {
		final String encrypted = BCrypt.hashpw(newPassword, BCrypt.gensalt());
		try {
			firstStatement.execute(
					"UPDATE AppUser SET AppUser.Password = '" + encrypted + "' WHERE AppUser.id_user = " + id + ";");

			return true;
		} catch (final SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Authenticate a user, returns true if logins are correct, false otherwise.
	 * 
	 * @param username
	 * @param password
	 * @return result of the login
	 */
	public boolean authenticate(String username, String password) {
		try {
			final ResultSet result = firstStatement
					.executeQuery("SELECT AppUser.id_user, AppUser.Password FROM AppUser WHERE AppUser.username = '"
							+ username + "'");

			if (!result.next() || !BCrypt.checkpw(password, result.getString("Password"))) {
				return false;
			} else {
				this.name = username;
				this.password = password;
				this.id = result.getInt("id_user");
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public boolean insert() {
		// TODO Auto-generated method stub
		return false;
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

	public interface IStadiumUserMatch {
		void onMatch(String stadiumID);
	}
}
