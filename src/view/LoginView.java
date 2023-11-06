package view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import control.Config;
import control.Controller;
import view.component.Button;
import view.component.ButtonRound;
import view.component.Label;
import view.component.PasswordField;
import view.component.TextField;

public class LoginView extends JFrame {

	private static final long serialVersionUID = -4972101784603327025L;
	private final TextField usernameField;
	private final PasswordField passwordField;

	private boolean isPasswordVisible = true;

	public LoginView(Controller controller) {
		getContentPane().setBackground(Config.SECONDARY_COLOR);

		setResizable(false);
		getContentPane().setLayout(null);
		setSize(400, 250);

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		setTitle(controller.getResourceBundle().getString("loginViewWindowTitle"));

		final Label lblUsername = new Label(controller.getResourceBundle().getString("loginViewUsername"));
		lblUsername.setBounds(70, 59, 104, 17);
		getContentPane().add(lblUsername);

		final Label lblPassword = new Label(controller.getResourceBundle().getString("loginViewPassword"));
		lblPassword.setBounds(70, 98, 93, 17);
		getContentPane().add(lblPassword);

		usernameField = new TextField();
		usernameField.setBounds(192, 57, 114, 21);
		getContentPane().add(usernameField);
		usernameField.setColumns(10);

		passwordField = new PasswordField();
		passwordField.setBounds(192, 96, 114, 21);
		getContentPane().add(passwordField);
		passwordField.setEchoChar('*');
		passwordField.setColumns(10);

		final ButtonRound btnSubmit = new ButtonRound(controller.getResourceBundle().getString("loginViewSubmit"), getWidth() / 2, 160);
		btnSubmit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				controller.submitLogins(usernameField.getText(), passwordField.getText());
			}
		});
		getContentPane().add(btnSubmit);

		final Button visiblePasswordBtn = new Button("", 384, 98);
		visiblePasswordBtn.setIcon(new ImageIcon("img/visibility24.png"));
		visiblePasswordBtn.setBackgroundVisible(false);
		visiblePasswordBtn.setBorder(null);
		visiblePasswordBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (isPasswordVisible) {
					passwordField.setEchoChar((char) 0);
				} else {
					passwordField.setEchoChar('*');
				}

				isPasswordVisible = !isPasswordVisible;
			}
		});
		visiblePasswordBtn.setBounds(324, 98, 25, 25);
		getContentPane().add(visiblePasswordBtn);

		final ButtonRound btnChangePassword = new ButtonRound(controller.getResourceBundle().getString("loginPasswordChange"), getWidth() / 4 - 105 / 2, 160);
		btnChangePassword.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				controller.submitPasswordChange(usernameField.getText(), passwordField.getText());
			}
		});
		getContentPane().add(btnChangePassword);
	}
}
