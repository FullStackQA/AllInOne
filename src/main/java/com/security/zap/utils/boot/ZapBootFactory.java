package com.security.zap.utils.boot;

import com.security.zap.utils.ZapInfo;

/**
 * Factory to create the correct {@link ZapBoot} implementation
 * based on the given {@link ZapInfo} instance.
 * 

 */
public final class ZapBootFactory {

	static ZapBoot makeZapBoot(ZapInfo zapInfo) {
		if (zapInfo.shouldRunWithDocker()) {
			return new ZapDockerBoot();
		}
		if (zapInfo.getPath() != null && !zapInfo.getPath().isEmpty()) {
			return new ZapLocalBoot();
		}
		return new ZapNilBoot();
	}
	
	private ZapBootFactory() {}
	
}
