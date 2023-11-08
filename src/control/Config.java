package control;

import java.awt.Color;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.properties.EncryptableProperties;

public class Config {

	// Locale
	public static final Locale DEFAULT_LOCALE = Locale.US;

	// Config files
	public static final String DBENVFILEPATH_OLD = "data/db.env";
	public static final String DBENVFILEPATH = "data/dbenv.properties";

	// Look and feel

	// Colors
	public static final Color MAIN_COLOR = new Color(51, 40, 144);
	public static final Color CLICKED_COLOR = new Color(0, 0, 220);
	public static final Color ROLLOVER_COLOR = new Color(0, 0, 200);

	public static final Color SECONDARY_COLOR = new Color(20, 18, 67);

	public static final Color MAIN_FOREGROUND = new Color(255, 255, 255);

	public static final Color TEXTFIELD_BACKGROUND = new Color(80, 80, 80, 255);

	public static final Color PANEL_TRANSLUENT_BACKGROUND = new Color(200, 200, 200, 100);

	// Fonts
	public static final Font MAIN_FONT = new Font("Consolas", Font.PLAIN, 12);

	// Password config parameters
	public static final int MIN_PASSWORD_LENGTH = 12;
	public static final int MAX_PASSWORD_LENGTH = 24;

	public Properties loadConfigFile(String filePath, String password) {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		//TODO Default password 
		encryptor.setPassword(password);
		encryptor.setAlgorithm("PBEWithHMACSHA512AndAES_256");
		encryptor.setIvGenerator(new RandomIvGenerator());
		
		Properties properties = new EncryptableProperties(encryptor);
		try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
			properties.load(fileInputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
}
