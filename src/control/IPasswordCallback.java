package control;

import view.PasswordChangeView;

public interface IPasswordCallback {

	public abstract void match(PasswordChangeView view, boolean state);
	
}
