package view.component;

import java.awt.Insets;

import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import control.Config;

public class TextField extends JTextField {

	private static final long serialVersionUID = 2304592188592649746L;

	public TextField() {
		super.setBackground(Config.TEXTFIELD_BACKGROUND);
		super.setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
		super.setForeground(Config.MAIN_FOREGROUND);
		super.setFont(Config.MAIN_FONT);
	}

}
