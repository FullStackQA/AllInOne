package pages;

public class ContactUsPage extends Page {

    public ContactUsPage() {
        initElements(this);
    }
    public String getPageUrl(){
        return driver.getCurrentUrl();
    }

}
