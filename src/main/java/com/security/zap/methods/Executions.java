package com.security.zap.methods;

import com.security.zap.api.ZapClient;
import com.security.zap.api.model.AnalysisInfo;
import com.security.zap.api.model.AuthenticationInfo;
import com.security.zap.api.report.ZapReport;
import com.security.zap.utils.ZapInfo;
import com.security.zap.utils.boot.Zap;



public class Executions extends  BaseExecutionClass {

    @Override
    public void doExecute()  {
       System.out.println("Starting ZAP analysis at target: " + getTargetUrl());

        ZapInfo zapInfo = buildZapInfo();
        AuthenticationInfo authenticationInfo = buildAuthenticationInfo();
        AnalysisInfo analysisInfo = buildAnalysisInfo();

        ZapClient zapClient = new ZapClient(zapInfo, authenticationInfo);
        try {
            startZap(zapInfo);
            ZapReport zapReport = zapClient.analyze(analysisInfo);
            saveReport(zapReport);
        } finally {
            Zap.stopZap();
        }

        System.out.println("ZAP analysis finished.");
    }

}

