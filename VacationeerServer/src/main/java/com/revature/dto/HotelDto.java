package com.revature.dto;

import java.util.Objects;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import com.revature.beans.Hotel;

@Table("hotel")
public class HotelDto {
	@PrimaryKeyColumn(name = "location", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String location;
	@PrimaryKeyColumn(name = "id", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
	private UUID id;
	private String name;
	private Double costPerNight;
	private Integer roomsAvailable;

	public HotelDto() {
		super();
	}

	public HotelDto(Hotel h) {
		this.location = h.getLocation();
		this.id = h.getId();
		this.name = h.getName();
		this.costPerNight = h.getCostPerNight();
		this.roomsAvailable = h.getRoomsAvailable();
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
	
	public Hotel getHotel() {
		Hotel h = new Hotel();
		
		h.setLocation(location);
		h.setId(id);
		h.setName(name);
		h.setCostPerNight(costPerNight);
		h.setRoomsAvailable(roomsAvailable);
		
		return h;
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
		HotelDto other = (HotelDto) obj;
		return Objects.equals(costPerNight, other.costPerNight) && Objects.equals(id, other.id)
				&& Objects.equals(location, other.location) && Objects.equals(name, other.name)
				&& Objects.equals(roomsAvailable, other.roomsAvailable);
	}

	@Override
	public String toString() {
		return "HotelDto [location=" + location + ", id=" + id + ", name=" + name + ", costPerNight=" + costPerNight
				+ ", roomsAvailable=" + roomsAvailable + "]";
	}

}
