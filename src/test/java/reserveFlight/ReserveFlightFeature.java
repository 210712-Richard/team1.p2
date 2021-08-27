package reserveFlight;
import com.intuit.karate.junit5.Karate;

public class ReserveFlightFeature {
	@Karate.Test
	Karate testReserveFlight() {
		return Karate.run("reserveFlight").relativeTo(getClass());

}
}
