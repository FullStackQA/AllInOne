package steps;

import pages.HomePage;
import pages.LoginPage;
import pages.UserHomePage;
import  org.testng.Assert;
import static zap.ZapHelpers.aScanUrl;

public class LoginSteps {


    public void Login(String userName, String password) throws Exception {
        String userNameActual=userName;
        HomePage homePage = new HomePage();
        LoginPage loginPage = new LoginPage();
        UserHomePage userHomePage = new UserHomePage();
        homePage.clickonLoginLink();
        loginPage.enterUsername(userName);
        loginPage.enterPassword(password);
        loginPage.clickSubmit();
        String actualUserName= userHomePage.getUserNameOnScreen();
        Assert.assertEquals( userNameActual,actualUserName);
        aScanUrl(loginPage.getPageUrl(),"ReportForLoginPage");
    }

}
