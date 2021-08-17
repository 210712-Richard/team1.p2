package com.revature.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import com.revature.beans.User;
import com.revature.beans.UserType;

@Table("user")
public class UserDto {
	@PrimaryKeyColumn(name = "username", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String username;
	@PrimaryKeyColumn(name = "password", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
	private String password;
	private String email;
	private String firstName;
	private String lastName;
	private LocalDate birthday;
	private List<UUID> vacations;
	private String type;

	public UserDto() {
		super();
	}

	public UserDto(User user) {
		this.setUsername(user.getUsername());
		this.setPassword(user.getPassword());
		this.setEmail(user.getEmail());
		this.setFirstName(user.getFirstName());
		this.setLastName(user.getLastName());
		this.setBirthday(user.getBirthday());
		this.vacations = new ArrayList<>();
		user.getVacations().stream().forEach(v -> this.vacations.add(v.getId()));
		this.setType(user.getType().toString());
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public User getUser() {
		return new User(this.username, this.password, this.email, this.firstName, this.lastName, this.birthday,
				UserType.valueOf(this.type));
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
		UserDto other = (UserDto) obj;
		return Objects.equals(birthday, other.birthday) && Objects.equals(email, other.email)
				&& Objects.equals(firstName, other.firstName) && Objects.equals(lastName, other.lastName)
				&& Objects.equals(password, other.password) && Objects.equals(type, other.type)
				&& Objects.equals(username, other.username) && Objects.equals(vacations, other.vacations);
	}

	@Override
	public String toString() {
		return "UserDto [username=" + username + ", password=" + password + ", email=" + email + ", firstName="
				+ firstName + ", lastName=" + lastName + ", birthday=" + birthday + ", vacations=" + vacations
				+ ", type=" + type + "]";
	}

}
