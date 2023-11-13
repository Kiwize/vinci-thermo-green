package view.component;

import javax.swing.JRadioButton;

import config.Config;

public class RadioButton extends JRadioButton {

	private static final long serialVersionUID = 1L;

	public RadioButton(String text) {
		super(text);
		super.setBackground(Config.SECONDARY_COLOR);
		super.setForeground(Config.MAIN_FOREGROUND);
	}

}
