package control;

import java.text.ParseException;

import utils.BCrypt;

public class Start {
	
	public static void main(String[] args) {
		try {
			new Start().init();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void init() throws ParseException {
		Controller controller = new Controller();
		
		System.out.println(BCrypt.hashpw("password3", BCrypt.gensalt()));
	}
}
