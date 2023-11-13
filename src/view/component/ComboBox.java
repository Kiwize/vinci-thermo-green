package view.component;

import javax.swing.JComboBox;

import config.Config;

public class ComboBox<E> extends JComboBox<E> {

	private static final long serialVersionUID = 4941364516102660885L;

	public ComboBox() {
		super.setBackground(Config.SECONDARY_COLOR);
		super.setForeground(Config.MAIN_FOREGROUND);
	}

}
