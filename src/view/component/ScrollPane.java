package view.component;

import javax.swing.JScrollPane;

import control.Config;

public class ScrollPane extends JScrollPane {

	private static final long serialVersionUID = 5683064417561962135L;

	public ScrollPane() {
		setBackground(Config.SECONDARY_COLOR);
		setForeground(Config.MAIN_FOREGROUND);
	}

}
