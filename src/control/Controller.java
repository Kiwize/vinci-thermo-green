/**
 * @author J�r�me Valenti 
 */
package control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.Mesure;
import view.ConsoleGUI;

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

	private ConsoleGUI gui;
	
	/**
	 * Les mesures lues dans le fichier des relev�s de temp�ratures
	 */
	private ArrayList<Mesure> lesMesures = new ArrayList<Mesure>();
	
	public Controller() throws ParseException {
		//TODO Replace CSV data
		lireCSV("data/mesures.csv");
		
		this.gui = new ConsoleGUI(this);
	}

	/**
	 * Lit un fichier de type CSV (Comma Separated Values)
	 * Le fichier contient les mesures de temp&eacute;rature de la pelouse.
	 * @author J�r�me Valenti
	 * @return
	 * @throws ParseException
	 * @since 2.0.0
	 */
	public void lireCSV(String filePath) throws ParseException {

		try {
			File f = new File(filePath);
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);

			try {
				// Chaque ligne est un enregistrement de donn�es
				String records = br.readLine();

				// Chaque enregistrement contient des champs
				String[] fields = null;
				String numZone = null;
				Date horoDate = null;
				float fahrenheit;

				while (records != null) {
					// Affecte les champs de l'enregistrement courant dans un
					// tableau de chaine
					fields = records.split(";");

					// Affecte les champs aux param�tre du constructeur de
					// mesure
					numZone = fields[0];
					horoDate = strToDate(fields[1]);
					fahrenheit = Float.parseFloat(fields[2]);

					// Instancie une Mesure
					Mesure laMesure = new Mesure(numZone, horoDate, fahrenheit);
					lesMesures.add(laMesure);

					// Enregistrement suivant
					records = br.readLine();
				}

				br.close();
				fr.close();
			} catch (IOException exception) {
				System.out.println("Erreur lors de la lecture : " + exception.getMessage());
			}
		} catch (FileNotFoundException exception) {
			System.out.println("Le fichier n'a pas �t� trouv�");
		}
	}

	/**
	 * <p>
	 * Filtre la collection des mesures en fonction des param&egrave;tres :
	 * </p>
	 * <ol>
	 * <li>la zone (null = toutes les zones)</li>
	 * <li>la date de d&eacute;but (null = &agrave; partir de l'origine)</li>
	 * <li>la date de fin (null = jusqu'&agrave; la fin)<br />
	 * </li>
	 * </ol>
	 */
	// public void filtrerLesMesure(String laZone, Date leDebut, Date lafin) {
	public ArrayList<Mesure> filtrerLesMesure(String laZone) {
		// Parcours de la collection
		// Ajout � laSelection des objets qui correspondent aux param�tres
		// Envoi de la collection
		ArrayList<Mesure> laSelection = new ArrayList<Mesure>();
		for (Mesure mesure : lesMesures) {
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
	public ArrayList<Mesure> getLesMesures() {

		return lesMesures;
	}

	/**
	 * <p>
	 * Convertion d'une String en Date
	 * </p>
	 * 
	 * @param strDate
	 * @return Date
	 * @throws ParseException
	 */
	private Date strToDate(String strDate) throws ParseException {

		SimpleDateFormat leFormat = null;
		Date laDate = new Date();
		leFormat = new SimpleDateFormat("yy-MM-dd kk:mm");

		laDate = leFormat.parse(strDate);
		return laDate;
	}
}
