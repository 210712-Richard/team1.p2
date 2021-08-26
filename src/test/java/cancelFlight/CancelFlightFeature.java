package cancelFlight;

import com.intuit.karate.junit5.Karate;

public class CancelFlightFeature {
		@Karate.Test
		Karate testCreateVacation() {
			return Karate.run("cancelFlight").relativeTo(getClass());
		}
}
