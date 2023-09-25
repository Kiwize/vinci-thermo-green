package model;

public class User {

	private int id;
	private String name;
	private String password;
	
	public User(String name, String password, int id) {
		this.name = name;
		this.password = password;
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPassword() {
		return password;
	}
	
	public int getId() {
		return id;
	}
	
}
