package view.component;

import javax.swing.JPanel;

import config.Config;

public class Panel extends JPanel {

	private static final long serialVersionUID = -539846659934673681L;

	public Panel() {
		super.setBackground(Config.SECONDARY_COLOR);
		super.setForeground(Config.CONSTRASTED_FOREGROUND);
	}
}
