package steps;

import org.testng.Assert;
import pages.AboutUsPage;
import pages.ContactUsPage;
import pages.UserHomePage;

import static zap.ZapHelpers.aScanUrl;

public class UserPageSteps {

    public void userHome() throws InterruptedException {
        UserHomePage userHomePage = new UserHomePage();
        AboutUsPage aboutPage = new AboutUsPage();
        ContactUsPage contactPage = new ContactUsPage();
        userHomePage.clickAbout();
        String actualAboutHeader = userHomePage.getAboutUsTabHeader();
        Assert.assertEquals(actualAboutHeader ,"About Us Corporate History & Policy");
        //aScanUrl(aboutPage.getPageUrl(),"ReportForAboutPage");
        userHomePage.clickContactUsButton();
        String actualContactHeader = userHomePage.getContactUsTabHeader();
        Assert.assertEquals(actualContactHeader ,"Contact Us");
        //aScanUrl(contactPage.getPageUrl(),"ReportForContactPage");
    }
}
