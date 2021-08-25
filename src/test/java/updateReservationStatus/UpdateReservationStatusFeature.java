package updateReservationStatus;

import com.intuit.karate.junit5.Karate;

public class UpdateReservationStatusFeature {
	@Karate.Test
	Karate testUpdateReservationUser() {
		return Karate.run("UpdateReservationUser").relativeTo(getClass());
	}
	
	@Karate.Test
	Karate testUpdateReservationStaff() {
		return Karate.run("UpdateReservationStaff").relativeTo(getClass());
	}
	
	@Karate.Test
	Karate testUpdateReservationInvalidStatus() {
		return Karate.run("UpdateReservationInvalidStatus").relativeTo(getClass());
	}
	
	@Karate.Test
	Karate testUpdateReservationInvalidUser() {
		return Karate.run("UpdateReservationInvalidStaff").relativeTo(getClass());
	}
}
