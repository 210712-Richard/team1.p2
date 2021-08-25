package staffReschedule;

import com.intuit.karate.junit5.Karate;

public class StaffRescheduleReservation {
	@Karate.Test
	Karate testReserveHotel() {
		return Karate.run("staffRescheduleReservation").relativeTo(getClass());
	}
}
