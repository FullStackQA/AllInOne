package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class UserHomePage extends Page {


    public UserHomePage() {
        initElements(this);
    }

    @FindBy(css= "svg[data-icon=\"user-circle\"]")
    WebElement userIcon;

    @FindBy(css = ".ribbon-spacer")
    WebElement AboutButton;

    @FindBy(css= "svg[data-icon=\"comment\"]")
    WebElement ContactUsButton;

    @FindBy(css = ".page-header-sm")
    WebElement AboutHeader;

    @FindBy(css = ".page-header-sm")
    WebElement ContactHeader;

    public String getUserNameOnScreen()
    {
        waitUntilElementIsVisible(userIcon);
        String userNameOnScreen=userIcon.getAttribute("uib-tooltip");
        return userNameOnScreen;
    }

    public void clickAbout() throws InterruptedException {
        click(AboutButton);
    }

    public void clickContactUsButton() throws InterruptedException {
        click(ContactUsButton);
    }

    public String getAboutUsTabHeader() {
        waitUntilElementIsVisible(AboutHeader);
        String AboutUsHeader = AboutHeader.getText();
        return AboutUsHeader;
    }

    public String getContactUsTabHeader() {
        waitUntilElementIsVisible(ContactHeader);
        String ContactUsHeader = ContactHeader.getText();
        return ContactUsHeader;
    }

}
