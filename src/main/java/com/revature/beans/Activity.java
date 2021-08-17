package com.revature.beans;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Activity {
	private String location;
	private UUID id;
	private String name;
	private String description;
	private Double cost;
	private LocalDateTime date;
	private Integer maxParticipants;
	
	public Activity() {
		super();
	}
	public Activity(String location, UUID id, String name, String description, Double cost, LocalDateTime date,
			Integer maxParticipants) {
		super();
		this.setLocation(location);
		this.setId(id);
		this.setName(name);
		this.setDescription(description);
		this.setCost(cost);
		this.setDate(date);
		this.setMaxParticipants(maxParticipants);
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}
	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	public Integer getMaxParticipants() {
		return maxParticipants;
	}
	public void setMaxParticipants(Integer maxParticipants) {
		this.maxParticipants = maxParticipants;
	}
	@Override
	public int hashCode() {
		return Objects.hash(cost, date, description, id, location, maxParticipants, name);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Activity other = (Activity) obj;
		return Objects.equals(cost, other.cost) && Objects.equals(date, other.date)
				&& Objects.equals(description, other.description) && Objects.equals(id, other.id)
				&& Objects.equals(location, other.location) && Objects.equals(maxParticipants, other.maxParticipants)
				&& Objects.equals(name, other.name);
	}
	@Override
	public String toString() {
		return "Activity [location=" + location + ", id=" + id + ", name=" + name + ", description=" + description
				+ ", cost=" + cost + ", date=" + date + ", maxParticipants=" + maxParticipants + "]";
	}
	
	
}
