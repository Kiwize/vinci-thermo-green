package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import control.Controller;
import utils.DatabaseHelper;

public class Stadium implements IModel {

	private String id;
	private String name;
	private int id_user;

	public Stadium(String id) {
		try {
			Statement st = Controller.INSTANCE.getDB().getCon().createStatement();
			ResultSet result = st.executeQuery(
					"SELECT Stadium.ID_Stadium, Stadium.nom_stade, Stadium.id_user FROM Stadium WHERE Stadium.ID_Stadium = '"
							+ id + "';");

			if (!result.next()) {
				this.id = id;
				this.name = "default";
				this.id_user = 0;
			} else {
				this.id = id;
				this.name = result.getString("nom_stade");
				this.id_user = result.getInt("id_user");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Stadium(String ID_Stadium, String nom_stade, int id_user) {
		this.id = ID_Stadium;
		this.name = nom_stade;
		this.id_user = id_user;
	}

	@Override
	public boolean save() {
		try {
			Statement st = Controller.INSTANCE.getDB().getCon().createStatement();

			boolean res = st.execute("UPDATE Stadium SET Stadium.nom_stade='" + this.name + "', Stadium.id_user='"
					+ this.id_user + "'" + "' where Stadium.ID_Stadium = " + this.id + ";");

			return res;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean insert() {
		try {
			Statement st = Controller.INSTANCE.getDB().getCon().createStatement();
			boolean res = st.execute("INSERT INTO Stadium (ID_Stadium, nom_stade, id_user) VALUES ('" + this.id + "', '"
					+ this.name + "','" + this.id_user + "')");

			return res;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
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

}
