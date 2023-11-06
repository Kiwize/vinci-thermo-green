package control;

import view.PasswordChangeView;

public interface IPasswordCallback {

	void match(PasswordChangeView view, boolean state);

}
