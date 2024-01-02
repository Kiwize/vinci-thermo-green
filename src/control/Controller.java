package control;

import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
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
 * The main controller :
 * <ol>
 * <li> Initializes views and manage main program's functionalities.
 * </ol>
 * 
 * @author Thomas PRADEAU
 * @version 3.0.0
 */
public class Controller {

	//TODO Remove static controller instance
	public static Controller INSTANCE;

	/**
	 * Resource bundle use for internationalization.
	 */
	private ResourceBundle rc;

	private ConfigManager cfgManager;
	private DatabaseHelper db;

	private float overflowMin;
	private float overflowMax;
	
	private HashMap<String, String> LOCALE_TAG_MAP;
	
	private SMSSender smsSender;

	// Views
	private final ConsoleGUI consoleGui;
	private final LoginView loginView;
	private final PasswordChangeView passchview;

	// Models
	private ArrayList<Mesure> mesures = new ArrayList<>();
	private final ArrayList<Stadium> stadiums = new ArrayList<>();
	private User user;

	/**
	 * <p>
	 * PasswordValidator is a class provided by the <b>Passay</b> library.
	 * This class specifies different rules for passwords to be valids. This class
	 * is used for the password change use case.
	 * </p>
	 */
	private final PasswordValidator passwordValidator;

	public Controller() throws ParseException {
		cfgManager = new ConfigManager(Config.DBENVFILEPATH);
		
		smsSender = new SMSSender(this);
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
	 * <p>
	 * Submit and verify credentials supplied by the user.
	 * </p>
	 *
	 * @param username
	 * @param password
	 * 
	 * @author Thomas PRADEAU
	 * @version 3.0.0
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
	 * <p>
	 * Asks controller to show password change view if the logins provided by the
	 * user are correct.
	 * </p>
	 * 
	 * @param username
	 * @param password
	 * 
	 * @author Thomas PRADEAU
	 * @version 3.0.0
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

	/**
	 * <p>
	 * Get stadiums managed by the current logged user.
	 * </p>
	 * 
	 * @author Thomas PRADEAU
	 * @version 3.0.0
	 */
	private void gatherUserStadiums() {
		user.getUserStadiums((stadiumID) -> {
			Stadium stadium = new Stadium(stadiumID);
			stadiums.add(stadium);
			stadium.getMesures((mesureID) -> {
				mesures.add(new Mesure(mesureID));
			});
		});
	}

	/**
	 * <p>
	 * Updates displayed locale. 
	 * </p>
	 * 
	 * @param newLocale - The new locale to display.
	 * 
	 * @author Thomas PRADEAU
	 * @version 3.0.0
	 */
	/*
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

	/**
	 * <p>
	 * Change user password, asks for authentication first.
	 * </p>
	 * 
	 * @param password - New password of the user who needs to change his password
	 * @param confirm - Confirmation of the previous password
	 * 
	 * @author Thomas PRADEAU
	 * @version 3.0.0
	 */
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
	 * <p>
	 * Filter mesures by stadium zones.
	 * A stadium contains multiple zones.
	 * </p>
	 * 
	 * @param zoneFilter - The zone filter.
	 * @return The array list that contains the filtered mesures.
	 * 
	 * @author Thomas PRADEAU
	 * @version 3.0.0
	 */
	public ArrayList<Mesure> filterMesures(String zoneFilter) {
		final ArrayList<Mesure> laSelection = new ArrayList<>();
		for (final Mesure mesure : mesures) {
			if ((zoneFilter.compareTo("*") == 0) || (Integer.parseInt(zoneFilter) == mesure.getNumZone())) {
				laSelection.add(mesure);
			}
		}
		return laSelection;
	}

	/**
	 * <p>
	 * Update overflow status by replacing the button wich represent the overflow
	 * status. If temps are outside the user define temp range, the button become
	 * red, green otherwise.
	 * </p>
	 * <p>
	 * Temps that are outside the range are displayed in the scroll table in the
	 * console view.
	 * </p>
	 * 
	 * @author Thomas PRADEAU
	 * @version 3.0.0
	 */
	public void updateOverflowStatus() {
		try {
			if (overflowMax > overflowMin) {
				for (Mesure mesure : mesures) {
					if (mesure.getIDStadium().equals(consoleGui.getCurrentStadium().getStadiumID())) {
						consoleGui.setAlertIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/img/s_green_button.png"))));

						// Temp outside range
						if (mesure.getCelsius() < overflowMin || mesure.getCelsius() > overflowMax) {
							consoleGui.setAlertIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/img/s_red_button.png"))));

							// TODO Display temp in red in the table and/or in the graph
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * <p>
	 * This method verifies the new password provided by the user from the PasswordChangeView class and
	 * check if this password respects passwords strength specifications from the <b>passwordValidator</b> object.
	 * </p>
	 * 
	 * 
	 * @param password
	 * @param confirmation
	 * @return
	 */
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

	/**
	 * <p>
	 * Quit the application by closing the database connection and the displayed views.
	 * </p>
	 * 
	 * @author Thomas PRADEAU
	 * @version 3.0.0
	 */
	public void quit() {
		db.closeStatements();
		db.close();
	}

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
	
	public ConsoleGUI getConsoleGui() {
		return consoleGui;
	}
	
	public SMSSender getSmsSender() {
		return smsSender;
	}

	/**
	 * <p>
	 * Password errors callback enumeration That allows to define error messages for
	 * each password requirement.
	 * </p>
	 * <p>
	 * Each password errors redefine the callback method from the <b>IPasswordCallback</b> interface.
	 * </p>
	 * 
	 * @author Thomas PRADEAU
	 * @version 3.0.0
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
