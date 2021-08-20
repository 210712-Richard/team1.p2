package reserveHotel;

import com.intuit.karate.junit5.Karate;

public class ReserveHotelFeature {
	@Karate.Test
	Karate testRegister() {
		return Karate.run("reserveHotel").relativeTo(getClass());
	}
}
