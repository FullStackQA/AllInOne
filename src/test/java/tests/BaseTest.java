package tests;

import com.utils.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import static com.utils.PropertyReader.getPropertyValue;

public class BaseTest {

    public WebDriver driver;

    @BeforeSuite
    public void getDriver() throws InterruptedException {
        driver = DriverManager.getDriver();
        driver.get(getPropertyValue("URL"));
    }

    // @AfterSuite
    // public void closeBrowser() throws InterruptedException {
    //     DriverManager.tearDown();
    // }
}
