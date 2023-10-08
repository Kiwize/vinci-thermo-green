package control;

import java.text.ParseException;

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
	}
}
