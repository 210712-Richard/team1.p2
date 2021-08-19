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
	private List<UUID> reservations;
	private List<UUID> activities;
	private Instant startTime;
	private Instant endTime;
	private Double total;
	private Integer partySize;
	private Integer duration;

	public VacationDto() {
		reservations = new ArrayList<>();
		activities = new ArrayList<>();
	}

	public VacationDto(Vacation v) {
		this();
		this.setUsername(v.getUsername());
		this.setId(v.getId());
		this.setDestination(v.getDestination());
		this.setActivities(v.getActivities());
		this.setStartTime(v.getStartTime().toInstant(ZoneOffset.UTC));
		this.setEndTime(v.getEndTime().toInstant(ZoneOffset.UTC));
		this.setTotal(v.getTotal());
		this.setPartySize(v.getPartySize());
		this.setDuration(v.getDuration());

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

	public List<UUID> getReservations() {
		return reservations;
	}

	public void setReservations(List<UUID> reservations) {
		this.reservations = reservations;
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
		v.setReservations(reservations);
		v.setActivities(activities);
		v.setStartTime(LocalDateTime.ofInstant(startTime, ZoneOffset.UTC));
		v.setEndTime(LocalDateTime.ofInstant(endTime, ZoneOffset.UTC));
		v.setTotal(total);
		v.setPartySize(partySize);
		v.setDuration(duration);

		return v;
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
		VacationDto other = (VacationDto) obj;
		return Objects.equals(activities, other.activities) && Objects.equals(destination, other.destination)
				&& Objects.equals(duration, other.duration) && Objects.equals(endTime, other.endTime)
				&& Objects.equals(id, other.id) && Objects.equals(partySize, other.partySize)
				&& Objects.equals(reservations, other.reservations) && Objects.equals(startTime, other.startTime)
				&& Objects.equals(total, other.total) && Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "VacationDto [username=" + username + ", id=" + id + ", destination=" + destination + ", reservations="
				+ reservations + ", activities=" + activities + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", total=" + total + ", partySize=" + partySize + ", duration=" + duration + "]";
	}

}
