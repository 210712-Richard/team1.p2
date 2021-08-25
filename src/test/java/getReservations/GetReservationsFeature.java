package getReservations;

import com.intuit.karate.junit5.Karate;

public class GetReservationsFeature {
	@Karate.Test
	Karate testReserveHotel() {
		return Karate.run("getReservations").relativeTo(getClass());
	}
}
