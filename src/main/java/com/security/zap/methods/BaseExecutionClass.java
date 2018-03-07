package com.security.zap.methods;

        import com.beust.jcommander.Parameter;
        import com.security.zap.api.model.AnalysisInfo;
        import com.security.zap.api.model.AnalysisType;
        import com.security.zap.api.model.AuthenticationInfo;
        import com.security.zap.api.model.SeleniumDriver;
        import com.security.zap.api.report.ZapReport;
        import com.security.zap.api.report.ZapReportUtil;
        import com.security.zap.utils.ZapInfo;
        import com.security.zap.utils.boot.Zap;


/**
 * Abstract Mojo used as a base for the other ZAP Mojos.
 *
 * @author pdsec
 */
public abstract class BaseExecutionClass {

    // Common
    /**
     * Disables the plug-in execution.
     */
private boolean skip=false;

    // Analysis
    /**
     * URL of the application that will be scanned.
     */
   private String targetUrl;

    /**
     * Starting point URL for the Spider (and AJAX Spider, in case it runs).
     */
    private String spiderStartingPointUrl;

    /**
     * Starting point URL for the Active Scan.
     */
  private String activeScanStartingPointUrl;

    /**
     * The URLs to be set on ZAP's context (absolute or relative).
     */
private String[] context;
 private String[] technologies;

    /**
     * Analysis timeout in minutes.
     */
private int analysisTimeoutInMinutes=50000;

    /**
     * Indicates whether ZAP should execute the AJAX Spider after the default Spider (it can improve the scan on applications that rely on AJAX).
     */
    private boolean shouldRunAjaxSpider;

    /**
     * In case it's true, the Active Scan will not be executed.
     */
    private boolean shouldRunPassiveScanOnly;

    /**
     * Indicates whether a new session should be started on ZAP before the analysis.
     */
private boolean shouldStartNewSession;

    // ZAP
    /**
     * Port where ZAP is running or will run.
     */
    private Integer zapPort;

    /**
     * Host where ZAP is running.
     */
private String zapHost;

    /**
     * API key needed to access ZAP's API, in case it's enabled.
     */
private String zapApiKey;

    /**
     * Absolute path where ZAP is installed, used to automatically start ZAP.
     */
 private String zapPath;

    /**
     * JVM options used to launch ZAP.
     */
    private String zapJvmOptions;

    /**
     * Options that will be used to automatically start ZAP.
     */
   private String zapOptions=ZapInfo.DEFAULT_OPTIONS;

    /**
     * Indicates whether ZAP should be automatically started with Docker.
     */
 private boolean shouldRunWithDocker;

    /**
     * ZAP's automatic initialization timeout in milliseconds.
     */
   private Integer initializationTimeoutInMillis=120000;

    /**
     * Absolute or relative path where the generated reports will be saved.
     */
  private String reportPath="/target/zapReports";

    // Authentication
    /**
     * Define the authentication type: 'http', 'form', 'cas' or 'selenium'.
     */
private String authenticationType;

    /**
     * Login page URL.
     */
 private String loginUrl;

    /**
     * Username used in the authentication.
     */
    private String username;

    /**
     * Password used in the authentication.
     */
     private String password;

    /**
     * Used to define any extra parameters that must be passed in the authentication request (e.g. domain=someDomain&amp;param=value)
     */
     private String extraPostData;

    /**
     * Regex that identifies a pattern in authenticated responses (needed to allow re-authentication).
     */
     private String loggedInRegex;

    /**
     * Regex that identifies a pattern in non-authenticated responses (needed to allow re-authentication).
     */
     private String loggedOutRegex;

    /**
     * Define the URLs regexs that will be excluded from the scan.
     */
     private String[] excludeFromScan;

    // CAS
    /**
     * Define the URL of a protected page of the application that will be scanned.
     */
    @Parameter private String[] protectedPages;

    // Form and Selenium
    /**
     * Name of the request parameter that holds the username.
     */
  private String usernameParameter="username";

    /**
     * Name of the request parameter that holds the password.
     */
private String passwordParameter="password";

    /**
     * Any additional session tokens that should be added to ZAP prior authentication.
     */
private String[] httpSessionTokens;

    /**
     * The web driver that will be used to perform authentication: 'htmlunit', 'firefox', or 'phantomjs'.
     */
    private String seleniumDriver="chrome";

    // HTTP
    /**
     * The host name of the server where the authentication is done.
     */
 private String hostname;

    /**
     * The realm the credentials apply to.
     */
private String realm;

    /**
     * The port of the server where the authentication is done.
     */
private int port;

    protected ZapInfo buildZapInfo() {
        return ZapInfo.builder()
                .host   (zapHost)
                .port   (zapPort)
                .apiKey (zapApiKey)
                .path   (zapPath)
                .jmvOptions(zapJvmOptions)
                .options(zapOptions)
                .initializationTimeoutInMillis((long) initializationTimeoutInMillis)
                .shouldRunWithDocker(shouldRunWithDocker)
                .build();
    }

    protected AuthenticationInfo buildAuthenticationInfo() {
        if (authenticationType == null) {
            return null;
        }
        return AuthenticationInfo.builder()
                .type             (authenticationType)
                .loginUrl         (loginUrl)
                .username         (username)
                .password         (password)
                .extraPostData    (extraPostData)
                .loggedInRegex    (loggedInRegex)
                .loggedOutRegex   (loggedOutRegex)
                .excludeFromScan  (excludeFromScan)
                .protectedPages   (protectedPages)
                .usernameParameter(usernameParameter)
                .passwordParameter(passwordParameter)
                .loginRequestData()
                .httpSessionTokens(httpSessionTokens)
                .seleniumDriver(SeleniumDriver.valueOf(seleniumDriver.toUpperCase()))
                .hostname(hostname)
                .realm(realm)
                .port(port)
                .build();
    }

    protected AnalysisInfo buildAnalysisInfo() {
        AnalysisType analysisType = AnalysisType.WITH_SPIDER;
        if (shouldRunAjaxSpider && shouldRunPassiveScanOnly) {
            analysisType = AnalysisType.SPIDER_AND_AJAX_SPIDER_ONLY;
        } else {
            if (shouldRunAjaxSpider) {
                analysisType = AnalysisType.WITH_AJAX_SPIDER;
            }
            if (shouldRunPassiveScanOnly) {
                analysisType = AnalysisType.SPIDER_ONLY;
            }
        }
        return buildAnalysisInfo(analysisType);
    }

    protected AnalysisInfo buildAnalysisInfo(AnalysisType analysisType) {
        return AnalysisInfo.builder()
                .targetUrl(targetUrl)
                .spiderStartingPointUrl(spiderStartingPointUrl)
                .activeScanStartingPointUrl(activeScanStartingPointUrl)
                .context(context)
                .technologies(technologies)
                .analysisTimeoutInMinutes(analysisTimeoutInMinutes)
                .analysisType(analysisType)
                .shouldStartNewSession(shouldStartNewSession)
                .build();
    }

    protected void saveReport(ZapReport zapReport) {
        if (reportPath != null) {
            ZapReportUtil.saveAllReports(zapReport, reportPath);
        } else {
            ZapReportUtil.saveAllReports(zapReport);
        }
    }

    protected String getTargetUrl() {
        return this.targetUrl;
    }


    public final void execute() {
        if (skip) {
            return;
        }

        doExecute();
    }

    public void startZap(ZapInfo zapInfo)  {
        Zap.startZap(zapInfo);
    }

    public abstract void doExecute();

}

