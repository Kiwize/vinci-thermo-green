package control;

import java.awt.Color;
import java.awt.Font;

public class Config {
	
	//Config files
	public static final String DBENVFILEPATH = "data/db.env";
	
	
	//Look and feel
	
	//Colors
	public static final Color MAIN_COLOR = new Color(0, 0, 180);
	public static final Color CLICKED_COLOR = new Color(0, 0, 220);
	public static final Color ROLLOVER_COLOR = new Color(0, 0, 200); 
	
	public static final Color SECONDARY_COLOR = new Color(89, 89, 89);
	
	public static final Color MAIN_FOREGROUND = new Color(255, 255, 255);
	
	public static final Color TEXTFIELD_BACKGROUND = new Color(80, 80, 80, 255);
	
	public static final Color PANEL_TRANSLUENT_BACKGROUND = new Color(200, 200, 200, 100);
	
	
	//Fonts
	public static final Font MAIN_FONT = new Font("Consolas", Font.PLAIN, 12);
	
	//Password config parameters
	public static final int MIN_PASSWORD_LENGTH = 12;
	public static final int MAX_PASSWORD_LENGTH = 24;
	
}
