package view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import control.Controller;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class LoginView extends JFrame {

	private Controller controller;

	public LoginView(Controller controller) {
		this.controller = controller;

		setTitle(controller.getResourceBundle().getString("loginViewWindowTitle"));
		setResizable(false);

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 51, 114, 114, 102, 0 };
		gbl_panel.rowHeights = new int[] { 27, 0, 0, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		JLabel usernameLabel = new JLabel(controller.getResourceBundle().getString("loginViewUsername"));
		GridBagConstraints gbc_usernameLabel = new GridBagConstraints();
		gbc_usernameLabel.insets = new Insets(0, 0, 5, 5);
		gbc_usernameLabel.anchor = GridBagConstraints.EAST;
		gbc_usernameLabel.gridx = 1;
		gbc_usernameLabel.gridy = 1;
		panel.add(usernameLabel, gbc_usernameLabel);

		usernameTextField = new JTextField();
		usernameTextField.setToolTipText(controller.getResourceBundle().getString("loginViewUsername"));
		GridBagConstraints gbc_usernameTextField = new GridBagConstraints();
		gbc_usernameTextField.anchor = GridBagConstraints.WEST;
		gbc_usernameTextField.insets = new Insets(0, 0, 5, 5);
		gbc_usernameTextField.gridx = 2;
		gbc_usernameTextField.gridy = 1;
		panel.add(usernameTextField, gbc_usernameTextField);
		usernameTextField.setColumns(10);

		JLabel passwordLabel = new JLabel(controller.getResourceBundle().getString("loginViewPassword"));
		GridBagConstraints gbc_passwordLabel = new GridBagConstraints();
		gbc_passwordLabel.insets = new Insets(0, 0, 5, 5);
		gbc_passwordLabel.anchor = GridBagConstraints.EAST;
		gbc_passwordLabel.gridx = 1;
		gbc_passwordLabel.gridy = 2;
		panel.add(passwordLabel, gbc_passwordLabel);

		passwordTextField = new JPasswordField();
		GridBagConstraints gbc_passwordTextField = new GridBagConstraints();
		gbc_passwordTextField.anchor = GridBagConstraints.WEST;
		gbc_passwordTextField.insets = new Insets(0, 0, 5, 5);
		gbc_passwordTextField.gridx = 2;
		gbc_passwordTextField.gridy = 2;
		panel.add(passwordTextField, gbc_passwordTextField);
		passwordTextField.setColumns(10);

		JButton submitButton = new JButton(controller.getResourceBundle().getString("loginViewSubmit"));
		submitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (usernameTextField.getText().isEmpty() || passwordTextField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(e.getComponent(),
							controller.getResourceBundle().getString("loginViewEmptyCredentials"),
							controller.getResourceBundle().getString("loginViewError"), JOptionPane.ERROR_MESSAGE);
					return;
				}

				controller.submitLogins(usernameTextField.getText(), passwordTextField.getText());
			}
		});
		GridBagConstraints gbc_submitButton = new GridBagConstraints();
		gbc_submitButton.insets = new Insets(0, 0, 0, 5);
		gbc_submitButton.anchor = GridBagConstraints.NORTHWEST;
		gbc_submitButton.gridx = 2;
		gbc_submitButton.gridy = 4;
		panel.add(submitButton, gbc_submitButton);
		setSize(400, 200);
	}

	private static final long serialVersionUID = 1L;
	private JTextField usernameTextField;
	private JTextField passwordTextField;

}
