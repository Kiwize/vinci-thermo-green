package view.component;

import javax.swing.JLabel;

import control.Config;

public class Label extends JLabel{

	private static final long serialVersionUID = -6766825511223080114L;

	public Label(String text) {
		super(text);
		super.setFont(Config.MAIN_FONT);
		super.setForeground(Config.MAIN_FOREGROUND);
	}

}
