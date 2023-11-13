package view;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

import config.Config;
import control.Controller;
import control.Controller.EPasswordError;
import view.component.ButtonRound;
import view.component.CheckBox;
import view.component.Label;
import view.component.TextField;

public class PasswordChangeView extends JFrame {

	private static final long serialVersionUID = 8961167478637393103L;

	private final TextField passwordField;
	private final TextField confirmationPasswordField;

	private final CheckBox chkboxSimilarPasswords;
	private final CheckBox chkboxMajuscule;
	private final CheckBox chkboxMinuscule;
	private final CheckBox chkboxSpecialChar;
	private final CheckBox chkboxMinLength;
	private final CheckBox chkboxDigit;

	public PasswordChangeView(Controller controller) {
		setResizable(false);
		getContentPane().setBackground(Config.SECONDARY_COLOR);
		getContentPane().setLayout(null);

		final Label lblMotDePasse = new Label("Mot de passe : ");
		lblMotDePasse.setBounds(12, 46, 98, 17);
		getContentPane().add(lblMotDePasse);

		final Label lblConfirmation = new Label("Confirmation :");
		lblConfirmation.setBounds(12, 93, 98, 17);
		getContentPane().add(lblConfirmation);

		passwordField = new TextField();
		passwordField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				controller.passwordFieldUpdate(passwordField.getText(), confirmationPasswordField.getText());
			}
		});
		passwordField.setBounds(117, 44, 114, 21);
		getContentPane().add(passwordField);
		passwordField.setColumns(10);

		confirmationPasswordField = new TextField();
		confirmationPasswordField.setBounds(117, 91, 114, 21);
		getContentPane().add(confirmationPasswordField);
		confirmationPasswordField.setColumns(10);
		confirmationPasswordField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				controller.passwordFieldUpdate(passwordField.getText(), confirmationPasswordField.getText());
			}
		});

		final JProgressBar passwordStrength = new JProgressBar();
		passwordStrength.setBounds(12, 148, 219, 14);
		getContentPane().add(passwordStrength);

		final Label lblFor = new Label(controller.getResourceBundle().getString("passwordChangeViewPasswordStrength"));
		lblFor.setBounds(12, 131, 164, 17);
		getContentPane().add(lblFor);

		final ButtonRound btnValider = new ButtonRound(controller.getResourceBundle().getString("passwordChangeViewSubmit"), 148, 179);
		btnValider.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.changePassword(passwordField.getText(), confirmationPasswordField.getText());
			}
		});
		getContentPane().add(btnValider);

		chkboxSimilarPasswords = new CheckBox(controller.getResourceBundle().getString("passwordChangeViewSamePasswords"));
		chkboxSimilarPasswords.setBounds(270, 8, 214, 25);
		chkboxSimilarPasswords.setEnabled(false);
		getContentPane().add(chkboxSimilarPasswords);

		chkboxMajuscule = new CheckBox(controller.getResourceBundle().getString("passwordChangeViewUppercase"));
		chkboxMajuscule.setBounds(270, 39, 114, 25);
		getContentPane().add(chkboxMajuscule);
		chkboxMajuscule.setEnabled(false);

		chkboxMinuscule = new CheckBox(controller.getResourceBundle().getString("passwordChangeViewLowercase"));
		chkboxMinuscule.setBounds(270, 68, 114, 25);
		getContentPane().add(chkboxMinuscule);
		chkboxMinuscule.setEnabled(false);

		chkboxSpecialChar = new CheckBox(controller.getResourceBundle().getString("passwordChangeViewSpecialChar"));
		chkboxSpecialChar.setBounds(270, 124, 214, 25);
		getContentPane().add(chkboxSpecialChar);
		chkboxSpecialChar.setEnabled(false);

		chkboxMinLength = new CheckBox(controller.getResourceBundle().getString("passwordChangeViewSpecialChar"));
		chkboxMinLength.setBounds(270, 153, 214, 25);
		getContentPane().add(chkboxMinLength);
		chkboxMinLength.setEnabled(false);

		chkboxDigit = new CheckBox(controller.getResourceBundle().getString("passwordChangeViewNumeric"));
		chkboxDigit.setBounds(270, 97, 114, 25);
		getContentPane().add(chkboxDigit);
		chkboxDigit.setEnabled(false);
		super.setSize(500, 250);
	}

	public void showPasswordStatus(HashMap<EPasswordError, Boolean> errors) {
		for(final EPasswordError eerr : EPasswordError.values()) {
			eerr.getCallbackError().match(this, !errors.get(eerr));
		}
	}

	public JCheckBox getChkboxSimilarPasswords() {
		return chkboxSimilarPasswords;
	}

	public JCheckBox getChkboxMinLength() {
		return chkboxMinLength;
	}

	public JCheckBox getChkboxMajuscule() {
		return chkboxMajuscule;
	}

	public JCheckBox getChkboxSpecialChar() {
		return chkboxSpecialChar;
	}

	public JCheckBox getChkboxMinuscule() {
		return chkboxMinuscule;
	}

	public JCheckBox getChkboxDigit() {
		return chkboxDigit;
	}
}
