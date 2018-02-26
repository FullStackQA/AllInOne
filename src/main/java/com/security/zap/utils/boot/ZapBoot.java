package com.security.zap.utils.boot;

import com.security.zap.utils.exception.ZapInitializationTimeoutException;
import com.security.zap.utils.ZapInfo;

/**
 * This interface should be implemented by any class capable 
 * of starting and stopping ZAP, no matter how.
 * 

 */
public interface ZapBoot {

	/**
	 * Starts ZAP.
	 * <p>
	 * It should throw {@link ZapInitializationTimeoutException ZapInitializationTimeoutException}
	 * in case ZAP is not started before a timeout, defined by {@code zapInfo.initializationTimeout}
	 * (the default value is {@code 120000}).
	 * 
	 * @param zapInfo an object with all the information needed to start ZAP.
	 */
	void startZap(ZapInfo zapInfo);
	
	/**
	 * Stops ZAP.
	 */
	void stopZap();
	
}
