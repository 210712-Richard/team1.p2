package logout;

import com.intuit.karate.junit5.Karate;

public class LogoutFeature {
	@Karate.Test
	Karate testLogin() {
		return Karate.run("logout").relativeTo(getClass());
	}
}