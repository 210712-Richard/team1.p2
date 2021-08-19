package Register;

import com.intuit.karate.junit5.Karate;

public class RegisterFeature {
	@Karate.Test
	Karate testRegister() {
		return Karate.run("register").relativeTo(getClass());
}
}
