package tests.loginTests;

import org.testng.annotations.Test;
import steps.LoginSteps;
import tests.BaseTest;

import static com.utils.PropertyReader.getPropertyValue;

public class LoginTest extends BaseTest {

    @Test
    public void LoginTest() throws InterruptedException {
        LoginSteps steps=new LoginSteps();
        steps.Login(getPropertyValue("username"),getPropertyValue("password"));
    }
}
