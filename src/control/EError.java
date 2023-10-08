package control;

public enum EError {
	
	NO_ERROR("", 0),
	MISSING_INVALID_CONFIG("Missing or invalid configuration", -1),
	UNREACHABLE_DATABASE("Database connexion error, please check your database connexion informations", -2);
	
	private String errMessage;
	private int errCode;
	
	private EError(String errMessage, int errCode) {
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
