package control;

import view.PasswordChangeView;

/**
 * <p>
 * Callback methods for password change.
 * </p>
 * 
 * @author Thomas PRADEAU
 * @version 3.0.0
 */
public interface IPasswordCallback {

	/**
	 * <p>
	 * This method is redefined in the Controller class and is called
	 * every time the user type in the password fields in the <b>PasswordChangeView</b>
	 * </p>
	 * 
	 * @param view - The view it came from
	 * @param state - State of the error
	 * 
	 * @author Thomas PRADEAU
	 * @version 3.0.0
	 */
	void match(PasswordChangeView view, boolean state);

}
