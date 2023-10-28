package view.component;

import javax.swing.JCheckBox;

import control.Config;

public class CheckBox extends JCheckBox{

	private static final long serialVersionUID = -3577621832215067596L;

	public CheckBox(String text) {
		super(text);
		this.setBackground(Config.SECONDARY_COLOR);
		this.setForeground(Config.MAIN_FOREGROUND);
	}
	
	
	
}
