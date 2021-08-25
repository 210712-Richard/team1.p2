package getActivities;

import com.intuit.karate.junit5.Karate;

public class GetAllActivitiesFeature {
		@Karate.Test
		Karate testGetAllActivities() {
			return Karate.run("getAllActivities").relativeTo(getClass());
		}
	}
