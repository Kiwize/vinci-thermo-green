package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import control.Controller;

/**
 * Represents a mesure from a stadium.
 * 
 * <ul>
 * <li> A mesure is related to a unique zone.
 * <li> Each stadium contains multiple zones.
 * <li> A mesure contains the value in degrees farhenheit, the date and time when the mesure has been taken and the zone from the stadium.
 * </ul>
 *
 * @author Thomas PRADEAU
 * @version 3.0.0
 *
 */
public class Mesure implements IModel {

	private int id;
	
	private int numZone;
	private Date horoDate;
	private float fahrenheit;
	private String IDStadium;
	
	/**
	 * <p>
	 * Current statement use to execute queries to database.
	 * </p>
	 */
	private final Statement currentStatement = Controller.INSTANCE.getDB().getStatement(0);

	/**
	 * <p>
	 * Selects a mesure from his ID.
	 * </p>
	 * 
	 * @param id - The mesure ID
	 * 
	 * @author Thomas PRADEAU
	 * @version  3.0.0
	 */
	public Mesure(int id) {
		try {
			final ResultSet result = currentStatement.executeQuery(
					"SELECT Mesure.num_zone, Mesure.Temp, Mesure.Date_mesure, Mesure.ID_Stadium FROM Mesure WHERE Mesure.ID_mesure = "
							+ id + ";");

			if (!result.next()) {
				throw new IllegalArgumentException();
			} else {
				this.id = id;
				numZone = result.getInt("num_zone");
				fahrenheit = result.getFloat("Temp");
				horoDate = result.getDate("Date_mesure");
				IDStadium = result.getString("ID_Stadium");
			}
		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}

	public String getIDStadium() {
		return IDStadium;
	}

	public void setIDStadium(String iDStadium) {
		IDStadium = iDStadium;
	}

	public int getNumZone() {
		return numZone;
	}

	public void setNumZone(int numZone) {
		this.numZone = numZone;
	}

	public Date getHoroDate() {
		return horoDate;
	}

	public void setHoroDate(Date horoDate) {
		this.horoDate = horoDate;
	}

	public float getFahrenheit() {
		return fahrenheit;
	}

	public void setFahrenheit(float valFahrenheit) {
		fahrenheit = valFahrenheit;
	}

	/**
	 * <p>
	 * Converts farhenheit to celsius.
	 * </p>
	 * @since 3.0.0
	 * @return float celsius value.
	 */
	public float getCelsius() {
		return (fahrenheit - 32.0f) / 1.8f;
	}

	@Override
	public boolean save() {
		try {
			return currentStatement.execute("UPDATE Mesure SET Mesure.num_zone = '" + numZone + "', Mesure.Date_mesure='"
					+ horoDate + "', Mesure.Temp='" + fahrenheit + "', Mesure.ID_Stadium='" + IDStadium
					+ "' where Mesure.ID_Mesure = " + id + ";");

		} catch (final SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean insert() {
		try {
			// Format date if needed
			final DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
			return currentStatement.execute(
					"INSERT INTO Mesure (num_zone, Date_mesure, Temp, ID_Stadium) VALUES ('" + numZone + "','"
							+ format.format(horoDate) + "','" + fahrenheit + "','" + IDStadium + "')");

		} catch (final SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

}
