package com.utilities;


import com.sun.javafx.PlatformUtil;
import net.lightbody.bmp.BrowserMobProxy;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.concurrent.TimeUnit;

import static com.utilities.PropertyReader.getPropertyValue;

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
        if (driver != null) {
        driver.quit();
            driver = null;
        }
    }
    public static WebDriver createChromeDriver(boolean clearCache) {
        if (PlatformUtil.isMac()) {
            System.setProperty("webdriver.chrome.driver", "src/main/resources/drivers/chromedriver");
        }

        Proxy proxy = new Proxy();
        proxy.setHttpProxy("localhost:8090");
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability("proxy", proxy);
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
