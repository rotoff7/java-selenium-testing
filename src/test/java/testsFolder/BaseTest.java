package testsFolder;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class BaseTest {
    public static WebDriver driver;

    @BeforeAll
    static void beforeTests() {

        ChromeOptions co = new ChromeOptions();
        co.setBinary("E:\\TestChrome\\chrome-win64\\chrome.exe");

        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver(co);

        driver.get("http://localhost:8080/food");
        driver.manage().window().maximize();
    }

    @AfterAll
    static void afterTests() {
        driver.quit();
    }

}