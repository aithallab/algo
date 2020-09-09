package io.algo.rateLimit.service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import io.algo.rateLimit.config.RateLimitConfig;
import io.algo.rateLimit.core.TokenBucket;
import io.algo.rateLimit.exceptions.ApiNotConfiguredException;
import io.algo.rateLimit.exceptions.InvalidConfigurationException;

/**
 * @author Ambareesha
 *
 */
public class RateLimitService {

	/**
	 * 
	 */
	private Map<String, RateLimitConfig> rateLimitConfigurations;

	/**
	 * Constructor - Constructs the service
	 * 
	 * @throws InvalidConfigurationException - When the configuration file is not
	 *                                       found or incorrect
	 */
	public RateLimitService() throws InvalidConfigurationException {
		rateLimitConfigurations = new HashMap<String, RateLimitConfig>();

		configure("RateLimitConfig.xml");
	}

	/**
	 * Constructor - Constructs the service
	 * 
	 * @param configFileName - Configuration file
	 * @throws InvalidConfigurationException - When the configuration file is not
	 *                                       found or incorrect
	 */
	public RateLimitService(String configFileName) throws InvalidConfigurationException {
		rateLimitConfigurations = new HashMap<String, RateLimitConfig>();

		configure(configFileName);
	}

	/**
	 * Configures the service
	 * 
	 * @param configFileName Configuration file
	 * @throws InvalidConfigurationException- When the configuration file is not
	 *                                        found or incorrect
	 */
	public void configure(String configFileName) throws InvalidConfigurationException {

		XmlMapper xmlMapper = new XmlMapper();

		try {
			List<RateLimitConfig> configs = Arrays
					.asList(xmlMapper.readValue(new File(configFileName), RateLimitConfig[].class));

			for (RateLimitConfig config : configs) {
				rateLimitConfigurations.put(config.getApi(), config);
			}

		} catch (JsonParseException e) {
			throw new InvalidConfigurationException("Cannot parse configuration file: " + configFileName, e);
		} catch (JsonMappingException e) {
			throw new InvalidConfigurationException("Cannot map values in configuration file: " + configFileName, e);
		} catch (IOException e) {
			throw new InvalidConfigurationException("Cannot fing configuration file: " + configFileName, e);
		}

	}

	/**
	 * Starts monitoring the call to API , limiting calling rate from a user token -
	 * with configured/default values
	 * 
	 * @param userToken - Source of call - can be IP address, or a request header
	 *                  variable
	 * @param api       - The API being called
	 */
	public TokenBucket startMonitoringCall(String userToken, String api) {
		RateLimitConfig config = rateLimitConfigurations.get(api);
		long maxBucketSize = config.getMaxBucketSize();
		long refillRate = config.getRefillRate();

		TokenBucket bucket = new TokenBucket(maxBucketSize, refillRate);
		RateLimitRegistry.getInstance().addToken(userToken + api, bucket);

		return bucket;
	}

	/**
	 * Verifies if limit for the call of API from a source is exceeded
	 * 
	 * @param userToken - Source of API call
	 * @param api       - The API being called
	 * @return TRUE if limit is not exceeded, FALSE otherwise
	 * @throws ApiNotConfiguredException - If the API is not configured for rate
	 *                                   limit
	 */
	public boolean checkLimitOfCall(String userToken, String api) throws ApiNotConfiguredException {

		String call = userToken + api;
		TokenBucket token = RateLimitRegistry.getInstance().getToken(call);

		if (token == null) {
			RateLimitConfig config = rateLimitConfigurations.get(api);
			if (config == null)
				throw new ApiNotConfiguredException(api + " is not configured for rate limit");

			token = startMonitoringCall(userToken, api);
		}

		return token.utilizeToken();
	}

	/**
	 * Registers API for limiting call rate
	 * 
	 * @param api           API to be monitored
	 * @param maxBucketSize Max bucket size
	 * @param refillRate    Rate/ms at which buckets to be filled
	 */
	public void registerApi(String api, long maxBucketSize, long refillRate) {
		rateLimitConfigurations.put(api, new RateLimitConfig(api, maxBucketSize, refillRate));

	}

}