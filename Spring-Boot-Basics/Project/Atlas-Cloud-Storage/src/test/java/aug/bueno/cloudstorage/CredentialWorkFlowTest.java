package aug.bueno.cloudstorage;

import aug.bueno.cloudstorage.util.AccessAndRegisterUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CredentialWorkFlowTest {

    private static final String USER_NAME = "augusto";
    private static final String PASSWORD = "augustosPassword";

    private static final String DEFAULT_URL = "3w.nicetube.com";
    private static final String DEFAULT_USER_NAME = "default-augusto";
    private static final String DEFAULT_PASSWORD = "default-augustosPassword";

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private WebDriverWait webDriverWait;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() throws InterruptedException {

        this.driver = new ChromeDriver();
        this.webDriverWait = new WebDriverWait(driver, 5);

        AccessAndRegisterUtils.signupUser(driver, port, PASSWORD, USER_NAME);
        AccessAndRegisterUtils.loginUser(driver, port, PASSWORD, USER_NAME);

        this.insertNewCredential();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    public void testCreationAndDeletionOfCredential() {

        Assertions.assertDoesNotThrow(() -> {
            this.driver.findElement(By.xpath("//th[text()='" + DEFAULT_URL + "']"));
            this.driver.findElement(By.xpath("//td[text()='" + DEFAULT_USER_NAME + "']"));
        });

        WebElement deleteButton = this.driver.findElement(
                By.xpath("//*[@id=\"credentialTable\"]/tbody/tr/td[1]/a"));

        this.webDriverWait.until(ExpectedConditions.elementToBeClickable(deleteButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteButton);

        AccessAndRegisterUtils.goToHome(driver, webDriverWait, "nav-credentials-tab");

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            this.driver.findElement(By.xpath("//th[text()='" + DEFAULT_URL + "']"));
            this.driver.findElement(By.xpath("//td[text()='" + DEFAULT_USER_NAME + "']"));
        });
    }

    @Test
    @Order(2)
    public void testUpdateOfCredential() {

        final String newURL = "3w.new-nicetube.com";
        final String newUserName = "new-user-name";

        Assertions.assertDoesNotThrow(() -> {
            this.driver.findElement(By.xpath("//th[text()='" + DEFAULT_URL + "']"));
            this.driver.findElement(By.xpath("//td[text()='" + DEFAULT_USER_NAME + "']"));
        });

        WebElement editButton = this.driver.findElement(
                By.xpath("//*[@id='credentialTable']/tbody/tr/td[1]/button"));
        this.webDriverWait.until(ExpectedConditions.elementToBeClickable(editButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", editButton);

        WebElement urlInputField = this.driver.findElement(By.id("credential-url"));
        this.webDriverWait.until(ExpectedConditions.visibilityOf(urlInputField));
        urlInputField.clear();
        urlInputField.sendKeys(newURL);

        WebElement userNameInputField = this.driver.findElement(By.id("credential-username"));
        userNameInputField.clear();
        userNameInputField.sendKeys(newUserName);

        WebElement credentialForm = this.driver.findElement(By.id("credentialSubmit"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", credentialForm);


        AccessAndRegisterUtils.goToHome(driver, webDriverWait, "nav-credentials-tab");

        Assertions.assertDoesNotThrow(() -> {
            this.driver.findElement(By.xpath("//th[text()='" + newURL + "']"));
            this.driver.findElement(By.xpath("//td[text()='" + newUserName + "']"));
        });
    }

    private void insertNewCredential() {

        this.driver.get("http://localhost:" + this.port + "/home");

        WebElement credentialsTab = this.driver.findElement(By.id("nav-credentials-tab"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", credentialsTab);

        WebElement credentialCreateButton = driver.findElement(By.id("credential-create-btn"));
        this.webDriverWait.until(ExpectedConditions.elementToBeClickable(credentialCreateButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", credentialCreateButton);

        WebElement urlInputField = this.driver.findElement(By.id("credential-url"));
        this.webDriverWait.until(ExpectedConditions.visibilityOf(urlInputField));
        urlInputField.sendKeys(DEFAULT_URL);

        WebElement userNameInputField = this.driver.findElement(By.id("credential-username"));
        userNameInputField.sendKeys(DEFAULT_USER_NAME);

        WebElement passwordInputField = this.driver.findElement(By.id("credential-password"));
        passwordInputField.sendKeys(DEFAULT_PASSWORD);

        WebElement credentialForm = this.driver.findElement(By.id("credentialSubmit"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", credentialForm);

        AccessAndRegisterUtils.goToHome(driver, webDriverWait, "nav-credentials-tab");
    }


}
