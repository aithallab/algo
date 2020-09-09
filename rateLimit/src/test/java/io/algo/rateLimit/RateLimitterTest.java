package io.algo.rateLimit;

import io.algo.rateLimit.exceptions.ApiNotConfiguredException;
import io.algo.rateLimit.service.RateLimitService;

/**
 * @author Ambareesha
 *
 */
public class RateLimitterTest {

	public static void main(String[] args) throws Exception {
		RateLimitService service = new RateLimitService();

		System.out.println("----------Single API call....----------");
		boolean withinLimit = service.checkLimitOfCall("10.0.0.0", "register");
		System.out.println(isSuccessful(withinLimit));

		System.out.println("----------Overload API call...----------");
		for (int i = 0; i < 20; i++) {
			withinLimit = service.checkLimitOfCall("10.0.0.1", "create");
			System.out.println("Call # " + i + " : " + isSuccessful(withinLimit));
		}

		System.out.println("----------Overload API call with delay...----------");
		for (int i = 0; i < 20; i++) {
			withinLimit = service.checkLimitOfCall("10.0.0.2", "create");
			Thread.sleep(1);
			System.out.println("Call # " + i + " : " + isSuccessful(withinLimit));
		}

		System.out.println("----------Single call, API not congigured....----------");
		try {
			withinLimit = service.checkLimitOfCall("10.0.0.3", "login");
		} catch (ApiNotConfiguredException e) {
			System.out.println("The API is not configured for limiting rate.");
		}

		System.out.println("----------Single call, Configuring API----------");
		service.registerApi("login", 100, 10);
		withinLimit = service.checkLimitOfCall("10.0.0.3", "login");
		System.out.println("Calling newly configured API : " + isSuccessful(withinLimit));

	}

	/**
	 * @param withinLimit
	 * @return
	 */
	private static String isSuccessful(boolean withinLimit) {
		if (withinLimit)
			return "Successful";
		return "Unsuccessful";
	}
}