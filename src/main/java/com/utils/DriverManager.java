package com.utils;


import com.sun.javafx.PlatformUtil;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.proxy.CaptureType;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterTest;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.utils.PropertyReader.getPropertyValue;

public  class DriverManager {
    private static WebDriver driver;
    private static final String CHROME = "chrome";
    static String sFileName = "target/harTest.har";
    public static BrowserMobProxy proxy;

    public static String browserName = getPropertyValue("browser");
    public static boolean cleanCache = Boolean.parseBoolean(getPropertyValue("clearcache"));

    public static void setDriver() {
        if(driver!=null)
            return;

        if (browserName.equalsIgnoreCase(CHROME)) {
            driver =  createChromeDriver(cleanCache);
        }
        else {
            throw new RuntimeException("Unknown WebDriver browser: " + browserName);
        }

        if (cleanCache) {
            driver.manage().deleteAllCookies();
        }

        driver.manage().window().maximize();
    }

    public static WebDriver getDriver() {
        if(driver==null)
            setDriver();
        return driver;
    }

    public static WebDriver getExistingDriverElseReturnNull() {
        return driver;
    }


    public static void tearDown() {
        Har har = proxy.getHar();

        // Write HAR Data in a File
        File harFile = new File(sFileName);
        try {
            har.writeTo(harFile);
        } catch (IOException ex) {
            System.out.println (ex.toString());
            System.out.println("Could not find file " + sFileName);
        }

        if (driver != null) {
        proxy.stop();

        driver.quit();
            driver = null;
        }
    }
    public static WebDriver createChromeDriver(boolean clearCache) {
        if (PlatformUtil.isMac()) {
            System.setProperty("webdriver.chrome.driver", "src/main/resources/drivers/chromedriver");
        }
        DesiredCapabilities capabilities = new DesiredCapabilities();
        proxy = new BrowserMobProxyServer();
        proxy.start(0);

        Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);

        capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
        // enable more detailed HAR capture, if desired (see CaptureType for the complete list)
        proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
        proxy.newHar("siteundertest.com");


        WebDriver driver =  new ChromeDriver(capabilities);
        driver.manage().timeouts().implicitlyWait(Integer.parseInt(getPropertyValue("timeout")), TimeUnit.SECONDS);
        if (clearCache) {
            driver.get("chrome://extensions-frame");
            WebElement checkbox = driver.findElement(By.xpath("//label[@class='incognito-control']/input[@type='checkbox']"));
            if (!checkbox.isSelected()) {
                checkbox.click();
            }
        }
        return driver;
    }

}
