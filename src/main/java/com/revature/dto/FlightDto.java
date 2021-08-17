package com.revature.dto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import com.revature.beans.Flight;

@Table("flight")
public class FlightDto {
	@PrimaryKeyColumn(name = "destination", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String destination;
	@PrimaryKeyColumn(name = "id", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
	private UUID id;
	@PrimaryKeyColumn(name = "airline", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
	private String airline;
	private Instant departingDate;
	private String startingLocation;
	private Double ticketPrice;
	private Integer openSeats;

	public FlightDto() {
		super();
	}

	public FlightDto(Flight f) {
		this();
		this.destination = f.getDestination();
		this.id = f.getId();
		this.airline = f.getAirline();
		this.departingDate = f.getDepartingDate().toInstant(ZoneOffset.UTC);
		this.startingLocation = f.getStartingLocation();
		this.ticketPrice = f.getTicketPrice();
		this.openSeats = f.getOpenSeats();
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

	public Instant getDepartingDate() {
		return departingDate;
	}

	public void setDepartingDate(Instant departingDate) {
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

	public Flight getFlight() {
		Flight f = new Flight();
		
		f.setDestination(destination);
		f.setId(id);
		f.setAirline(airline);
		f.setDepartingDate(LocalDateTime.ofInstant(departingDate, ZoneOffset.UTC));
		f.setStartingLocation(startingLocation);
		f.setTicketPrice(ticketPrice);
		f.setOpenSeats(openSeats);
		
		return f;
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
		FlightDto other = (FlightDto) obj;
		return Objects.equals(airline, other.airline) && Objects.equals(departingDate, other.departingDate)
				&& Objects.equals(destination, other.destination) && Objects.equals(id, other.id)
				&& Objects.equals(openSeats, other.openSeats)
				&& Objects.equals(startingLocation, other.startingLocation)
				&& Objects.equals(ticketPrice, other.ticketPrice);
	}

	@Override
	public String toString() {
		return "FlightDto [destination=" + destination + ", id=" + id + ", airline=" + airline + ", departingDate="
				+ departingDate + ", startingLocation=" + startingLocation + ", ticketPrice=" + ticketPrice
				+ ", openSeats=" + openSeats + "]";
	}

}
