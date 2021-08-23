package confirmReservation;

import com.intuit.karate.junit5.Karate;

public class ConfirmReservationFeature {
	@Karate.Test
	Karate testCreateVacation() {
		return Karate.run("confirmReservation").relativeTo(getClass());
	}
}
