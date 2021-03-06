package pages;


import com.google.common.base.Function;
import com.utilities.DriverManager;
import com.utilities.Timeout;
import com.utilities.exceptions.ElementNotFoundException;
import com.utilities.exceptions.ElementVisibleException;
import com.utilities.exceptions.PageNotReadyException;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Page {

    WebDriver driver;
    WebDriverWait wait;
    Actions action;
    public JavascriptExecutor jsDriver;

    public Page() {
        driver = DriverManager.getDriver();
        wait = new WebDriverWait(driver, Timeout.element_timeout_in_seconds);
        driver.manage().timeouts().implicitlyWait(1000,TimeUnit.SECONDS);
        action = new Actions(driver);
        jsDriver = (JavascriptExecutor) driver;
    }


    protected Page initElements(Object instance) {
        PageFactory.initElements(driver, instance);
        return this;
    }

    protected Page visit(String url) {
        driver.navigate().to(url);
        waitForPageLoad();
        return this;
    }

    protected Page browserBack() {
        driver.navigate().back();
        waitForPageLoad();
        return this;
    }

    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    protected int numberOfOpenWindows() {
        return driver.getWindowHandles().size();
    }

    protected String getWindowTitleName(){return  driver.getTitle();}

    protected void openANewWindow(String url) throws InterruptedException {
        Set<String> windows = driver.getWindowHandles();
        String parentHandle = driver.getWindowHandle();
        WebDriver parentDriver = driver.switchTo().window(parentHandle);
        ((JavascriptExecutor) parentDriver).executeScript("window.open();");
        Set<String> customerWindow = driver.getWindowHandles();
        customerWindow.removeAll(windows);
        String newWindowHandle = ((String) customerWindow.toArray()[0]);
        driver.switchTo().window(newWindowHandle);
        visit(url);
        maximizeWindow();
    }

    protected void openANewWindow() throws InterruptedException {
        ((JavascriptExecutor) driver).executeScript("window.open();");
    }

    protected void closeBrowser() {
        driver.close();
        driver.quit();
    }



    protected void selectDropdown(WebElement dropdown, String selectVAlue) {
        Select select = new Select(dropdown);
        select.selectByVisibleText(selectVAlue);
    }

    protected void selectDropdown(WebElement dropdown, int index) {
        Select select = new Select(dropdown);
        select.selectByIndex(index);
    }

    protected void closeWindow() {
        driver.close();
        Object[] allWindowNames = driver.getWindowHandles().toArray();
        //switching back to parent window
        if (allWindowNames != null)
            driver.switchTo().window((String) allWindowNames[0]);
    }

    protected Page click(WebElement element) throws ElementNotVisibleException, InterruptedException {
        waitUntilElementIsVisible(element);
        waitUntilElementIsClickable(element);
        element.click();
        return this;
    }



    protected boolean areElementsDisplayed(List<WebElement> elements, String elementName) throws InterruptedException {
        try {
            waitForElementsToBeVisible(elements, elementName);
        } catch (ElementNotVisibleException e) {
            return false;
        } catch (ElementNotFoundException e) {
            return false;
        } catch (StaleElementReferenceException e) {
            return false;
        }
        return true;
    }


    protected boolean isElementNotDisplayed(WebElement element, String elementName) throws InterruptedException {
        try {
            waitForElementToBeInvisible(element, elementName);
        } catch (ElementVisibleException e) {
            return false;
        }
        return true;
    }

    protected boolean isElementNotDisplayed(WebElement element, String elementName, int timeout) throws InterruptedException {
        try {
            waitForElementToBeInvisible(element, elementName, timeout);
        } catch (ElementVisibleException e) {
            return false;
        }
        return true;
    }

    protected boolean isElementNotVisible(WebElement element, int timeout) throws InterruptedException {
        try {
            waitUntilElementGetsInvisible(element, timeout);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    protected boolean isTextPresent(WebElement element, String text) {
        waitForPageLoad();
        return text.equals(element.getText());
    }

    protected String getValue(WebElement element) {
        return element.getAttribute("value");
    }

    protected void enterTextOnBrowser(String text) {
        clearTextOnBrowser();
        new Actions(driver).sendKeys(text).build().perform();
    }

    protected void clearTextOnBrowser() {
        new Actions(driver)
                .sendKeys(Keys.chord(Keys.CONTROL, "a"))
                .build().perform();

    }

    protected Page waitForPageLoad() {

        ExpectedCondition<Boolean> pageLoadCondition = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        JavascriptExecutor js = (JavascriptExecutor) driver;
                        return (js.executeScript("return document.readyState").equals("complete"));

                    }
                };
        wait.until(pageLoadCondition);
        return this;
    }

    protected Page maximizeWindow() {
        //driver.manage().window().maximize();
        waitForPageLoad();
        return this;
    }


    protected Page waitForElementAtIndexToBeVisible(List<WebElement> elementsList, int index, String itemName) throws ElementNotVisibleException, InterruptedException {
        int timeout = Timeout.element_timeout_in_seconds;
        for (int i = 0; i <= 10; i++) {
            try {
                WebElement elementInList = elementsList.get(index);
                scrollIntoView(elementInList);
                if (elementInList.isDisplayed())
                    break;
            } catch (NoSuchElementException e) {
                // do nothing
            } catch (IndexOutOfBoundsException e) {
                //do nothing
            } catch (StaleElementReferenceException e) {
                //do nothing
            }
            Thread.sleep(timeout * 100);
        }
        try {
            if (!elementsList.get(index).isDisplayed()) {
                throw new ElementNotVisibleException(itemName);
            }
        } catch (NoSuchElementException e) {
            throw new ElementNotFoundException(itemName, timeout);
        } catch (IndexOutOfBoundsException e) {
            throw new ElementNotFoundException(itemName, timeout);
        }
        return this;
    }

    protected Page waitForElementsToBeVisible(List<WebElement> elementsList, String itemName) throws ElementNotVisibleException, InterruptedException {
        return waitForElementAtIndexToBeVisible(elementsList, 0, itemName);
    }

    //Do not delete, could be used for future reference
    public void waitForCustomCondition(WebElement elementTobeClicked) {
        FluentWait<WebElement> wait = new FluentWait<WebElement>(elementTobeClicked);
        wait.withTimeout(Timeout.element_timeout_in_seconds,TimeUnit.SECONDS);
        Function<WebElement, Boolean> function = new Function<WebElement, Boolean>()
        {
            public Boolean apply(WebElement ele) {
                String width = ele.getCssValue("width").toString();
                System.out.println("width = " + width);
                if(width.equals("260px"))
                {
                    return true;
                }
                return false;
            }
        };
        wait.until(function);
    }

    protected Page waitForElementToBeClickable(WebElement element, String itemName) throws InterruptedException, ElementNotVisibleException {
        waitForPageLoad();
        WebDriverWait wait = new WebDriverWait(driver, Timeout.element_timeout_in_seconds);
        wait.until(ExpectedConditions.elementToBeClickable(element));
        return this;
    }


    protected void scrollIntoView(WebElement element) {
        JavascriptExecutor jsDriver = (JavascriptExecutor) DriverManager.getDriver();
        jsDriver.executeScript("arguments[0].scrollIntoView(false);", element);
    }

    protected void scrollIntoViewByYLoc(WebElement element) {
        JavascriptExecutor jsDriver = (JavascriptExecutor) DriverManager.getDriver();
        jsDriver.executeScript("window.scrollTo(0, [0]);", element.getLocation().getY());
    }

    protected Page waitForElementToBeInvisible(WebElement element, String itemName) throws InterruptedException {
        int timeout = Timeout.element_timeout_in_seconds;
        return waitForElementToBeInvisible(element, itemName, timeout);
    }

    protected Page waitForElementToBeInvisible(WebElement element, String itemName, int timeoutInSeconds) throws InterruptedException {
        for (int i = 0; i <= 100; i++) {
            try {
                if (!element.isDisplayed())
                    break;
            } catch (NoSuchElementException e) {
                return this;
            } catch (StaleElementReferenceException e) {
                return this;
            }
            Thread.sleep(timeoutInSeconds * 10);
        }
        try {
            if (element.isDisplayed()) {
                throw new ElementVisibleException(itemName, timeoutInSeconds);
            }
        } catch (Exception e) {
            return this;
        }
        return this;
    }

    protected boolean waitUntilElementIsVisible(WebElement elementTobeVisible) {
        wait.until(ExpectedConditions.elementToBeClickable(elementTobeVisible));
        return true;
    }

    protected boolean waitForElementToBeVisible(WebElement elementTobeVisible, int timeoutInSeconds) {
        try {
            List<WebElement> elements = new ArrayList<WebElement>();
            elements.add(elementTobeVisible);
            WebDriverWait wait = new WebDriverWait(driver,timeoutInSeconds);
            wait.until(ExpectedConditions.visibilityOfAllElements(elements));
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    protected void waitUntilElementGetsInvisible(WebElement elementTobeInvisible) {
        List<WebElement> element = new ArrayList<WebElement>();
        element.add(elementTobeInvisible);
        wait.until(ExpectedConditions.invisibilityOfAllElements(element));
    }

    protected void waitUntilElementGetsInvisible(WebElement elementTobeVisible, int timeoutInSeconds) {
        List<WebElement> elements = new ArrayList<WebElement>();
        elements.add(elementTobeVisible);
        WebDriverWait wait = new WebDriverWait(driver,timeoutInSeconds);
        wait.until(ExpectedConditions.invisibilityOfAllElements(elements));
    }


    protected void waitUntilElementIsClickable(WebElement elementTobeClicked) {
        wait.until(ExpectedConditions.elementToBeClickable(elementTobeClicked));
    }

    protected boolean isElementHasAttribute(WebElement element, String attributeName) {
        String attributeValue = element.getAttribute(attributeName);
        return attributeValue != null;
    }

    protected boolean isElementClickable(WebElement element) {
        try {
            if (element.isDisplayed() && element.isEnabled())
                return true;
        } catch (NoSuchElementException e) {
            return false;
        }
        return false;
    }

    protected boolean isElementVisible(WebElement element) {
        try {
            if (element.isDisplayed())
                return true;
        } catch (NoSuchElementException e) {
            return false;
        }
        return false;
    }

    protected boolean isElementVisible(WebElement element, int timeout) {
        try {
            List<WebElement> elements = new ArrayList<WebElement>();
            elements.add(element);
            WebDriverWait wait = new WebDriverWait(driver,timeout);
            wait.until(ExpectedConditions.visibilityOfAllElements(elements));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean isElementDisabled(WebElement element) {
        return element.getAttribute("class").contains("disable");
    }

    protected boolean isButtonDisabled(WebElement btn) {
        btn.isEnabled();
        return btn.getAttribute("disabled") != null;
    }

    protected boolean isClassNamePresent(WebElement element, String className) {
        String[] classValues = element.getAttribute("class").split(" ");
        for (String classValue : classValues) {
            if (classValue.equalsIgnoreCase(className))
                return true;
        }
        return false;
    }

    protected Page waitForElementToHaveClassNameAttribute(WebElement element, String className) throws InterruptedException {
        int timeout = Timeout.element_timeout_in_seconds;
        for (int i = 0; i <= 100; i++) {
            try {
                if (isClassNamePresent(element, className))
                    break;
            } catch (NoSuchElementException e) {
                throw new NoSuchElementException("Element could not be found: " + element + ". " + e.getMessage());
            }
            Thread.sleep(timeout * 10);
        }
        if (!isClassNamePresent(element, className))
            throw new ElementNotFoundException(String.format("with CSS classname '%s'", className), timeout);
        return this;
    }

    protected Page waitForElementToNotHaveClassNameAttribute(WebElement element, String className) throws InterruptedException {
        int timeout = Timeout.element_timeout_in_seconds;
        for (int i = 0; i <= 100; i++) {
            try {
                if (!isClassNamePresent(element, className))
                    break;
            } catch (NoSuchElementException e) {
                throw new NoSuchElementException("Element could not be found: " + element + ". " + e.getMessage());
            }
            Thread.sleep(timeout * 10);
        }
        if (isClassNamePresent(element, className))
            throw new ElementNotFoundException(String.format("without CSS classname '%s'", className), timeout);
        return this;
    }

    protected boolean isElementEnabled(WebElement element) {
        try {
            if (element.isEnabled())
                return true;
        } catch (NoSuchElementException e) {
            return false;
        }
        return false;
    }

    protected boolean isIFrameEnabled() {
        try {
            if (driver.getPageSource().contains("iframe"))
                return true;
        } catch (NoSuchElementException noSuchElement) {
            noSuchElement.printStackTrace();
        }
        return false;
    }

    protected void waitForPresenceByLocator(By by) {
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    protected WebElement findElementWithAttributeFromList(List<WebElement> listOfElements, String attributeName, String attributeValue) throws InterruptedException {
        for (WebElement element : listOfElements) {
            if ((element.getAttribute(attributeName)).contains(attributeValue))
                return element;
        }
        throw new ElementNotFoundException(String.format("Element with attributeName: %s attributeValue: %s", attributeName, attributeValue));
    }

    protected WebElement findElementWithoutAttributeFromList(List<WebElement> listOfElements, String attributeName, String attributeValue) {
        for (WebElement element : listOfElements) {
            if (!(element.getAttribute(attributeName)).contains(attributeValue))
                return element;
        }
        throw new ElementNotFoundException(String.format("Element with attributeName: %s attributeValue: %s", attributeName, attributeValue));
    }

    protected String getDataIdForElement(WebElement element) {
        return element.getAttribute("data-id");
    }

    protected String getTitleForElement(WebElement element) {
        return element.getAttribute("title");
    }

    protected void clearTextField(WebElement element) {
        element.click();
        element.sendKeys(Keys.BACK_SPACE);
        element.sendKeys(Keys.CONTROL + "a");
        element.sendKeys(Keys.DELETE);
        element.clear();
    }

    protected String getUrl() {
        return driver.getCurrentUrl();
    }

    protected void setSbBaseUrl() {
        driver.get(driver.getCurrentUrl().substring(0, 25));
    }

    protected WebDriver switchToIframeWindow(WebElement frame) {
        return driver.switchTo().frame(frame);
    }

    protected WebDriver switchToParentWindow() {
        return driver.switchTo().parentFrame();
    }

    protected WebElement findElement(By by) {
        return driver.findElement(by);
    }

    protected List<WebElement> findElements(By by) {
        return driver.findElements(by);
    }

    protected WebDriver switchToNextWindow() {
        driver.getWindowHandles().toArray();
        Object[] allWindowNames = driver.getWindowHandles().toArray();
        String lastWindow = (String) allWindowNames[allWindowNames.length - 1];
        return driver.switchTo().window(lastWindow);
    }

    protected WebDriver switchToFirstFrame() {
        return driver.switchTo().frame(1);
    }

    protected Page refreshPage() {
        driver.navigate().refresh();
        waitForPageLoad();
        return this;
    }

    protected void jsWaitTillTrue(JavascriptExecutor jsDriver, final String condition) {
        Wait wait = new FluentWait(jsDriver)
                .withTimeout(Timeout.element_timeout_in_seconds, TimeUnit.SECONDS)
                .pollingEvery(100, TimeUnit.MILLISECONDS)
                .ignoring(WebDriverException.class);

        wait.until(new Function<JavascriptExecutor, Boolean>() {
            public Boolean apply(JavascriptExecutor jsDriver) {
                return (Boolean) jsDriver.executeScript(condition);
            }
        });
    }

    protected boolean verifyIfPageConditionTrue(Object o, Boolean condition, String message) throws PageNotReadyException {
        if (!condition)
            throw new PageNotReadyException(o, message);
        return true;
    }

    protected String getElementAttribute(WebElement element, String attributeName){
        return element.getAttribute(attributeName);
    }
}
