package view.component;

import javax.swing.JSlider;

import config.Config;

public class Slider extends JSlider{

	private static final long serialVersionUID = 2972667786866964772L;

	public Slider() {
		super.setBackground(Config.SECONDARY_COLOR);
		super.setForeground(Config.CONSTRASTED_FOREGROUND);
	}

}
