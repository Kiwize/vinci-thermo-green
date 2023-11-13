/**
 * @author J�r�me Valenti
 */
package control;

import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.EnglishSequenceData;
import org.passay.IllegalSequenceRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

import config.Config;
import config.ConfigManager;
import model.Mesure;
import model.Stadium;
import model.User;
import utils.DatabaseHelper;
import view.ConsoleGUI;
import view.LoginView;
import view.PasswordChangeView;

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

	public static Controller INSTANCE;

	private ResourceBundle rc;

	private ConfigManager cfgManager;
	private DatabaseHelper db;

	private float overflowMin;
	private float overflowMax;
	
	private HashMap<String, String> LOCALE_TAG_MAP;

	// Views
	private final ConsoleGUI consoleGui;
	private final LoginView loginView;
	private final PasswordChangeView passchview;

	// Models
	private ArrayList<Mesure> mesures = new ArrayList<>();
	private final ArrayList<Stadium> stadiums = new ArrayList<>();
	private User user;

	private final PasswordValidator passwordValidator;

	public Controller() throws ParseException {
		cfgManager = new ConfigManager(Config.DBENVFILEPATH);

		rc = ResourceBundle.getBundle("locale/locale",
				Locale.forLanguageTag(cfgManager.getProperties().getProperty("locale.preferred")));

		Controller.INSTANCE = this;
		passwordValidator = new PasswordValidator(
				new LengthRule(Config.MIN_PASSWORD_LENGTH, Config.MAX_PASSWORD_LENGTH),
				new CharacterRule(EnglishCharacterData.LowerCase, 1),
				new CharacterRule(EnglishCharacterData.UpperCase, 1), new CharacterRule(EnglishCharacterData.Digit, 1),
				new CharacterRule(EnglishCharacterData.Special, 1),
				new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 4, false),
				new IllegalSequenceRule(EnglishSequenceData.Numerical, 4, false),
				new IllegalSequenceRule(EnglishSequenceData.USQwerty, 4, false), new WhitespaceRule());

		try {
			db = new DatabaseHelper(cfgManager);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		LOCALE_TAG_MAP = new HashMap<>();
		LOCALE_TAG_MAP.put(Locale.US.toLanguageTag(), Locale.US.getDisplayLanguage());
		LOCALE_TAG_MAP.put(Locale.FRANCE.toLanguageTag(), Locale.FRANCE.getDisplayLanguage());
		
		user = new User();
		consoleGui = new ConsoleGUI(this);
		loginView = new LoginView(this);
		passchview = new PasswordChangeView(this);
		loginView.setVisible(true);
	}

	/**
	 * Submit and verify credentials supplied by the user.
	 *
	 * @param username
	 * @param password
	 */
	public void submitLogins(String username, String password) {
		if (user.authenticate(username, password)) {
			loginView.setVisible(false);

			gatherUserStadiums();
			consoleGui.prepareDisplay();

			// Asks for connection windows to close.
			loginView.dispatchEvent(new WindowEvent(loginView, WindowEvent.WINDOW_CLOSING));
			passchview.dispatchEvent(new WindowEvent(loginView, WindowEvent.WINDOW_CLOSING));
		} else {
			JOptionPane.showMessageDialog(loginView.getComponent(0), rc.getString("loginViewInvalidCredentials"),
					rc.getString("loginViewError"), JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Asks controller to show password change view if the logins provided by the
	 * user are correct.
	 * 
	 * @param username
	 * @param password
	 */
	public void submitPasswordChange(String username, String password) {
		if (user.authenticate(username, password)) {
			loginView.setVisible(false);
			passchview.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(loginView.getComponent(0), rc.getString("loginViewInvalidCredentials"),
					rc.getString("loginViewError"), JOptionPane.ERROR_MESSAGE);
		}
	}

	private void gatherUserStadiums() {
		user.getUserStadiums((stadiumID) -> {
			Stadium stadium = new Stadium(stadiumID);
			stadiums.add(stadium);
			stadium.getMesures((mesureID) -> {
				mesures.add(new Mesure(mesureID));
			});
		});
	}

	public void updateDisplayedLocale(Locale newLocale) {
		if (!cfgManager.updateSettings("locale.preferred", newLocale.toLanguageTag()))
			System.err.println("Error updating config file !");
		rc = ResourceBundle.getBundle("locale/locale",
				Locale.forLanguageTag(cfgManager.getProperties().getProperty("locale.preferred")));

		consoleGui.updateComponentsText();
		
		consoleGui.invalidate();
		consoleGui.validate();
		consoleGui.repaint();
	}

	public void changePassword(String password, String confirm) {
		if (password.equals(confirm)) {
			final PasswordData passwordData = new PasswordData(password);
			final RuleResult validate = passwordValidator.validate(passwordData);

			if (!validate.isValid()) {
				JOptionPane.showMessageDialog(loginView.getComponent(0), "Mot de passe non conforme !",
						rc.getString("loginViewError"), JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (user.updatePassword(password)) {
				JOptionPane.showMessageDialog(loginView.getComponent(0), "Mot de passe changé avec succés !",
						"Mot de passe modifié.", JOptionPane.INFORMATION_MESSAGE);

				gatherUserStadiums();
				consoleGui.prepareDisplay();

				loginView.dispatchEvent(new WindowEvent(loginView, WindowEvent.WINDOW_CLOSING));
				passchview.dispatchEvent(new WindowEvent(loginView, WindowEvent.WINDOW_CLOSING));
			}
		} else {
			JOptionPane.showMessageDialog(loginView.getComponent(0), "Les mots de passe sont différents !",
					rc.getString("loginViewError"), JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Filtre la collection des mesures en fonction des param&egrave;tres :
	 * <li>la zone (null = toutes les zones)</li>
	 * <li>la date de d&eacute;but (null = &agrave; partir de l'origine)</li>
	 * <li>la date de fin (null = jusqu'&agrave; la fin)<br />
	 */
	// public void filtrerLesMesure(String laZone, Date leDebut, Date lafin) {
	public ArrayList<Mesure> filterMesures(String laZone) {
		final ArrayList<Mesure> laSelection = new ArrayList<>();
		for (final Mesure mesure : mesures) {
			if ((laZone.compareTo("*") == 0) || (Integer.parseInt(laZone) == mesure.getNumZone())) {
				laSelection.add(mesure);
			}
		}
		return laSelection;
	}

	/**
	 * Update overflow status by replacing the button wich represent the overflow
	 * status. If temps are outside the user define temp range, the button become
	 * red, green otherwise.
	 * 
	 * Temps that are outside the range are displayed in the scroll table in the
	 * console view.
	 */
	public void updateOverflowStatus() {
		if (overflowMax > overflowMin) {
			for (Mesure mesure : mesures) {
				if (mesure.getIDStadium().equals(consoleGui.getCurrentStadium().getStadiumID())) {
					consoleGui.setAlertIcon(new ImageIcon("img/s_green_button.png"));

					// Temp outside range
					if (mesure.getCelsius() < overflowMin || mesure.getCelsius() > overflowMax) {
						consoleGui.setAlertIcon(new ImageIcon("img/s_red_button.png"));

						// TODO Display temp in red in the table and/or in the graph
					}
				}
			}
		}

	}

	public HashMap<EPasswordError, Boolean> passwordFieldUpdate(String password, String confirmation) {
		final HashMap<EPasswordError, Boolean> errsBuffer = new HashMap<>();
		final String specialChars = "/*!@#$%^&*()\"{}_[]|\\?/<>,.";

		for (final EPasswordError error : EPasswordError.values()) {
			errsBuffer.put(error, true);
		}

		if (password.equals(confirmation)) {
			errsBuffer.replace(EPasswordError.SIMILAR_PASSWORDS, false);
		}

		if (password.length() >= Config.MIN_PASSWORD_LENGTH) {
			errsBuffer.replace(EPasswordError.MIN_LENGTH, false);
		}

		for (int i = 0; i < password.length(); i++) {
			final char c = password.charAt(i);

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

	public void quit() {
		db.closeStatements();
		db.close();
	}

	/**
	 * Retourne la collection des mesures
	 * 
	 * @return ArrayList<Mesure>
	 */
	public ArrayList<Mesure> getMesures() {
		return mesures;
	}

	public void setMesures(ArrayList<Mesure> mesures) {
		this.mesures = mesures;
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

	public ArrayList<Stadium> getStadiums() {
		return stadiums;
	}

	public PasswordChangeView getPasswordChangeView() {
		return passchview;
	}

	public ConfigManager getConfigManager() {
		return cfgManager;
	}

	/**
	 * Password errors callback enumeration That allows to define error messages for
	 * each password requirement.
	 */
	public enum EPasswordError {
		// TODO, translate errors messages
		SIMILAR_PASSWORDS("Les mots de passes sont différents", (PasswordChangeView frame, boolean state) -> {
			frame.getChkboxSimilarPasswords().setSelected(state);
		}),

		REQUIRE_UPPERCASE("Une majuscule requise", (PasswordChangeView frame, boolean state) -> {
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

		private final String textError;
		private final IPasswordCallback callbackError;

		EPasswordError(String textError, IPasswordCallback callbackError) {
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
