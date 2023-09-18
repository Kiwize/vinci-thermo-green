/**
 * @author J�r�me Valenti 
 */
package control;

import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.JFrame;

import model.Mesure;
import utils.FileUtils;
import view.ConsoleGUI;
import view.LoginView;

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

	//Views
	private ConsoleGUI consoleGui;
	private LoginView loginView;
	
	//Models
	private ArrayList<Mesure> mesures = new ArrayList<Mesure>();
	
	public Controller() throws ParseException {
		//TODO Replace CSV data by database
		FileUtils.lireCSV("data/mesures.csv", mesures);
		
		this.consoleGui = new ConsoleGUI(this);
		this.loginView = new LoginView(this);
		
		loginView.setVisible(true);
	}
	
	/**
	 * Submit and verify credentials supplied by the user.
	 * @param username
	 * @param password
	 */
	public void submitLogins(String username, String password) {
		String fileData = FileUtils.readTxtFile("data/logins.txt"); //TODO Replace with database access to check credentials
		String[] credentials = fileData.split("\n");
		
		if(username.equals(credentials[0]) && password.equals(credentials[1])) {
			System.out.println("Access granted !");
			loginView.setVisible(false);
			consoleGui.setVisible(true);
		} else {
			System.err.println("Invalid username and password !");
		}
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
				if (laZone.compareTo(mesure.getNumZone()) == 0) {
					laSelection.add(mesure);
				}
			}
		}
		return laSelection;
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
}
