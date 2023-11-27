package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import control.Controller;

/**
* Represents a stadium.
* 
* <ul>
* <li> A stadium is related to a unique user and to multiples mesures.
* <li> It contains a name and the ID of the user who manage it.
* </ul>
*
* @author Thomas PRADEAU
* @version 3.0.0
*
*/
public class Stadium implements IModel {

	private String id;
	private String name;
	private String maintenanceNumber;
	private int id_user;

	private final Statement currentStatement = Controller.INSTANCE.getDB().getStatement(0);
	private final Statement secondStatement = Controller.INSTANCE.getDB().getStatement(1);

	/**
	 * <p>
	 * Selects a stadium from his ID.
	 * </p>
	 * 
	 * @param id - The stadium ID
	 * 
	 * @author Thomas PRADEAU
	 * @version 3.0.0
	 */
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

	/**
	 * <p>
	 * Selects distincts zones from the stadium.
	 * </p>
	 * 
	 * @return An arraylist that contains the zones.
	 * 
	 * @author Thomas PRADEAU
	 * @version 3.0.0
	 */
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

	/**
	 * <p>
	 * Select all mesures from the stadium.
	 * </p>
	 * 
	 * @param action - A callback method which describes what to do on every result.
	 * 
	 * @author Thomas PRADEAU
	 * @version 3.0.0
	 */
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

	public String getContactNumber() {
		return maintenanceNumber;
	}

}
