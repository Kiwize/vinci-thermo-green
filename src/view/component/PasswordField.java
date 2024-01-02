package view.component;

import javax.swing.JPasswordField;

import config.Config;

public class PasswordField extends JPasswordField{

	private static final long serialVersionUID = 7649848729385307627L;
	
	public PasswordField() {
		super.setBackground(Config.TEXTFIELD_BACKGROUND);
		super.setForeground(Config.MAIN_FOREGROUND);
		super.setBorder(null);
	}

}
