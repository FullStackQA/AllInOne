package tests.loginTests;

import com.utils.HarGenerator;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import steps.LoginSteps;
import tests.BaseTest;

import java.io.IOException;

import static com.utils.PropertyReader.getPropertyValue;

public class LoginTest extends BaseTest {

    @Test
    public void LoginTest() throws InterruptedException {
        LoginSteps steps=new LoginSteps();
        steps.Login(getPropertyValue("username"),getPropertyValue("password"));
    }

    @AfterClass
    public void tearDown() throws IOException {
//        HarGenerator.harFileGenerator(this.getClass().getSimpleName());
    }
}
