package reserveCar;

import com.intuit.karate.junit5.Karate;

public class ReserveCarFeature {
	@Karate.Test
	Karate testRegister() {
		return Karate.run("reserveCar").relativeTo(getClass());
	}
}