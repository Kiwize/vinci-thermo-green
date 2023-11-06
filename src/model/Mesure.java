/**
 * @author J�r�me Valenti
 */
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
 * <p>
 * Des capteurs mesure r�guli�rement la temp�rature de la pelouse.
 * </p>
 * <p>
 * Pour chaque capteur :
 * </p>
 * <ul>
 * <li>les mesures sont donn&eacute;es en degr&eacute; Fahrenheit;<br />
 * </li>
 * <li>localis&eacute;es par le d&eacute;coupage du terrain en zones;<br />
 * </li>
 * <li>horadat&eacute;ee par la date et l'heure.<br />
 * </li>
 * </ul>
 *
 * @author jvalenti
 * @version 2.0.0
 *
 */
public class Mesure implements IModel {

	private int id;

	/**
	 *numZone contient le num�ro de la zone mesur�e
	 */
	private int numZone;
	/**
	 * horoDate contient la date et l'heure de la mesure au format aa-mm-jj hh:mm
	 */
	private Date horoDate;
	/**
	 * valFahrenheit contient la valeur de la temp�rature mesur�e en degr�
	 * Fahrenheit
	 */
	private float fahrenheit;
	private String IDStadium;
	private final Statement currentStatement = Controller.INSTANCE.getDB().getStatement(0);

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
	 * Convertit Fahrenheit en �Celsius
	 * @since 2.0.0
	 * @return float t�Celsius
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
