package deleteUser;

import com.intuit.karate.junit5.Karate;

public class DeleteUserFeature {
       @Karate.Test
		Karate testDeleteUser() {
			return Karate.run("deleteUser").relativeTo(getClass());
		}
	}


