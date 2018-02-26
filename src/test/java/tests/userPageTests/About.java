package tests.userPageTests;

import org.testng.annotations.Test;
import steps.UserPageSteps;
import tests.BaseTest;

public class About extends BaseTest {

    @Test
    public void AboutTest() throws InterruptedException {
        UserPageSteps userPage =new UserPageSteps();
        userPage.userHome();
        System.out.println("Inside About Test");
    }

}
