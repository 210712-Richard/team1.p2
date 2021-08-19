package com.revature.beans;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class User {
	private String username;
	private String password;
	private String email;
	private String firstName;
	private String lastName;
	private LocalDate birthday;
	private List<UUID> vacations;
	private UserType type;
	
	public User() {
		vacations = new ArrayList<>();
	}
	
	public User(String username, String password, String email, String firstName, String lastName, LocalDate birthday,
			UserType type) {
		this();
		this.setUsername(username);
		this.setPassword(password);
		this.setEmail(email);
		this.setFirstName(firstName);
		this.setLastName(lastName);
		this.setBirthday(birthday);
		this.setType(type);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	public List<UUID> getVacations() {
		return vacations;
	}

	public void setVacations(List<UUID> vacations) {
		this.vacations = vacations;
	}

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(birthday, email, firstName, lastName, password, type, username, vacations);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(birthday, other.birthday) && Objects.equals(email, other.email)
				&& Objects.equals(firstName, other.firstName) && Objects.equals(lastName, other.lastName)
				&& Objects.equals(password, other.password) && type == other.type
				&& Objects.equals(username, other.username) && Objects.equals(vacations, other.vacations);
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", email=" + email + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", birthday=" + birthday + ", vacations=" + vacations + ", type=" + type
				+ "]";
	}
	
}
