package com.revature.beans;

import java.util.Objects;
import java.util.UUID;

public class Car {
	private String location;
	private UUID id;
	private String make;
	private String model;
	private Integer year;
	private String rentalPlace;
	private Double costPerDay;
	private Boolean inUse;

	public Car() {
		super();
		this.setInUse(false);
	}

	public Car(String location, UUID id, String make, String model, Integer year, String rentalPlace,
			Double costPerDay) {
		this();
		this.setLocation(location);
		this.setId(id);
		this.setMake(make);
		this.setModel(model);
		this.setYear(year);
		this.setRentalPlace(rentalPlace);
		this.setCostPerDay(costPerDay);
		
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
		Car other = (Car) obj;
		return Objects.equals(costPerDay, other.costPerDay) && Objects.equals(id, other.id)
				&& Objects.equals(inUse, other.inUse) && Objects.equals(location, other.location)
				&& Objects.equals(make, other.make) && Objects.equals(model, other.model)
				&& Objects.equals(rentalPlace, other.rentalPlace) && Objects.equals(year, other.year);
	}

	@Override
	public String toString() {
		return "Car [location=" + location + ", id=" + id + ", make=" + make + ", model=" + model + ", year=" + year
				+ ", rentalPlace=" + rentalPlace + ", costPerDay=" + costPerDay + ", inUse=" + inUse + "]";
	}

}
