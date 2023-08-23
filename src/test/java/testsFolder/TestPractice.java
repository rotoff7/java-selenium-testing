package testsFolder;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.NoSuchElementException;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.fail;


public class TestPractice {

    private static WebDriver driver;

    @BeforeAll
    static void beforeTests() {

        ChromeOptions co = new ChromeOptions();
        co.setBinary("E:\\TestChrome\\chrome-win64\\chrome.exe");

        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver(co);

        driver.get("http://localhost:8080/food");
        driver.manage().window().maximize();

    }

    @Test
    void firstTest() {

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

//        Попробовал отловить ошибку (только для самой таблицы).
        try {
            WebElement productTable = driver.findElement(By.xpath("//h5[text()='Список товаров']"));
        } catch (NoSuchElementException e) {
            System.out.println("Элемент не найден");
            fail("Table not found");
        }

//        Проверяем наличие элементов и соответсвие наименований.
        WebElement idColumn = driver.findElement(By.xpath("//tr/th[text()='#']"));
        WebElement nameColumn = driver.findElement(By.xpath("//tr/th[text()='Наименование']"));
        WebElement typeColumn = driver.findElement(By.xpath("//tr/th[text()='Тип']"));
        WebElement exoticColumn = driver.findElement(By.xpath("//tr/th[text()='Экзотический']"));
        WebElement addGoodsButton = driver.findElement(By.xpath(
                "//button[@class='btn btn-primary' and text()='Добавить']"));

//        Получаем информацию о цвете кнопки
        String backgroundColor = addGoodsButton.getCssValue("background-color");
        String borderColor = addGoodsButton.getCssValue("border-color");

//        Проверяем соответствие цвета
        if (backgroundColor.equals("rgba(0, 123, 255, 1)") && borderColor.equals("rgb(0, 123, 255)")) {
            System.out.println("Button color is correct");
        } else {
            fail("Wrong button color");
        }

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @AfterAll
    static void afterTests() {
//        driver.quit();
    }
}
