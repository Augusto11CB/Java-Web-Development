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
public class NoteWorkFlowTest {

    private static final String USER_NAME = "augusto";
    private static final String PASSWORD = "augustosPassword";

    private static final String DEFAULT_NOTE_TITLE = "Nice Title";
    private static final String DEFAULT_NOTE_DESC = "Very Nice Description";

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

        this.insertNewNote();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    @Order(1)
    public void testCreateAndDeleteNote() {

        Assertions.assertDoesNotThrow(() -> {
            this.driver.findElement(By.xpath("//th[text()='" + DEFAULT_NOTE_TITLE + "']"));
            this.driver.findElement(By.xpath("//td[text()='" + DEFAULT_NOTE_DESC + "']"));
        });

        WebElement deleteBtn = this.driver.findElement(
                By.xpath("//*[@id=\"userTable\"]/tbody/tr/td[1]/a"));

        this.webDriverWait.until(ExpectedConditions.elementToBeClickable(deleteBtn));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteBtn);


        AccessAndRegisterUtils.goToHome(driver, webDriverWait, "nav-notes-tab");

        Assertions.assertThrows(NoSuchElementException.class, () -> {
            this.driver.findElement(By.xpath("//th[text()='" + DEFAULT_NOTE_TITLE + "']"));
            this.driver.findElement(By.xpath("//td[text()='" + DEFAULT_NOTE_DESC + "']"));
        });
    }

    @Test
    @Order(2)
    public void testUpdateNote() {

        final String newNoteTitle = "Pretty Cool Title";
        final String newNoteDesc = "What a nice description";

        Assertions.assertDoesNotThrow(() -> {
            this.driver.findElement(By.xpath("//th[text()='" + DEFAULT_NOTE_TITLE + "']"));
            this.driver.findElement(By.xpath("//td[text()='" + DEFAULT_NOTE_DESC + "']"));
        });

        WebElement editButton = this.driver.findElement(
                By.xpath("//*[@id='userTable']/tbody/tr/td[1]/button"));

        this.webDriverWait.until(ExpectedConditions.elementToBeClickable(editButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", editButton);

        WebElement noteTitleField = this.driver.findElement(By.id("note-title"));
        this.webDriverWait.until(ExpectedConditions.visibilityOf(noteTitleField));
        noteTitleField.clear();
        noteTitleField.sendKeys(newNoteTitle);

        WebElement noteDescriptionField = this.driver.findElement(By.id("note-description"));
        noteDescriptionField.clear();
        noteDescriptionField.sendKeys(newNoteDesc);

        WebElement noteForm = this.driver.findElement(By.id("noteSubmit"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", noteForm);

        AccessAndRegisterUtils.goToHome(driver, webDriverWait, "nav-notes-tab");

        Assertions.assertDoesNotThrow(() -> {
            this.driver.findElement(By.xpath("//th[text()='" + newNoteTitle + "']"));
            this.driver.findElement(By.xpath("//td[text()='" + newNoteDesc + "']"));
        });
    }

    private void insertNewNote() {

        this.driver.get("http://localhost:" + this.port + "/home");

        WebElement notesTab = this.driver.findElement(By.id("nav-notes-tab"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", notesTab);

        this.webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("note-creation-btn")));

        WebElement noteCreationButton = driver.findElement(By.id("note-creation-btn"));
        Assertions.assertNotNull(noteCreationButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", noteCreationButton);

        WebElement noteTitleField = this.driver.findElement(By.id("note-title"));
        this.webDriverWait.until(ExpectedConditions.visibilityOf(noteTitleField));
        noteTitleField.sendKeys(DEFAULT_NOTE_TITLE);

        WebElement noteDescriptionField = this.driver.findElement(By.id("note-description"));
        noteDescriptionField.sendKeys(DEFAULT_NOTE_DESC);

        WebElement noteSubmitElement = this.driver.findElement(By.id("noteSubmit"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", noteSubmitElement);

        AccessAndRegisterUtils.goToHome(driver, webDriverWait, "nav-notes-tab");
    }


}