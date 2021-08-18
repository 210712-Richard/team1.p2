package com.revature.dto;

import java.util.Objects;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import com.revature.beans.Car;

@Table("car")
public class CarDto {
	@PrimaryKeyColumn(name = "location", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
	private String location;
	@PrimaryKeyColumn(name = "id", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
	private UUID id;
	private String make;
	private String model;
	private Integer year;
	private String rentalPlace;
	private Double costPerDay;
	private Boolean inUse;

	public CarDto() {
		super();
	}

	public CarDto(Car c) {
		this.setLocation(c.getLocation());
		this.setId(c.getId());
		this.setMake(c.getMake());
		this.setModel(c.getModel());
		this.setYear(c.getYear());
		this.setRentalPlace(c.getRentalPlace());
		this.setCostPerDay(c.getCostPerDay());
		this.setInUse(c.getInUse());
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

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getRentalPlace() {
		return rentalPlace;
	}

	public void setRentalPlace(String rentalPlace) {
		this.rentalPlace = rentalPlace;
	}

	public Double getCostPerDay() {
		return costPerDay;
	}

	public void setCostPerDay(Double costPerDay) {
		this.costPerDay = costPerDay;
	}

	public Boolean getInUse() {
		return inUse;
	}

	public void setInUse(Boolean inUse) {
		this.inUse = inUse;
	}

	public Car getCar() {
		Car c = new Car();

		c.setLocation(location);
		c.setId(id);
		c.setMake(make);
		c.setModel(model);
		c.setYear(year);
		c.setRentalPlace(rentalPlace);
		c.setCostPerDay(costPerDay);
		c.setInUse(inUse);

		return c;
	}

	@Override
	public int hashCode() {
		return Objects.hash(costPerDay, id, inUse, location, make, model, rentalPlace, year);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CarDto other = (CarDto) obj;
		return Objects.equals(costPerDay, other.costPerDay) && Objects.equals(id, other.id)
				&& Objects.equals(inUse, other.inUse) && Objects.equals(location, other.location)
				&& Objects.equals(make, other.make) && Objects.equals(model, other.model)
				&& Objects.equals(rentalPlace, other.rentalPlace) && Objects.equals(year, other.year);
	}

	@Override
	public String toString() {
		return "CarDto [location=" + location + ", id=" + id + ", make=" + make + ", model=" + model + ", year=" + year
				+ ", rentalPlace=" + rentalPlace + ", costPerDay=" + costPerDay + ", inUse=" + inUse + "]";
	}

}
