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
	private List<Reservation> reservations;
	private List<Activity> activities;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private Double total;
	private Integer partySize;
	private Integer duration;

	public Vacation() {
		reservations = new ArrayList<>();
		activities = new ArrayList<>();
		total = 0.00;
	}

	public Vacation(String username, UUID id, String destination, LocalDateTime startTime, LocalDateTime endTime,
			Integer partySize, Integer duration) {
		super();
		this.setUsername(username);
		this.setId(id);
		this.setDestination(destination);
		this.setStartTime(startTime);
		this.setEndTime(endTime);
		this.setPartySize(partySize);
		this.setDuration(duration);
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
	
	public List<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
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
		return Objects.hash(activities, destination, duration, endTime, id, partySize, reservations, startTime, total,
				username);
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
		return Objects.equals(activities, other.activities) && Objects.equals(destination, other.destination)
				&& Objects.equals(duration, other.duration) && Objects.equals(endTime, other.endTime)
				&& Objects.equals(id, other.id) && Objects.equals(partySize, other.partySize)
				&& Objects.equals(reservations, other.reservations) && Objects.equals(startTime, other.startTime)
				&& Objects.equals(total, other.total) && Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "Vacation [username=" + username + ", id=" + id + ", destination=" + destination + ", reservations="
				+ reservations + ", activities=" + activities + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", total=" + total + ", partySize=" + partySize + ", duration=" + duration + "]";
	}

	
}
