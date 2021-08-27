package rescheduleCar;

import com.intuit.karate.junit5.Karate;

public class RescheduleCarFeature {
	@Karate.Test
	Karate testRescheduleCar() {
		return Karate.run("rescheduleCar").relativeTo(getClass());
	}
}
