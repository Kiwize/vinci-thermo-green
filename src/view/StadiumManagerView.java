package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import control.Controller;
import model.Stadium;

public class StadiumManagerView extends JFrame {

	private Controller controller;

	public StadiumManagerView(Controller controller) {
		setTitle("Available stadiums");

		this.controller = controller;
		this.setSize(400, 300);
		this.setResizable(false);

		JLabel lblStadium = new JLabel("Stades");
		getContentPane().add(lblStadium, BorderLayout.NORTH);

		JScrollPane stadiumScrollPane = new JScrollPane();
		getContentPane().add(stadiumScrollPane, BorderLayout.CENTER);

		JPanel recordsPanel = new JPanel();
		stadiumScrollPane.setViewportView(recordsPanel);
		recordsPanel.setLayout(new BoxLayout(recordsPanel, BoxLayout.Y_AXIS));

		for (Stadium stadium : controller.getUserStadiums()) {
			JButton button = new JButton(stadium.getStadiumName());
			
			button.addMouseListener(new MouseAdapter() {
				
				@Override
				public void mouseClicked(MouseEvent e) {
					controller.updateConsoleView(stadium);
					super.mouseClicked(e);
				}
			});
			
			button.setAlignmentX(Component.LEFT_ALIGNMENT);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			button.setPreferredSize(
					new Dimension(recordsPanel.getPreferredSize().width, button.getPreferredSize().height));
			recordsPanel.add(button);
		}
	}

}
