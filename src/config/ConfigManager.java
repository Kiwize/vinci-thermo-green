package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;
import org.jasypt.properties.EncryptableProperties;

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

	public Properties loadConfigFile(String filePath, String password) {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		// TODO Default password
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

	public Properties getProperties() {
		return properties;
	}

	public void checkDefaultSettings() {
		for (String setting : VALID_SETTINGS_KEYS)
			if (!properties.containsKey(setting))
				properties.setProperty(setting, DEFAULT_SETTINGS_VALUES.get(setting));

		save();
	}

	public boolean updateSettings(String key, String value) {
		if (properties.containsKey(key)) {
			properties.setProperty(key, value);
			save();
			return true;
		}
		return false;
	}
	
	public void save() {
		try {
			properties.store(new FileWriter(new File(Config.DBENVFILEPATH)), "");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
