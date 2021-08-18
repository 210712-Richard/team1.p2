package com.revature.dto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import com.revature.beans.Vacation;

@Table("vacation")
public class VacationDto {
	@PrimaryKeyColumn(name = "username", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String username;
	@PrimaryKeyColumn(name = "id", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
	private UUID id;
	@PrimaryKeyColumn(name = "destination", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
	private String destination;
	private List<UUID> flights;
	private List<UUID> hotels;
	private List<UUID> cars;
	private List<UUID> activities;
	private Instant startTime;
	private Instant endTime;
	private Double total;
	private Integer partySize;
	private Integer duration;

	public VacationDto() {
		flights = new ArrayList<>();
		hotels = new ArrayList<>();
		cars = new ArrayList<>();
	}

	public VacationDto(Vacation v) {
		this();
		this.setUsername(v.getUsername());
		this.setId(v.getId());
		this.setDestination(v.getDestination());
		this.setStartTime(v.getStartTime().toInstant(ZoneOffset.UTC));
		this.setEndTime(v.getEndTime().toInstant(ZoneOffset.UTC));
		this.setTotal(v.getTotal());
		this.setPartySize(v.getPartySize());
		this.setDuration(v.getDuration());

		v.getFlights().stream().forEach(f -> this.flights.add(f.getId()));

		v.getHotels().stream().forEach(h -> this.hotels.add(h.getId()));

		v.getCars().stream().forEach(c -> this.cars.add(c.getId()));

		v.getActivities().stream().forEach(a -> this.activities.add(a.getId()));
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

	public List<UUID> getFlights() {
		return flights;
	}

	public void setFlights(List<UUID> flights) {
		this.flights = flights;
	}

	public List<UUID> getHotels() {
		return hotels;
	}

	public void setHotels(List<UUID> hotels) {
		this.hotels = hotels;
	}

	public List<UUID> getCars() {
		return cars;
	}

	public void setCars(List<UUID> cars) {
		this.cars = cars;
	}

	public List<UUID> getActivities() {
		return activities;
	}

	public void setActivities(List<UUID> activities) {
		this.activities = activities;
	}

	public Instant getStartTime() {
		return startTime;
	}

	public void setStartTime(Instant startTime) {
		this.startTime = startTime;
	}

	public Instant getEndTime() {
		return endTime;
	}

	public void setEndTime(Instant endTime) {
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

	public Vacation getVacation() {
		Vacation v = new Vacation();

		v.setUsername(username);
		v.setId(id);
		v.setDestination(destination);
		v.setStartTime(LocalDateTime.ofInstant(startTime, ZoneOffset.UTC));
		v.setEndTime(LocalDateTime.ofInstant(endTime, ZoneOffset.UTC));
		v.setTotal(total);
		v.setPartySize(partySize);
		v.setDuration(duration);

		return v;
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
		VacationDto other = (VacationDto) obj;
		return Objects.equals(activities, other.activities) && Objects.equals(cars, other.cars)
				&& Objects.equals(destination, other.destination) && Objects.equals(duration, other.duration)
				&& Objects.equals(endTime, other.endTime) && Objects.equals(flights, other.flights)
				&& Objects.equals(hotels, other.hotels) && Objects.equals(id, other.id)
				&& Objects.equals(partySize, other.partySize) && Objects.equals(startTime, other.startTime)
				&& Objects.equals(total, other.total) && Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "VacationDto [username=" + username + ", id=" + id + ", destination=" + destination + ", flights="
				+ flights + ", hotels=" + hotels + ", cars=" + cars + ", activities=" + activities + ", startTime="
				+ startTime + ", endTime=" + endTime + ", total=" + total + ", partySize=" + partySize + ", duration="
				+ duration + "]";
	}

}
