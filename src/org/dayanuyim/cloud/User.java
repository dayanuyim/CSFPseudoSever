package org.dayanuyim.cloud;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	private String lastName;
	private String firstName;
	private String email;

	public User(String lastName, String firstName, String email) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.email = email;
	}

	public User() {
		this.lastName = "";
		this.firstName = "";
		this.email = "";
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
	
}
