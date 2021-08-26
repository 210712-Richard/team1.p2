package updateReservationStatus;

import com.intuit.karate.junit5.Karate;

public class UpdateReservationStatusFeature {
	@Karate.Test
	Karate testUpdateReservationStaff() {
		return Karate.run("updateReservationStaff").relativeTo(getClass());
	}
	
	@Karate.Test
	Karate testUpdateReservationUser() {
		return Karate.run("updateReservationUser").relativeTo(getClass());
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
