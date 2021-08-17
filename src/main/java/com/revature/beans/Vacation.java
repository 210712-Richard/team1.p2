package com.revature.beans;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Vacation {
	private String username;
	private UUID id;
	private String destination;
	private List<Flight> flights;
	private List<Hotel> hotels;
	private List<Car> cars;
	private List<Activity> activities;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private Double total;
	private Integer partySize;
	private Integer duration;

	public Vacation() {
		flights = new ArrayList<>();
		hotels = new ArrayList<>();
		cars = new ArrayList<>();
		activities = new ArrayList<>();
		total = 0.00;
	}

	public Vacation(String username, UUID id, String destination, LocalDateTime startTime, LocalDateTime endTime,
			Integer partySize, Integer duration) {
		super();
		this.username = username;
		this.id = id;
		this.destination = destination;
		this.startTime = startTime;
		this.endTime = endTime;
		this.partySize = partySize;
		this.duration = duration;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public List<Flight> getFlights() {
		return flights;
	}

	public void setFlights(List<Flight> flights) {
		this.flights = flights;
	}

	public List<Hotel> getHotels() {
		return hotels;
	}

	public void setHotels(List<Hotel> hotels) {
		this.hotels = hotels;
	}

	public List<Car> getCars() {
		return cars;
	}

	public void setCars(List<Car> cars) {
		this.cars = cars;
	}

	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Integer getPartySize() {
		return partySize;
	}

	public void setPartySize(Integer partySize) {
		this.partySize = partySize;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	@Override
	public int hashCode() {
		return Objects.hash(activities, cars, destination, duration, endTime, flights, hotels, id, partySize, startTime,
				total, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vacation other = (Vacation) obj;
		return Objects.equals(activities, other.activities) && Objects.equals(cars, other.cars)
				&& Objects.equals(destination, other.destination) && Objects.equals(duration, other.duration)
				&& Objects.equals(endTime, other.endTime) && Objects.equals(flights, other.flights)
				&& Objects.equals(hotels, other.hotels) && Objects.equals(id, other.id)
				&& Objects.equals(partySize, other.partySize) && Objects.equals(startTime, other.startTime)
				&& Objects.equals(total, other.total) && Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "Vacation [username=" + username + ", id=" + id + ", destination=" + destination + ", flights=" + flights
				+ ", hotels=" + hotels + ", cars=" + cars + ", activities=" + activities + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", total=" + total + ", partySize=" + partySize + ", duration=" + duration
				+ "]";
	}

	
}
