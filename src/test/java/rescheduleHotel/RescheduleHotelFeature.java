package rescheduleHotel;

import com.intuit.karate.junit5.Karate;

public class RescheduleHotelFeature {
	@Karate.Test
	Karate testReserveHotel() {
		return Karate.run("rescheduleHotel").relativeTo(getClass());
	}
}
