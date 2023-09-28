/**
 * @author J�r�me Valenti
 */
package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import utils.DatabaseHelper;

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
	 * <p>
	 * numZone contient le num�ro de la zone mesur�e
	 * </p>
	 */
	private int numZone;
	/**
	 * <p>
	 * horoDate contient la date et l'heure de la mesure au format aa-mm-jj hh:mm
	 * </p>
	 */
	private Date horoDate;
	/**
	 * <p>
	 * valFahrenheit contient la valeur de la temp�rature mesur�e en degr�
	 * Fahrenheit
	 * </p>
	 */
	private float fahrenheit;

	private String IDStadium;

	public Mesure(int id) {
		try {
			DatabaseHelper database = new DatabaseHelper();

			Statement st = database.getCon().createStatement();
			ResultSet result = st.executeQuery(
					"SELECT Mesure.num_zone, Mesure.Temp, Mesure.Date_mesure, Mesure.ID_Stadium FROM Mesure WHERE Mesure.ID_mesure = "
							+ id + ";");

			if (!result.next()) {
				this.id = id;
				this.numZone = -1;
				this.fahrenheit = -255.0f;
				this.horoDate = new Date();
				this.IDStadium = "STD01";
			} else {
				this.id = id;
				this.numZone = result.getInt("num_zone");
				this.fahrenheit = result.getFloat("Temp");
				this.horoDate = result.getDate("Date_mesure");
				this.IDStadium = result.getString("ID_Stadium");
			}

			database.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public Mesure() {
		this.numZone = 0;
		this.horoDate = new Date();
		this.fahrenheit = 0.0f;
		this.IDStadium = "STD01";
	}

	public Mesure(int pZone, Date pDate, float pFahrenheit) {
		this.numZone = pZone;
		this.horoDate = pDate;
		this.fahrenheit = pFahrenheit;
		this.IDStadium = "STD01";
	}
	
	public Mesure(int pZone, Date pDate, float pFahrenheit, String stadeID) {
		this.numZone = pZone;
		this.horoDate = pDate;
		this.fahrenheit = pFahrenheit;
		this.IDStadium = stadeID;
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
		this.fahrenheit = valFahrenheit;
	}

	/**
	 * <p>
	 * Convertit Fahrenheit en �Celsius
	 * </p>
	 * 
	 * @since 2.0.0
	 * @return float t�Celsius
	 */
	public float getCelsius() {
		// return (float) (valFahrenheit - 32) / 1.8)
		return (fahrenheit - 32.0f) / 1.8f;
	}

	@Override
	public boolean save() {
		try {
			DatabaseHelper database = new DatabaseHelper();
			Statement st = database.getCon().createStatement();
			
			boolean res = st.execute("UPDATE Mesure SET Mesure.num_zone = '" + this.numZone + "', Mesure.Date_mesure='"
					+ this.horoDate + "', Mesure.Temp='" + this.fahrenheit + "', Mesure.ID_Stadium='" + this.IDStadium
					+ "' where Mesure.ID_Mesure = " + this.id + ";");

			database.close();
			return res;

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean insert() {
		try {
			DatabaseHelper database = new DatabaseHelper();

			//Format date if needed
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
			Statement st = database.getCon().createStatement();
			
			
			boolean res = st.execute(
					"INSERT INTO Mesure (num_zone, Date_mesure, Temp, ID_Stadium) VALUES ('" + this.numZone + "','"
							+ format.format(this.horoDate) + "','" + this.fahrenheit + "','" + this.IDStadium + "')");

			database.close();
			return res;

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
