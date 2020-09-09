/**
 * 
 */
package io.algo.rateLimit.service;

import java.util.HashMap;
import java.util.Map;

import io.algo.rateLimit.core.TokenBucket;

/**
 * Singleton class that holds registry for all running rate limits.
 * 
 * @author Ambareesha
 *
 */
public final class RateLimitRegistry {

	private static RateLimitRegistry instance;

	private Map<String, TokenBucket> registry;

	private RateLimitRegistry() {
		registry = new HashMap<String, TokenBucket>();
	}

	/**
	 * Gets singleton instance
	 * 
	 * @return
	 */
	public static synchronized RateLimitRegistry getInstance() {
		if (instance == null)
			instance = new RateLimitRegistry();
		return instance;
	}

	/**
	 * Gets a token
	 * 
	 * @param call
	 * @return
	 */
	public TokenBucket getToken(String call) {
		return registry.get(call);
	}

	/**
	 * Adds a token
	 * 
	 * @param call
	 * @param bucket
	 */
	public void addToken(String call, TokenBucket bucket) {
		registry.put(call, bucket);
	}

}
