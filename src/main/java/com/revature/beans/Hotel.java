package com.revature.beans;

import java.util.Objects;
import java.util.UUID;

public class Hotel {
	private String location;
	private UUID id;
	private String name;
	private Double costPerNight;
	private Integer roomsAvailable;

	public Hotel() {
		super();
	}

	public Hotel(String location, UUID id, String name, Double costPerNight, Integer roomsAvailable) {
		super();
		this.setLocation(location);
		this.setId(id);
		this.setName(name);
		this.setCostPerNight(costPerNight);
		this.setRoomsAvailable(roomsAvailable);
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

	public Double getCostPerNight() {
		return costPerNight;
	}

	public void setCostPerNight(Double costPerNight) {
		this.costPerNight = costPerNight;
	}

	public Integer getRoomsAvailable() {
		return roomsAvailable;
	}

	public void setRoomsAvailable(Integer roomsAvailable) {
		this.roomsAvailable = roomsAvailable;
	}

	@Override
	public int hashCode() {
		return Objects.hash(costPerNight, id, location, name, roomsAvailable);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Hotel other = (Hotel) obj;
		return Objects.equals(costPerNight, other.costPerNight) && Objects.equals(id, other.id)
				&& Objects.equals(location, other.location) && Objects.equals(name, other.name)
				&& Objects.equals(roomsAvailable, other.roomsAvailable);
	}

	@Override
	public String toString() {
		return "Hotel [location=" + location + ", id=" + id + ", name=" + name + ", costPerNight=" + costPerNight
				+ ", roomsAvailable=" + roomsAvailable + "]";
	}

}
