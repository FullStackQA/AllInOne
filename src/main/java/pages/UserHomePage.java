package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class UserHomePage extends Page {


    public UserHomePage() {
        initElements(this);
    }

    @FindBy(css= "svg[data-icon=\"user-circle\"]")
    WebElement userIcon;




    public String getUserNameOnScreen()
    {
        waitUntilElementIsVisible(userIcon);
        String userNameOnScreen=userIcon.getAttribute("uib-tooltip");
        return userNameOnScreen;
    }


}
