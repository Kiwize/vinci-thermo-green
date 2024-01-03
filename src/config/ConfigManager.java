package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.properties.EncryptableProperties;

/**
 * <p>
 * This class loads and manage properties file for the application. It uses the
 * <b>Properties</b> class from the <b>java.util</b> JDK Package.
 * </p>
 * 
 * @author Thomas PRADEAU
 * @version 3.0.0
 */
public class ConfigManager {

	private Properties properties;
	private final String[] VALID_SETTINGS_KEYS;
	private final HashMap<String, String> DEFAULT_SETTINGS_VALUES;

	public ConfigManager(String configFile) {
		this.properties = loadConfigFile(configFile, "password");

		VALID_SETTINGS_KEYS = new String[] { "db.url", "db.username", "db.password", "locale.preferred" };

		DEFAULT_SETTINGS_VALUES = new HashMap<>();
		DEFAULT_SETTINGS_VALUES.put("locale.preferred", Locale.US.toLanguageTag());

		checkDefaultSettings();
	}

	/**
	 * <p>
	 * Loads config file from path. The methods needs the password to un-encrypt
	 * some protected variables inside the file.
	 * </p>
	 * 
	 * @param filePath - Relative or absolute path to the config file.
	 * @param password - Password to un-encrypt the file.
	 * @return The config variable as a Properties class instance.
	 * 
	 * @author Thomas PRADEAU
	 * @version 3.0.0
	 */
	public Properties loadConfigFile(String filePath, String password) {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		// TODO Default password
		encryptor.setPassword(password);
		encryptor.setAlgorithm("PBEWithHMACSHA512AndAES_256");
		encryptor.setIvGenerator(new RandomIvGenerator());
		
		System.out.println(encryptor.encrypt("vtg_dba"));
		System.out.println(encryptor.encrypt("P@ssw0rd"));

		Properties properties = new EncryptableProperties(encryptor);
		InputStream inputStream = getClass().getResourceAsStream("/" + filePath);
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

	public Properties getProperties() {
		return properties;
	}

	/**
	 * <p>
	 * Verifies if all config properties exists in the file. If not, these are
	 * created and set to their default values.
	 * </p>
	 * 
	 * @author Thomas PRADEAU
	 * @version 3.0.0
	 */
	public void checkDefaultSettings() {
		for (String setting : VALID_SETTINGS_KEYS)
			if (!properties.containsKey(setting))
				properties.setProperty(setting, DEFAULT_SETTINGS_VALUES.get(setting));

		//save();
	}

	/**
	 * <p>
	 * Update existing properties.
	 * </p>
	 * 
	 * @param key   - Property key
	 * @param value - The new value to set
	 * @return <b>true</b> if the property has been successfully updated,
	 *         <b>false</b> otherwise.
	 * 
	 * @author Thomas PRADEAU
	 * @version 3.0.0
	 */
	/*
	public boolean updateSettings(String key, String value) {
		if (properties.containsKey(key)) {
			properties.setProperty(key, value);
			save();
			return true;
		}
		return false;
	}

	/**
	 * <p>
	 * Save the current loaded configuration to disk.
	 * </p>
	 * 
	 * @author Thomas PRADEAU
	 * @version 3.0.0
	 */
	/*
	public void save() {
		try {
			// TODO Add some comments on config save.
			properties.store(new FileWriter(new File(Config.DBENVFILEPATH)), "");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	*/
}
