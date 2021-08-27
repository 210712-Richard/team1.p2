package rescheduleFlight;

import com.intuit.karate.junit5.Karate;

public class RescheduleFlightFeature {
	@Karate.Test
	Karate testReserveFlight() {
		return Karate.run("rescheduleFlight").relativeTo(getClass());

	}
}
