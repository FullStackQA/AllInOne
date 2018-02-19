package pages;


import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends Page {

    public HomePage() {
        initElements(this);
    }
    @FindBy(linkText= "Login")
    WebElement loginLink;



    public void clickonLoginLink()
    {
        loginLink.click();
    }





}
