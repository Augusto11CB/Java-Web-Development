package aug.bueno;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;
import java.util.concurrent.TimeUnit;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SeleniumTest {
    @LocalServerPort
    private Integer port;
    private static WebDriver driver;

     @BeforeAll
    public static void beforeAll(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }
    @AfterAll
    public static void afterAll(){
        driver.close();
    }
    @Test
    public void testAnimalEndpoint() throws InterruptedException {
        //start the driver, open chrome to our target url
        driver.get("http://localhost:"+port+"/animal");
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        //find the fields we want by id and fill them in
        WebElement inputField = driver.findElement(By.id("animalText"));
        inputField.sendKeys("Manatee");
        inputField = driver.findElement(By.id("adjective"));
        inputField.sendKeys("Whirling");
        List<WebElement> trainingResults = driver.findElements(By.className("trainingMessage"));
        // The field-values don’t clear on submit for our simple app, so just submit it 5 times
        // However, the elements gets removed from the DOM structure after each submit.
        for(int i = 0; i < 5; i++) {
            // We are re-assigning the inputField because this element gets removed from the DOM structure after each iteration.
            // Otherwise, you'll get org.openqa.selenium.StaleElementReferenceException at runtime.
            inputField = driver.findElement(By.id("adjective"));
            inputField.submit();
            System.out.println("trainingResults.size() = " + trainingResults.size());
        }
        // then get the element by the class conclusionMessage and print it
        WebElement conclusionResult = driver.findElement(By.className("conclusionMessage"));
        System.out.println("conclusionResult.getText() = " + conclusionResult.getText());
        Thread.sleep(5000);
    }
}
