package com.revature.beans;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Reservation {
	private ReservationType type;
	private UUID id;
	private UUID reservedId;
	private UUID vacationId;
	private String username;
	private String reservedName;
	private LocalDateTime starttime;
	private Double cost;
	private Integer duration;

	public Reservation() {
		super();
	}

	public Reservation(ReservationType type, UUID id, UUID reservedId, UUID vacationId, String username,
			LocalDateTime starttime) {
		super();
		this.setType(type);
		this.setId(id);
		this.setReservedId(reservedId);
		this.setVacationId(vacationId);
		this.setUsername(username);
		this.setStarttime(starttime);
	}

	public ReservationType getType() {
		return type;
	}

	public void setType(ReservationType type) {
		this.type = type;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getReservedId() {
		return reservedId;
	}

	public void setReservedId(UUID reservedId) {
		this.reservedId = reservedId;
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

	public LocalDateTime getStarttime() {
		return starttime;
	}

	public void setStarttime(LocalDateTime starttime) {
		this.starttime = starttime;
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

	@Override
	public int hashCode() {
		return Objects.hash(cost, duration, id, reservedId, reservedName, starttime, type, username, vacationId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reservation other = (Reservation) obj;
		return Objects.equals(cost, other.cost) && Objects.equals(duration, other.duration)
				&& Objects.equals(id, other.id) && Objects.equals(reservedId, other.reservedId)
				&& Objects.equals(reservedName, other.reservedName) && Objects.equals(starttime, other.starttime)
				&& type == other.type && Objects.equals(username, other.username)
				&& Objects.equals(vacationId, other.vacationId);
	}

	@Override
	public String toString() {
		return "Reservation [type=" + type + ", id=" + id + ", reservedId=" + reservedId + ", vacationId=" + vacationId
				+ ", username=" + username + ", reservedName=" + reservedName + ", starttime=" + starttime + ", cost="
				+ cost + ", duration=" + duration + "]";
	}

}
