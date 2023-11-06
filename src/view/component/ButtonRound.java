package view.component;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import control.Config;

public class ButtonRound extends Button {

	private static final long serialVersionUID = 699581217198410223L;

	public ButtonRound(String text, int x, int y) {
		super(text, x, y);
		super.setBackground(Config.MAIN_COLOR);
		super.setFont(Config.MAIN_FONT);
		super.setForeground(Config.MAIN_FOREGROUND);

		super.setPreferredSize(new Dimension(120, 25));
		setContentAreaFilled(false);
	}

	// Paint the round background and label.
	@Override
	protected void paintComponent(Graphics g) {
		if(getModel().isRollover()) {
			g.setColor(Config.ROLLOVER_COLOR);
		}

		if (getModel().isArmed()) {
			g.setColor(Config.CLICKED_COLOR);
		} else {
			g.setColor(getBackground());
		}

		//Pos X, Pos Y, Size X, Size Y, BorderRadius X, BorderRadius Y
		g.fillRoundRect(0, 0, getSize().width - 1, getSize().height - 1, 35, 35);
		super.paintComponent(g);
	}

	// Paint the border of the button using a simple stroke.
	@Override
	protected void paintBorder(Graphics g) {
		//g.setColor(getForeground());
		//g.drawRoundRect(0, 0, getSize().width - 1, getSize().height - 1, 35, 35);
	}

	// Hit detection.
	Shape shape;
	@Override
	public boolean contains(int x, int y) {
		// If the button has changed size,
		// make a new shape object.
		if (shape == null || !shape.getBounds().equals(getBounds())) {
			shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
		}
		return shape.contains(x, y);
	}

}
