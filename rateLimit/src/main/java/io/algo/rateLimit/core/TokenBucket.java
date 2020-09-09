package io.algo.rateLimit.core;

/**
 * Class to hold a token for each call from a user.
 * 
 * @author Ambareesha
 *
 */
public class TokenBucket {

	private final long maxBucketSize;
	private final long refillRate;

	private long currentBucketSize;
	private long lastRefillTIme;

	/**
	 * Constructor
	 * 
	 * @param maxBucketSize
	 * @param refillRate
	 */
	public TokenBucket(long maxBucketSize, long refillRate) {
		this.maxBucketSize = maxBucketSize;
		this.refillRate = refillRate;

		this.currentBucketSize = maxBucketSize;
		this.lastRefillTIme = System.currentTimeMillis();
	}

	/**
	 * Use one token for a call if one exists.
	 * 
	 * @return TRUE if a token is available for a call, FALSE otherwise
	 */
	public synchronized boolean utilizeToken() {

		refillTokens();

		if (currentBucketSize > 0) {
			currentBucketSize--;
			return true;
		}
		return false;
	}

	/**
	 * Refills the tokens
	 */
	private void refillTokens() {

		long now = System.currentTimeMillis();

		long tokensAccumulated = (now - lastRefillTIme) * refillRate;
		currentBucketSize = Math.min(currentBucketSize + tokensAccumulated, maxBucketSize);

		lastRefillTIme = now;
	}
}
