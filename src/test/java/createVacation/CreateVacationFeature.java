package createVacation;

import com.intuit.karate.junit5.Karate;

public class CreateVacationFeature {
	@Karate.Test
	Karate testCreateVacation() {
		return Karate.run("createVacation").relativeTo(getClass());
	}
}
