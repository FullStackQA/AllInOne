package com.security.zap.api.analysis;

import com.security.zap.api.model.AnalysisInfo;
import com.security.zap.api.report.ZapReport;
import org.zaproxy.clientapi.core.ClientApi;

public class WithAjaxSpiderAnalyzer extends BaseAnalyzer {
	
	public WithAjaxSpiderAnalyzer(String apiKey, ClientApi api) {
		super(apiKey, api);
	}
	
	public ZapReport analyze(AnalysisInfo analysisInfo) {
		init(analysisInfo.getAnalysisTimeoutInMillis());
		
		runSpider(analysisInfo);
		runAjaxSpider(analysisInfo);
		runActiveScan(analysisInfo);

		return generateReport();
	}
	
}
