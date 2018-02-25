package steps;

import org.zaproxy.clientapi.core.ClientApiException;
import pages.HomePage;
import pages.LoginPage;
import  org.testng.Assert;
import pages.UserHomePage;

import static zap.ZapHelpers.aScanUrl;

public class LoginSteps {


    public void Login(String userName, String password) throws InterruptedException {
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
        aScanUrl(loginPage.getPageUrl());

    }

}
