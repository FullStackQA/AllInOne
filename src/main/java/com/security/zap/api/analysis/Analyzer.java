package com.security.zap.api.analysis;

import com.security.zap.api.model.AnalysisInfo;
import com.security.zap.api.report.ZapReport;

public interface Analyzer {

	/**
	 * Runs an analysis of the given target and generates the report. 
	 * 
	 * @param analysisInfo the information about the analysis to be executed.
	 * @return the report of the analysis.
	 */
	ZapReport analyze(AnalysisInfo analysisInfo);
}
