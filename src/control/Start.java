package control;

import java.io.File;
import java.sql.SQLException;
import java.text.ParseException;

import utils.DatabaseHelper;

public class Start {
	
	public static void main(String[] args) {
		try {
			Start start = new Start();
			EError errcode = start.chkBoot();
			
			if(errcode.equals(EError.NO_ERROR))
				start.init();
			else {
				System.err.println(errcode.getErrMessage() + " (" + errcode.getErrCode() + ")");
				System.exit(errcode.getErrCode());
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Check basic configuration that can prevent the program from running properly.
	 * @return
	 */
	public EError chkBoot() {
		//Database verification
		File file = new File(Config.DBENVFILEPATH);
		if(!file.exists()) return EError.MISSING_INVALID_CONFIG;
		
		try {
			DatabaseHelper db = new DatabaseHelper();
			db.close();
		} catch (ClassNotFoundException | SQLException e) {
			return EError.UNREACHABLE_DATABASE;
		}
		
		return EError.NO_ERROR;
	}
		
	/**
	 * Initialization phase, post checking
	 * @throws ParseException
	 */
	public void init() throws ParseException {
		Controller controller = new Controller();
	}
}
