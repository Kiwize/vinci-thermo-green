package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import control.Controller;

public class Stadium implements IModel {

	private String id;
	private String name;
	private int id_user;

	private final Statement currentStatement = Controller.INSTANCE.getDB().getStatement(0);
	private final Statement secondStatement = Controller.INSTANCE.getDB().getStatement(1);

	public Stadium(String id) {
		try {
			final ResultSet result = currentStatement.executeQuery(
					"SELECT Stadium.ID_Stadium, Stadium.nom_stade, Stadium.id_user FROM Stadium WHERE Stadium.ID_Stadium = '"
							+ id + "';");

			if (!result.next()) {
				throw new IllegalArgumentException();
			} else {
				this.id = id;
				name = result.getString("nom_stade");
				id_user = result.getInt("id_user");
			}

		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean save() {
		try {
			return currentStatement.execute("UPDATE Stadium SET Stadium.nom_stade='" + name + "', Stadium.id_user='" + id_user + "'"
					+ "' where Stadium.ID_Stadium = " + id + ";");

		} catch (final SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean insert() {
		try {
			return currentStatement.execute("INSERT INTO Stadium (ID_Stadium, nom_stade, id_user) VALUES ('" + id + "', '" + name
					+ "','" + id_user + "')");

		} catch (final SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public ArrayList<String> getZones() {
		try {
			final ResultSet res = currentStatement
					.executeQuery("SELECT DISTINCT Mesure.num_zone FROM Mesure WHERE Mesure.ID_Stadium = '" + id + "'");

			final ArrayList<String> data = new ArrayList<>();

			while (res.next()) {
				data.add(res.getString("num_zone"));
			}

			return data;
		} catch (final SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void getMesures(IMesureStadiumMatch action) {
		try {
			final ResultSet res = secondStatement
					.executeQuery("SELECT Mesure.ID_Mesure FROM Mesure WHERE Mesure.ID_Stadium = '" + id + "'");

			while (res.next()) {
				action.onMatch(res.getInt("ID_Mesure"));
			}

		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}

	public String getStadiumID() {
		return id;
	}

	public int getUserID() {
		return id_user;
	}

	public String getStadiumName() {
		return name;
	}

	public interface IMesureStadiumMatch {
		void onMatch(int mesureID);
	}

}
