package com.security.zap.api.analysis;

import com.security.zap.api.model.AnalysisInfo;
import com.security.zap.api.report.ZapReport;
import org.zaproxy.clientapi.core.ClientApi;

public class ActiveScanOnlyAnalyzer extends BaseAnalyzer {

	public ActiveScanOnlyAnalyzer(String apiKey, ClientApi api) {
		super(apiKey, api);
	}

	public ZapReport analyze(AnalysisInfo analysisInfo) {
		init(analysisInfo.getAnalysisTimeoutInMillis());
		
		runActiveScan(analysisInfo);

		return generateReport();
	}
	
}
