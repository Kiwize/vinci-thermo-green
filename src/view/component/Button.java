package view.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Transparency;

import javax.swing.JButton;

import config.Config;

public class Button extends JButton {

	private static final long serialVersionUID = 699581217198410223L;

	public Button(String text, int x, int y) {
		super(text);
		super.setBackground(Config.MAIN_COLOR);
		super.setFont(Config.MAIN_FONT);
		super.setForeground(Config.CONSTRASTED_FOREGROUND);

		super.setPreferredSize(new Dimension(120, 25));
		super.setBounds(x, y, 105, 27);
		super.setSize(130, 30);
	}

	public void setBackgroundVisible(boolean value) {
		if(!value) {
			super.setBackground(new Color(0, 0, 0, Transparency.TRANSLUCENT));
		} else {
			super.setBackground(Config.MAIN_COLOR);
		}
	}
}
