package steps;

import org.testng.Assert;
import pages.UserHomePage;

public class UserPageSteps {

    public void userHome() throws InterruptedException {
        UserHomePage userHomePage = new UserHomePage();
        userHomePage.clickAbout();
        String actualAboutHeader = userHomePage.getAboutUsTabHeader();
        Assert.assertEquals(actualAboutHeader ,"About Us Corporate History & Policy");
        userHomePage.clickContactUsButton();
        String actualContactHeader = userHomePage.getContactUsTabHeader();
        Assert.assertEquals(actualContactHeader ,"Contact Us");
    }
}
