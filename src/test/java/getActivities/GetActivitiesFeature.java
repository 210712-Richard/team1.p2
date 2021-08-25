package getActivities;

import com.intuit.karate.junit5.Karate;

public class GetActivitiesFeature {
		@Karate.Test
		Karate testGetActivities() {
			return Karate.run("getActivities").relativeTo(getClass());
		}
	}
