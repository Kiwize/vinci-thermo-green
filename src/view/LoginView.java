package view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import config.Config;
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
	
	private final int COLUMNS = 20;
	private final int ROWS = 20;
	

	public LoginView(Controller controller) {
		getContentPane().setBackground(Config.SECONDARY_COLOR);

		setResizable(false);
		getContentPane().setLayout(null);
		//setSize(400, 250);
		setSize(1920, 1080);
		setExtendedState(JFrame.MAXIMIZED_BOTH); 
		setUndecorated(true);

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		setTitle(controller.getResourceBundle().getString("loginViewWindowTitle"));

		final Label lblUsername = new Label(controller.getResourceBundle().getString("loginViewUsername"));
		lblUsername.setBounds(((getWidth() / COLUMNS) * 9) - 35, ((getHeight() / ROWS) * 7) + 30, 104, 17);
		getContentPane().add(lblUsername);

		final Label lblPassword = new Label(controller.getResourceBundle().getString("loginViewPassword"));
		lblPassword.setBounds(((getWidth() / COLUMNS) * 9) - 35, ((getHeight() / ROWS) * 8) + 30, 93, 17);
		getContentPane().add(lblPassword);

		usernameField = new TextField();
		usernameField.setBounds(((getWidth() / COLUMNS) * 10) + 30, ((getHeight() / ROWS) * 7) + 30, 114, 21);
		getContentPane().add(usernameField);
		usernameField.setColumns(10);

		passwordField = new PasswordField();
		passwordField.setBounds(((getWidth() / COLUMNS) * 10) + 30, ((getHeight() / ROWS) * 8) + 30, 114, 21);
		getContentPane().add(passwordField);
		passwordField.setEchoChar('*');
		passwordField.setColumns(10);

		final ButtonRound btnSubmit = new ButtonRound(controller.getResourceBundle().getString("loginViewSubmit"), ((getWidth() / COLUMNS) * 9) - 60, ((getHeight() / ROWS) * 10) + 30);
		btnSubmit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				controller.submitLogins(usernameField.getText(), passwordField.getText());
			}
		});
		getContentPane().add(btnSubmit);

		final Button visiblePasswordBtn = new Button("", 384, 98);
		try {
			visiblePasswordBtn.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/img/visibility24.png"))));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		visiblePasswordBtn.setBounds(((getWidth() / COLUMNS) * 11) + 58, ((getHeight() / ROWS) * 8) + 30, 25, 25);
		getContentPane().add(visiblePasswordBtn);

		final ButtonRound btnChangePassword = new ButtonRound(controller.getResourceBundle().getString("loginPasswordChange"), ((getWidth() / COLUMNS) * 11) - 60, ((getHeight() / ROWS) * 10) + 30);
		btnChangePassword.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				controller.submitPasswordChange(usernameField.getText(), passwordField.getText());
			}
		});
		getContentPane().add(btnChangePassword);
	}
}
