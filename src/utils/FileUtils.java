package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.Mesure;

public class FileUtils {

	/**
	 * Lit un fichier de type CSV (Comma Separated Values)
	 * Le fichier contient les mesures de temp&eacute;rature de la pelouse.
	 * @author J�r�me Valenti
	 * @return
	 * @throws ParseException
	 * @since 2.0.0
	 */
	public static ArrayList<Mesure> lireCSV(String filePath, ArrayList<Mesure> mesures) throws ParseException {

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
					mesures.add(laMesure);

					// Enregistrement suivant
					records = br.readLine();
				}

				br.close();
				fr.close();
				
				return mesures;
			} catch (IOException exception) {
				System.out.println("Erreur lors de la lecture : " + exception.getMessage());
				return null;
			}
		} catch (FileNotFoundException exception) {
			System.out.println("Le fichier n'a pas �t� trouv�");
			return null;
		}
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
	public static Date strToDate(String strDate) throws ParseException {

		SimpleDateFormat leFormat = null;
		Date laDate = new Date();
		leFormat = new SimpleDateFormat("yy-MM-dd kk:mm");

		laDate = leFormat.parse(strDate);
		return laDate;
	}
	
	public static String readTxtFile(String path) {
		StringBuilder builder = new StringBuilder();
		
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))))){
			String line = "";
			while((line = reader.readLine()) != null) {
				builder.append(line).append("\n");
			}
		} catch (IOException e) {
			System.err.println("Couldn't load file : " + path);
		}
		
		return builder.toString();
	}
	
}
