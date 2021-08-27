package com.revature.beans;

public enum ReservationStatus {
	AWAITING("Awaiting"), CONFIRMED("confirmed"), CLOSED("closed"), CANCELLED("cancelled");
	
	private String value;
	
	ReservationStatus(String value) {
		this.value = value;
	}

	public static ReservationStatus getStatus(String value) {
		if(value.equalsIgnoreCase("awaiting") || value.equalsIgnoreCase("pending"))
			return AWAITING;
		
		if(value.equalsIgnoreCase("confirmed") || value.equalsIgnoreCase("confirm"))
			return CONFIRMED;
		
		if(value.equalsIgnoreCase("cancelled") || value.equalsIgnoreCase("cancel"))
			return CANCELLED;
		
		if(value.equalsIgnoreCase("closed") || value.equalsIgnoreCase("close"))
			return CLOSED;
		
		return null;
	  }
}

