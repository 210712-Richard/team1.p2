package chooseActivities;

import com.intuit.karate.junit5.Karate;

public class ChooseActivities {
	@Karate.Test
	Karate testChooseActivities() {
		return Karate.run("chooseActivities").relativeTo(getClass());
	}

}
