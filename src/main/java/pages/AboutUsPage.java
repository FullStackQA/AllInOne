package pages;

public class AboutUsPage extends Page{
    public AboutUsPage() {
        initElements(this);
    }

    public String getPageUrl(){
        return driver.getCurrentUrl();
    }

}
