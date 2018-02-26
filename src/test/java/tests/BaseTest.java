package tests;

import com.utilities.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import static com.utilities.PropertyReader.getPropertyValue;

public class BaseTest {

    public WebDriver driver;


    @BeforeSuite
    public void getDriver() throws InterruptedException {
        driver = DriverManager.getDriver();
        driver.get(getPropertyValue("URL"));
    }

    @AfterSuite
    public void closeBrowser() throws InterruptedException {
        DriverManager.tearDown();
    }
}
