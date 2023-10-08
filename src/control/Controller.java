/**
 * @author J�r�me Valenti 
 */
package control;

import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import model.Mesure;
import model.Stadium;
import model.User;
import utils.BCrypt;
import utils.DatabaseHelper;
import view.ConsoleGUI;
import view.LoginView;
import view.StadiumManagerView;

/**
 * <p>
 * Le cont&ocirc;lleur :
 * </p>
 * <ol>
 * <li>lit les mesures de temp�rature dans un fichier texte</li>
 * <li>retourne la collection des mesures<br />
 * </li>
 * </ol>
 * 
 * @author J�r�me Valenti
 * @version 2.0.0
 *
 */
public class Controller {

	public static final Locale DEFAULT_LOCALE = Locale.US;
	public static Controller INSTANCE;

	private ResourceBundle rc;
	private Locale locale;

	private DatabaseHelper db;
	
	private float overflowMin;
	private float overflowMax;

	// Views
	private ConsoleGUI consoleGui;
	private LoginView loginView;
	private StadiumManagerView smv;

	// Models
	private ArrayList<Mesure> mesures = new ArrayList<>();
	private ArrayList<Stadium> stadiums = new ArrayList<>();
	private User user;
	
	public Controller() throws ParseException {
		this.locale = Locale.getDefault();
		this.rc = ResourceBundle.getBundle("locale/locale", DEFAULT_LOCALE);

		// Load mesures from database
		try {
			this.db = new DatabaseHelper();
			Controller.INSTANCE = this;

			Statement st = db.getCon().createStatement();
			ResultSet set = st.executeQuery("SELECT Mesure.ID_Mesure FROM Mesure;");

			while (set.next()) {
				mesures.add(new Mesure(set.getInt("ID_Mesure")));
			}

			set = st.executeQuery("SELECT Stadium.ID_Stadium FROM Stadium;");

			while (set.next()) {
				stadiums.add(new Stadium(set.getString("ID_Stadium")));
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		this.consoleGui = new ConsoleGUI(this);
		this.loginView = new LoginView(this);
		loginView.setVisible(true);
		
		
	}

	/**
	 * Submit and verify credentials supplied by the user.
	 * 
	 * @param username
	 * @param password
	 */
	public void submitLogins(String username, String password) {
		// String fileData = FileUtils.readTxtFile("data/logins.txt"); //TODO Replace
		// with database access to check credentials

		try {
			Statement st = db.getCon().createStatement();
			ResultSet result = st
					.executeQuery("SELECT AppUser.id_user, AppUser.Password FROM AppUser WHERE AppUser.username = '"
							+ username + "'");

			if (!result.next()) {
				JOptionPane.showMessageDialog(loginView.getComponent(0),
						this.rc.getString("loginViewInvalidCredentials"), this.rc.getString("loginViewError"),
						JOptionPane.ERROR_MESSAGE);
			} else {
				if (BCrypt.checkpw(password, result.getString("Password"))) {
					user = new User(result.getInt("id_user"));
					loginView.setVisible(false);
					
					consoleGui.setCurrentStadium(this.getUserStadiums().get(0));
					consoleGui.updateTable();
					consoleGui.updateGraph();
					consoleGui.setVisible(true);
					
					this.smv = new StadiumManagerView(this);
					this.smv.setVisible(true);
					loginView.dispatchEvent(new WindowEvent(loginView, WindowEvent.WINDOW_CLOSING));
					
					
				} else {
					JOptionPane.showMessageDialog(loginView.getComponent(0),
							this.rc.getString("loginViewInvalidCredentials"), this.rc.getString("loginViewError"),
							JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updateConsoleView(Stadium stadium) {
		consoleGui.setCurrentStadium(stadium);
		consoleGui.updateTable();
		consoleGui.updateGraph();
	}

	public ArrayList<Stadium> getUserStadiums() {
		ArrayList<Stadium> sts = new ArrayList<>();

		for (Stadium st : stadiums) {
			if (st.getUserID() == user.getId()) {
				sts.add(st);
			}
		}

		return sts;
	}

	/**
	 * Filtre la collection des mesures en fonction des param&egrave;tres :
	 * <li>la zone (null = toutes les zones)</li>
	 * <li>la date de d&eacute;but (null = &agrave; partir de l'origine)</li>
	 * <li>la date de fin (null = jusqu'&agrave; la fin)<br />
	 */
	// public void filtrerLesMesure(String laZone, Date leDebut, Date lafin) {
	public ArrayList<Mesure> filtrerLesMesure(String laZone) {
		// Parcours de la collection
		// Ajout � laSelection des objets qui correspondent aux param�tres
		// Envoi de la collection
		ArrayList<Mesure> laSelection = new ArrayList<Mesure>();
		for (Mesure mesure : mesures) {
			if (laZone.compareTo("*") == 0) {
				laSelection.add(mesure);
			} else {
				if (Integer.parseInt(laZone) == (mesure.getNumZone())) {
					laSelection.add(mesure);
				}
			}
		}
		return laSelection;
	}
	
	public void updateOverflowStatus() {
		
		ArrayList<Mesure> currentMesures = Mesure.getMesuresFromStadiumID(consoleGui.getCurrentStadium().getStadiumID());
		
		System.out.println(overflowMin);
		System.out.println(overflowMax);
		
		if(overflowMax < overflowMin) {
			System.err.println("Inconsistant values.");
			return;
		}
		
		for(Mesure mesure : currentMesures) {
			if(mesure.getCelsius() < overflowMin || mesure.getCelsius() > overflowMax) {
				System.out.println("Température anormale !");
			}
		}
	}
	
	public void quit() {
		db.close();
		smv.dispatchEvent(new WindowEvent(loginView, WindowEvent.WINDOW_CLOSING));
	}

	/**
	 * <p>
	 * Retourne la collection des mesures
	 * </p>
	 * 
	 * @return ArrayList<Mesure>
	 */
	public ArrayList<Mesure> getMesures() {
		return mesures;
	}

	public void setMesures(ArrayList<Mesure> mesures) {
		this.mesures = mesures;
	}

	public Locale getLocale() {
		return locale;
	}

	public ResourceBundle getResourceBundle() {
		return rc;
	}

	public User getUser() {
		return user;
	}
	
	public DatabaseHelper getDB() {
		return db;
	}
	
	public float getOverflowMax() {
		return overflowMax;
	}
	
	public float getOverflowMin() {
		return overflowMin;
	}
	
	public void setOverflowMax(float overflowMax) {
		this.overflowMax = overflowMax;
	}
	
	public void setOverflowMin(float overflowMin) {
		this.overflowMin = overflowMin;
	}
}
