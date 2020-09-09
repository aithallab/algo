/**
 * 
 */
package io.algo.rateLimit.exceptions;

/**
 * @author Ambareesha
 *
 */
public class InvalidConfigurationException extends Exception {

	/**
	 * @param message
	 * @param exception
	 */
	public InvalidConfigurationException(String message, Exception exception) {
		super(message, exception);
	}

}
