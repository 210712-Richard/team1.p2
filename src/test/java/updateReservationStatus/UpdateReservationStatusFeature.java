package updateReservationStatus;

import com.intuit.karate.junit5.Karate;

public class UpdateReservationStatusFeature {
	@Karate.Test
	Karate testUpdateReservationStaff() {
		return Karate.run("updateReservationStaff").relativeTo(getClass());
	}
	
	@Karate.Test
	Karate testUpdateFlightReservation() {
		return Karate.run("updateFlightReservation").relativeTo(getClass());
	}
	
	@Karate.Test
	Karate testUpdateHotelReservation() {
		return Karate.run("updateHotelReservation").relativeTo(getClass());
	}
	
	@Karate.Test
	Karate testUpdateCarReservation() {
		return Karate.run("updateCarReservation").relativeTo(getClass());
	}
	
	@Karate.Test
	Karate testUpdateReservationInvalidStatus() {
		return Karate.run("updateReservationInvalidStatus").relativeTo(getClass());
	}
	
	@Karate.Test
	Karate testUpdateReservationInvalidUser() {
		return Karate.run("updateReservationInvalidStaff").relativeTo(getClass());
	}
}
