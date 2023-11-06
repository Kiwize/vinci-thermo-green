package view.component;

import javax.swing.JComboBox;

import control.Config;

public class ComboBox<Object> extends JComboBox<Object> {

	private static final long serialVersionUID = 4941364516102660885L;

	public ComboBox() {
		super.setBackground(Config.SECONDARY_COLOR);
		super.setForeground(Config.MAIN_FOREGROUND);
	}

}
