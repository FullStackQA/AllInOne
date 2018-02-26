package com.security.zap.utils.boot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.security.zap.utils.ZapInfo;

/**
 * Utility class to help with ZAP related tasks (start and stop ZAP, run ZAP Docker image).
 *
 */
public final class Zap {

	private static final Logger LOGGER = LoggerFactory.getLogger(Zap.class);
	
	private static ZapBoot zap;
	
	public static void startZap(ZapInfo zapInfo) {
		zap = ZapBootFactory.makeZapBoot(zapInfo);
		LOGGER.debug("ZAP will be started by: [{}].", zap.getClass().getSimpleName());
		
		zap.startZap(zapInfo);
	}
	
	public static void stopZap() {
		if (zap != null) {
			zap.stopZap();
		}
	}
	
	private Zap() {}
	
}
