package aug.bueno.cloudstorage.util;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;

public class AccessAndRegisterUtils {

    public static void loginUser(WebDriver driver, int port, String password, String userName) {

        driver.get("http://localhost:" + port + "/login");

        Assertions.assertEquals("Login", driver.getTitle());

        WebElement userNameField = driver.findElement(By.id("inputUsername"));
        userNameField.sendKeys(userName);

        WebElement passwordField = driver.findElement(By.id("inputPassword"));
        passwordField.sendKeys(password);

        WebElement submitButton = driver.findElement(By.id("loginButton"));
        Assertions.assertEquals("Login", submitButton.getText());
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);

    }

    public static void signupUser(WebDriver driver, int port, String password, String userName) {

        driver.get("http://localhost:" + port + "/signup");

        WebElement firsNameField = driver.findElement(By.id("inputFirstName"));
        firsNameField.sendKeys("Name");

        WebElement lastNameField = driver.findElement(By.id("inputLastName"));
        lastNameField.sendKeys("World");

        WebElement userNameField = driver.findElement(By.id("inputUsername"));
        userNameField.sendKeys(userName);

        WebElement pwdField = driver.findElement(By.id("inputPassword"));
        pwdField.sendKeys(password);

        WebElement submitButton = driver.findElement(By.id("signUpButton"));

        Assertions.assertEquals("Sign Up", submitButton.getText());
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", submitButton);
    }
}
