package org.example;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

class BaseTest {

    static WebDriver driver;

    public static void acceptCookie() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.id("cookie-agree")));
            WebElement cookieButton = driver.findElement(By.id("cookie-agree"));
            cookieButton.click();
        } catch (TimeoutException e) {
            System.out.println("Non");
        }
    }
    @BeforeAll
    static void openWebDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        driver = new ChromeDriver(options);
        driver.get("https://www.mts.by/");
        BaseTest.acceptCookie();
    }
    // @AfterAll
    static void AfterAll() {
        driver.quit();
    }
    @Test
    @DisplayName("Название блока")
    void checkPayFormName() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement h2Element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='pay__wrapper']/h2")));
        Assertions.assertEquals(h2Element.getText(), "Онлайн пополнение\nбез комиссии", "Ошибка в наименовании блока");
    }

    @Test
    @DisplayName("Наличие логотипов платёжных систем")
    void checkPresencePaymentLogo() {
        List<WebElement> logos = driver.findElements(By.xpath("//img[contains(@src, '/local/templates/new_design/assets/html/images/pages/index/pay/')]"));
        Assertions.assertEquals(5, logos.size(), "Ошибка: Логотипы не найдены");
        for (WebElement logo : logos) {
            Assertions.assertTrue(logo.isDisplayed(), "Ошибка: не отображается лого");
        }
    }
    @Test
    @DisplayName("Ссылка «Подробнее о сервисе»")
    void clickMoreInfoLink() {
        WebElement moreInfoLink = driver.findElement(By.linkText("Подробнее о сервисе"));
        moreInfoLink.click();
        Assertions.assertEquals("https://www.mts.by/help/poryadok-oplaty-i-bezopasnost-internet-platezhey/", driver.getCurrentUrl(), "Ссылка «Подробнее о сервисе» не активна");
    }

    @Test
    @DisplayName("Работа формы по кнопке «Продолжить»")
    void testFormSubmission() {
        WebElement phoneNumber = driver.findElement(By.id("connection-phone"));
        phoneNumber.click();
        phoneNumber.sendKeys("297777777");

        WebElement sumTotal = driver.findElement(By.id("connection-sum"));
        sumTotal.click();
        sumTotal.sendKeys("1000");

        WebElement email = driver.findElement(By.id("connection-email"));
        email.click();
        email.sendKeys("romanova@mail.com");

        WebElement continueButton = driver.findElement(By.cssSelector("button.button__default[type='submit']"));
    }
}