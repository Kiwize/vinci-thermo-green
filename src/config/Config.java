package config;

import java.awt.Color;
import java.awt.Font;
import java.util.Locale;

public class Config {

	// Locale
	public static final Locale DEFAULT_LOCALE = Locale.US;
	

	// Config files
	public static final String DBENVFILEPATH_OLD = "data/db.env";
	public static final String DBENVFILEPATH = "data/settings.properties";

	// Look and feel

	// Colors
	public static final Color MAIN_COLOR = new Color(81, 70, 174);
	public static final Color CLICKED_COLOR = new Color(0, 0, 220);
	public static final Color ROLLOVER_COLOR = new Color(0, 0, 200);

	public static final Color SECONDARY_COLOR = new Color(200, 200, 200);

	public static final Color MAIN_FOREGROUND = new Color(0, 0, 0);
	public static final Color CONSTRASTED_FOREGROUND = new Color(255, 255, 255);

	public static final Color TEXTFIELD_BACKGROUND = new Color(180, 180, 180, 255);

	public static final Color PANEL_TRANSLUENT_BACKGROUND = new Color(160, 160, 160, 100);
	public static final Color BUTTON_SHADOW = new Color(30, 30, 30, 180);

	// Fonts
	public static final Font MAIN_FONT = new Font("Consolas", Font.PLAIN, 14);

	// Password config parameters
	public static final int MIN_PASSWORD_LENGTH = 12;
	public static final int MAX_PASSWORD_LENGTH = 24;

	
}
