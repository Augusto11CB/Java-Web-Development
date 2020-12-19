package aug.bueno.cloudstorage.workflow;

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
public class RegistrationAndAcessWorkflowTest {

    private static final String USER_NAME = "augusto";
    private static final String PASSWORD = "augustosPassword";

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private WebDriverWait webDriverWait;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {

        this.driver = new ChromeDriver();
        this.webDriverWait = new WebDriverWait(driver, 5);
    }

    @AfterEach
    public void afterEach() {

        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    public void testHomeNotAccessibleWithoutBeLoggedIn() {

        this.driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    @Order(2)
    public void testLoginWithInvalidCredentials() throws InterruptedException {

        driver.get("http://localhost:" + this.port + "/login");

        Assertions.assertEquals("Login", driver.getTitle());

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            this.driver.findElement(By.id("error-msg"));
        });

        AccessAndRegisterUtils.loginUser(driver, port, PASSWORD, USER_NAME);

        Assertions.assertDoesNotThrow(() -> {
            this.driver.findElement(By.id("error-msg"));
        });
    }

    @Test
    @Order(3)
    public void testCompleteRegistrationAndLoginWorkflow() {

        this.signupWorkflow();
        this.loginWorkflow();

        this.webDriverWait.until(ExpectedConditions.titleContains("Home"));
        Assertions.assertEquals("Home", driver.getTitle());

        WebElement logoutButton = driver.findElement(By.id("logoutButton"));
        Assertions.assertEquals("Logout", logoutButton.getText());
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", logoutButton);

        this.webDriverWait.until(ExpectedConditions.titleContains("Login"));
        Assertions.assertEquals("Login", driver.getTitle());
    }

    private void loginWorkflow() {

        Assertions.assertEquals("Login", driver.getTitle());

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            this.driver.findElement(By.id("error-msg"));
        });

        AccessAndRegisterUtils.loginUser(driver, port, PASSWORD, USER_NAME);

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            this.driver.findElement(By.id("error-msg"));
        });
    }

    private void signupWorkflow() {

        this.driver.get("http://localhost:" + this.port + "/signup");
        Assertions.assertEquals("Sign Up", driver.getTitle());

        Assertions.assertDoesNotThrow(() -> {

            WebElement firstNameField = this.driver.findElement(By.id("inputFirstName"));
            firstNameField.sendKeys("The Answer");

            WebElement lastNameField = this.driver.findElement(By.id("inputLastName"));
            lastNameField.sendKeys("is 42");

            WebElement userNameField = driver.findElement(By.id("inputUsername"));
            userNameField.sendKeys(USER_NAME);

            WebElement passwordField = driver.findElement(By.id("inputPassword"));
            passwordField.sendKeys(PASSWORD);

            WebElement submitButton = driver.findElement(By.id("signUpButton"));
            Assertions.assertEquals("Sign Up", submitButton.getText());
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", submitButton);
        });

        this.webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("login-link")));

        WebElement toLoginBtn = this.driver.findElement(By.id("login-link"));
        Assertions.assertEquals("login", toLoginBtn.getText());
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", toLoginBtn);

        this.webDriverWait.until(ExpectedConditions.titleContains("Login"));
    }
}