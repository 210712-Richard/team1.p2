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

import com.revature.beans.Reservation;
import com.revature.beans.ReservationStatus;
import com.revature.beans.ReservationType;

@Table("reservation")
public class ReservationDto {
	@PrimaryKeyColumn(name = "id", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
	private UUID uuid;
	@PrimaryKeyColumn(name = "type", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
	private String type;
	@PrimaryKeyColumn(name = "reservedId", ordinal = 2, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
	private UUID reservedId;
	private UUID vacationId;
	private String username;
	private String reservedName;
	private Instant startTime;
	private Double cost;
	private Integer duration;
	private String status;
	
	public ReservationDto() {
		super();
	}
	
	public ReservationDto(Reservation r) {
		this.setType(r.getType().toString());
		this.setId(r.getId());
		this.setReservedId(r.getReservedId());
		this.setVacationId(r.getVacationId());
		this.setUsername(r.getUsername());
		this.setReservedName(r.getReservedName());
		this.setStartTime(r.getStartTime().toInstant(ZoneOffset.UTC));
		this.setCost(r.getCost());
		this.setDuration(r.getDuration());
		this.setStatus(r.getStatus().toString());
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public UUID getId() {
		return uuid;
	}

	public void setId(UUID id) {
		this.uuid = id;
	}

	public UUID getVacationId() {
		return vacationId;
	}

	public void setVacationId(UUID vacationId) {
		this.vacationId = vacationId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getReservedName() {
		return reservedName;
	}

	public void setReservedName(String reservedName) {
		this.reservedName = reservedName;
	}

	public Instant getStartTime() {
		return startTime;
	}

	public void setStartTime(Instant startTime) {
		this.startTime = startTime;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	
	public UUID getReservedId() {
		return reservedId;
	}

	public void setReservedId(UUID reservedId) {
		this.reservedId = reservedId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Reservation getReservation() {
		Reservation r = new Reservation();
		
		r.setType(ReservationType.valueOf(type));
		r.setId(uuid);
		r.setReservedId(reservedId);
		r.setVacationId(vacationId);
		r.setUsername(username);
		r.setReservedName(reservedName);
		r.setStartTime(LocalDateTime.ofInstant(startTime, ZoneOffset.UTC));
		r.setCost(cost);
		r.setDuration(duration);
		r.setStatus(ReservationStatus.valueOf(status));
		
		return r;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cost, duration, uuid, reservedId, reservedName, startTime, status, type, username,
				vacationId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReservationDto other = (ReservationDto) obj;
		return Objects.equals(cost, other.cost) && Objects.equals(duration, other.duration)
				&& Objects.equals(uuid, other.uuid) && Objects.equals(reservedId, other.reservedId)
				&& Objects.equals(reservedName, other.reservedName) && Objects.equals(startTime, other.startTime)
				&& Objects.equals(status, other.status) && Objects.equals(type, other.type)
				&& Objects.equals(username, other.username) && Objects.equals(vacationId, other.vacationId);
	}

	@Override
	public String toString() {
		return "ReservationDto [id=" + uuid + ", type=" + type + ", reservedId=" + reservedId + ", vacationId="
				+ vacationId + ", username=" + username + ", reservedName=" + reservedName + ", starttime=" + startTime
				+ ", cost=" + cost + ", duration=" + duration + ", status=" + status + "]";
	}
	
}
