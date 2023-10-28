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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JSlider;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.EnglishSequenceData;
import org.passay.IllegalSequenceRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

import model.Mesure;
import model.Stadium;
import model.User;
import utils.BCrypt;
import utils.DatabaseHelper;
import view.ConsoleGUI;
import view.LoginView;
import view.PasswordChangeView;
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
	private PasswordChangeView passchview;

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
		this.passchview = new PasswordChangeView(this);
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
					consoleGui.populateStadiumDropdown(getUserStadiums());

					// this.smv = new StadiumManagerView(this);
					// this.smv.setVisible(true);
					loginView.dispatchEvent(new WindowEvent(loginView, WindowEvent.WINDOW_CLOSING));
					passchview.dispatchEvent(new WindowEvent(loginView, WindowEvent.WINDOW_CLOSING));

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

	public void submitPasswordChange(String username, String password) {
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

					passchview.setVisible(true);
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

	public void changePassword(String password, String confirm) {
		if (password.equals(confirm)) {
			PasswordValidator passwordValidator = new PasswordValidator(
					new LengthRule(Config.MIN_PASSWORD_LENGTH, Config.MAX_PASSWORD_LENGTH),
					new CharacterRule(EnglishCharacterData.LowerCase, 1),
					new CharacterRule(EnglishCharacterData.UpperCase, 1),
					new CharacterRule(EnglishCharacterData.Digit, 1),
					new CharacterRule(EnglishCharacterData.Special, 1),
					new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 4, false),
					new IllegalSequenceRule(EnglishSequenceData.Numerical, 4, false),
					new IllegalSequenceRule(EnglishSequenceData.USQwerty, 4, false), new WhitespaceRule());
			PasswordData passwordData = new PasswordData(password);
			RuleResult validate = passwordValidator.validate(passwordData);

			String errors = "";

			for (String msg : passwordValidator.getMessages(validate)) {
				errors += msg += "\n";
			}

			if (!validate.isValid()) {
				JOptionPane.showMessageDialog(loginView.getComponent(0), "Mot de passe non conforme !",
						this.rc.getString("loginViewError"), JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (user.updatePassword(password)) {

				JOptionPane.showMessageDialog(loginView.getComponent(0), "Mot de passe changé avec succés !",
						"Mot de passe modifié.", JOptionPane.INFORMATION_MESSAGE);

				consoleGui.setCurrentStadium(this.getUserStadiums().get(0));
				consoleGui.updateTable();
				consoleGui.updateGraph();
				consoleGui.setVisible(true);
				consoleGui.populateStadiumDropdown(getUserStadiums());

				// this.smv = new StadiumManagerView(this);
				// this.smv.setVisible(true);

				loginView.dispatchEvent(new WindowEvent(loginView, WindowEvent.WINDOW_CLOSING));
				passchview.dispatchEvent(new WindowEvent(loginView, WindowEvent.WINDOW_CLOSING));

			} else {
				JOptionPane.showMessageDialog(loginView.getComponent(0), "An error as occured",
						this.rc.getString("loginViewError"), JOptionPane.ERROR_MESSAGE);
			}

		} else {
			JOptionPane.showMessageDialog(loginView.getComponent(0), "Les mots de passe sont différents !",
					this.rc.getString("loginViewError"), JOptionPane.ERROR_MESSAGE);
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
		ArrayList<Mesure> currentMesures = Mesure
				.getMesuresFromStadiumID(consoleGui.getCurrentStadium().getStadiumID());

		if (overflowMax < overflowMin) {
			return;
		}

		consoleGui.setAlertIcon(new ImageIcon("img/s_green_button.png"));

		for (Mesure mesure : currentMesures) {
			if (mesure.getCelsius() < overflowMin || mesure.getCelsius() > overflowMax) {
				consoleGui.setAlertIcon(new ImageIcon("img/s_red_button.png"));
			}
		}
	}

	public void quit() {
		db.close();
		// smv.dispatchEvent(new WindowEvent(loginView, WindowEvent.WINDOW_CLOSING));
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

	public PasswordChangeView getPasswordChangeView() {
		return passchview;
	}

	public HashMap<EPasswordError, Boolean> passwordFieldUpdate(String password, String confirmation) {
		HashMap<EPasswordError, Boolean> errsBuffer = new HashMap<>();
		String specialChars = "/*!@#$%^&*()\"{}_[]|\\?/<>,.";

		for (EPasswordError error : EPasswordError.values()) {
			errsBuffer.put(error, true);
		}

		if (password.equals(confirmation)) {
			errsBuffer.replace(EPasswordError.SIMILAR_PASSWORDS, false);
		}

		if (password.length() >= Config.MIN_PASSWORD_LENGTH) {
			errsBuffer.replace(EPasswordError.MIN_LENGTH, false);
		}

		for (int i = 0; i < password.length(); i++) {
			char c = password.charAt(i);

			if (Character.isUpperCase(c)) {
				errsBuffer.replace(EPasswordError.REQUIRE_UPPERCASE, false);
			}

			if (Character.isLowerCase(c)) {
				errsBuffer.replace(EPasswordError.REQUIRE_LOWERCASE, false);
			}

			if (Character.isDigit(c)) {
				errsBuffer.replace(EPasswordError.REQUIRE_DIGIT, false);
			}

			if (specialChars.contains(password.substring(i, i + 1))) {
				errsBuffer.replace(EPasswordError.REQUIRE_SPECIAL, false);
			}
		}

		passchview.showPasswordStatus(errsBuffer);

		return errsBuffer;
	}

	public boolean isPasswordValid(HashMap<String, Boolean> errsBitBuffer) {
		for (Map.Entry<String, Boolean> errSet : errsBitBuffer.entrySet()) {
			if (errSet.getValue())
				return false;
		}

		return true;
	}

	public enum EPasswordError {
		// TODO, translate errors messages
		SIMILAR_PASSWORDS("Les mots de passes sont différents", (PasswordChangeView frame, boolean state) -> {
			frame.getChkboxSimilarPasswords().setSelected(state);
		}), REQUIRE_UPPERCASE("Une majuscule requise", (PasswordChangeView frame, boolean state) -> {
			frame.getChkboxMajuscule().setSelected(state);
		}), REQUIRE_LOWERCASE("Une minuscule requise", (PasswordChangeView frame, boolean state) -> {
			frame.getChkboxMinuscule().setSelected(state);
		}), REQUIRE_SPECIAL("Un caractère spécial requis", (PasswordChangeView frame, boolean state) -> {
			frame.getChkboxSpecialChar().setSelected(state);
		}), REQUIRE_DIGIT("Un chiffre requis", (PasswordChangeView frame, boolean state) -> {
			frame.getChkboxDigit().setSelected(state);
		}), MIN_LENGTH("Le mot de passe doit faire " + Config.MIN_PASSWORD_LENGTH + " caractères minimum",
				(PasswordChangeView frame, boolean state) -> {
					frame.getChkboxMinLength().setSelected(state);
				});

		private String textError;
		private IPasswordCallback callbackError;

		private EPasswordError(String textError, IPasswordCallback callbackError) {
			this.textError = textError;
			this.callbackError = callbackError;
		}

		public String getTextError() {
			return textError;
		}

		public IPasswordCallback getCallbackError() {
			return callbackError;
		}
	}
}
