package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends Page{

    public LoginPage() {
        initElements(this);
    }
        @FindBy(id= "userEmail")
        WebElement username;

        @FindBy(id="userPassword")
        WebElement password;

        @FindBy(id = "loginButton")
        WebElement logInButton;


        public void enterUsername(String userName)
        {
            username.sendKeys(userName);
        }

        public void enterPassword(String password) {
            this.password.sendKeys(password);
        }

        public void clickSubmit() throws InterruptedException {
            click(logInButton);
        }



    }

