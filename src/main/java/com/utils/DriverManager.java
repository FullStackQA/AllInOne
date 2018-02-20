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
        if (driver != null) {
        driver.quit();
        driver = null;
        }
    }
    public static WebDriver createChromeDriver(boolean clearCache) {
        if (PlatformUtil.isMac()) {
            System.setProperty("webdriver.chrome.driver", "src/main/resources/drivers/chromedriver");
        }
        DesiredCapabilities capabilities= HarGenerator.desiredCapabilities();
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
