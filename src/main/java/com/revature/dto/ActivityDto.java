package com.revature.dto;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import com.revature.beans.Activity;

@Table("activity")
public class ActivityDto {
	@PrimaryKeyColumn(name = "location", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String location;
	@PrimaryKeyColumn(name = "id", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
	private UUID id;
	private String name;
	private String description;
	private Double cost;
	private Instant date;
	private Integer maxParticipants;

	public ActivityDto() {
		super();
	}

	public ActivityDto(Activity a) {
		this.setLocation(a.getLocation());
		this.setId(a.getId());
		this.setName(a.getName());
		this.setDescription(a.getDescription());
		this.setCost(a.getCost());
		this.setDate(a.getDate());
		this.setMaxParticipants(a.getMaxParticipants());
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

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}

	public Integer getMaxParticipants() {
		return maxParticipants;
	}

	public void setMaxParticipants(Integer maxParticipants) {
		this.maxParticipants = maxParticipants;
	}

	public Activity getActivity() {
		Activity a = new Activity();

		a.setLocation(location);
		a.setId(id);
		a.setName(name);
		a.setDescription(description);
		a.setCost(cost);
		a.setDate(date);
		a.setMaxParticipants(maxParticipants);

		return a;
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
		ActivityDto other = (ActivityDto) obj;
		return Objects.equals(cost, other.cost) && Objects.equals(date, other.date)
				&& Objects.equals(description, other.description) && Objects.equals(id, other.id)
				&& Objects.equals(location, other.location) && Objects.equals(maxParticipants, other.maxParticipants)
				&& Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "ActivityDto [location=" + location + ", id=" + id + ", name=" + name + ", description=" + description
				+ ", cost=" + cost + ", date=" + date + ", maxParticipants=" + maxParticipants + "]";
	}

}
