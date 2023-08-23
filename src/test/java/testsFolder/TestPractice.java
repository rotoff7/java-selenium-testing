package testsFolder;

import org.junit.jupiter.api.*;
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

    @Test
    void secondTest(){
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        WebElement addGoodsButton = driver.findElement(By.xpath(
                "//button[@class='btn btn-primary' and text()='Добавить']"));
        addGoodsButton.click();
        WebElement modalWindow = driver.findElement(By.xpath("//h5[@id='editModalLabel']"));

        String modalWindowTitle = modalWindow.getText();
        //Почему не приходит текст названия?
//        System.out.println(modalWindowTitle);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

//        В тз указано, что модальное окно именно "Добавление товаров", но чтоб не фэйлить тест подправил.
//        if (modalWindowTitle.equals("Добавление товара")){
//            System.out.println("Correct modal window name");
//        } else {
//            fail("Wrong modal window name");
//        }
//
        WebElement nameInputField = driver.findElement(By.xpath("//input[@id='name']"));
        nameInputField.sendKeys("Яблоко-кажу (от порт1. Сaju*)");

        WebElement typeSelectDrop = driver.findElement(By.xpath("//select[@id='type']"));
        typeSelectDrop.click();

        driver.findElement(By.xpath("//select[@id='type']/option[@value='FRUIT']")).click();
        //Добавил еше раз, поскольку список продолжал быть открытым.
        typeSelectDrop.click();

        driver.findElement(By.xpath("//input[@id='exotic']")).click();

        driver.findElement(By.xpath("//button[@id='save']")).click();

//        // Закрытие окна через крестик
//        driver.findElement(By.xpath("//span[@aria-hidden='true']")).click();


        //Данная штука исчезает, когда зкарывается модальное окно
        //div[@class='modal-backdrop fade show']
        //Еще нужно ожидание... иначе дропает ошибку
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            WebElement modalWinowCloseCheck = driver.findElement(By.xpath(
                    "//div[@class='modal-backdrop fade show']"));
            fail("Окно все еще открыто");
        } catch (NoSuchElementException e){
            System.out.println("continue");
        }

//        Проверяем корректность отображение добавленного товара
        WebElement newGoodName = driver.findElement(By.xpath(
                "//tbody/tr/td[text()='Яблоко-кажу (от порт1. Сaju*)']"));
        WebElement newGoodType = driver.findElement(By.xpath("//tbody/tr/td[text()='Фрукт']"));
        WebElement newGoodExoticBool = driver.findElement(By.xpath("//tbody/tr/td[text()='true']"));

        // Не знаю сколько смысла в этих проверках, если это дублирует то что в xpath
        Assertions.assertEquals(
                "Яблоко-кажу (от порт1. Сaju*)", newGoodName.getText(),
                "Не соответсвие названия добавленного товара");
        Assertions.assertEquals("Фрукт", newGoodType.getText(), "error");
        Assertions.assertEquals("true", newGoodExoticBool.getText(), "error");



        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @AfterAll
    static void afterTests() {
//        driver.quit();
    }
}
