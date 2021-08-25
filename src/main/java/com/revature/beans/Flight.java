package com.revature.beans;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Flight {
	private String destination;
	private UUID id;
	private String airline;
	private LocalDateTime departingDate;
	private String startingLocation;
	private Double ticketPrice;
	private Integer openSeats;

	public Flight() {
		super();
	}

	public Flight(String destination, UUID id, String airline, LocalDateTime departingDate, String startingLocation,
			Double ticketPrice, Integer openSeats) {
		super();
		this.setDestination(destination);
		this.setId(id);
		this.setAirline(airline);
		this.setDepartingDate(departingDate);
		this.setStartingLocation(startingLocation);
		this.setTicketPrice(ticketPrice);
		this.setOpenSeats(openSeats);
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getAirline() {
		return airline;
	}

	public void setAirline(String airline) {
		this.airline = airline;
	}

	public LocalDateTime getDepartingDate() {
		return departingDate;
	}

	public void setDepartingDate(LocalDateTime departingDate) {
		this.departingDate = departingDate;
	}

	public String getStartingLocation() {
		return startingLocation;
	}

	public void setStartingLocation(String startingLocation) {
		this.startingLocation = startingLocation;
	}

	public Double getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(Double ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public Integer getOpenSeats() {
		return openSeats;
	}

	public void setOpenSeats(Integer openSeats) {
		this.openSeats = openSeats;
	}

	@Override
	public int hashCode() {
		return Objects.hash(airline, departingDate, destination, id, openSeats, startingLocation, ticketPrice);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Flight other = (Flight) obj;
		return Objects.equals(airline, other.airline) && Objects.equals(departingDate, other.departingDate)
				&& Objects.equals(destination, other.destination) && Objects.equals(id, other.id)
				&& Objects.equals(openSeats, other.openSeats)
				&& Objects.equals(startingLocation, other.startingLocation)
				&& Objects.equals(ticketPrice, other.ticketPrice);
	}

	@Override
	public String toString() {
		return "Flight [destination=" + destination + ", id=" + id + ", airline=" + airline + ", departingDate="
				+ departingDate + ", startingLocation=" + startingLocation + ", ticketPrice=" + ticketPrice
				+ ", openSeats=" + openSeats + "]";
	}

}
