package io.algo.rateLimit.config;

/**
 * Class for holding configuration item for the library
 * 
 * @author Ambareesha
 *
 */
public class RateLimitConfig {

	private String api;

	private long maxBucketSize;

	private long refillRate;

	public RateLimitConfig() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param api
	 * @param maxBucketSize
	 * @param refillRate
	 */
	public RateLimitConfig(String api, long maxBucketSize, long refillRate) {
		super();
		this.api = api;
		this.maxBucketSize = maxBucketSize;
		this.refillRate = refillRate;
	}

	/**
	 * @return the api
	 */
	public String getApi() {
		return api;
	}

	/**
	 * @param api the api to set
	 */
	public void setApi(String api) {
		this.api = api;
	}

	/**
	 * @return the maxBucketSize
	 */
	public long getMaxBucketSize() {
		return maxBucketSize;
	}

	/**
	 * @param maxBucketSize the maxBucketSize to set
	 */
	public void setMaxBucketSize(long maxBucketSize) {
		this.maxBucketSize = maxBucketSize;
	}

	/**
	 * @return the refillRate
	 */
	public long getRefillRate() {
		return refillRate;
	}

	/**
	 * @param refillRate the refillRate to set
	 */
	public void setRefillRate(long refillRate) {
		this.refillRate = refillRate;
	}

	@Override
	public String toString() {
		return "RateLimitConfig [api=" + api + ", maxBucketSize=" + maxBucketSize + ", refillRate=" + refillRate + "]";
	}

}
