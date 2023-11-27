package control;

/**
 * <p>
 * Enumeration that describes all errors that can occur during the application lifetime.
 * </p>
 * 
 * @author Thomas PRADEAU
 * @version 3.0.0
 */
public enum EError {

	NO_ERROR("", 0),
	MISSING_INVALID_CONFIG("Missing or invalid configuration", -1),
	UNREACHABLE_DATABASE("Database connexion error, please check your database connexion informations", -2);

	private final String errMessage;
	private final int errCode;

	EError(String errMessage, int errCode) {
		this.errMessage = errMessage;
		this.errCode = errCode;
	}

	public String getErrMessage() {
		return errMessage;
	}

	public int getErrCode() {
		return errCode;
	}

}
