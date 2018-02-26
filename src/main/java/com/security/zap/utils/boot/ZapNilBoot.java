package com.security.zap.utils.boot;

import com.security.zap.utils.ZapInfo;

/**
 * Class to be used as the ZAP booter when ZAP is supposedly up and running.
 * 

 */
public class ZapNilBoot extends AbstractZapBoot {

	@Override
	public void startZap(ZapInfo zapInfo) {}

	@Override
	public void stopZap() {}

}
