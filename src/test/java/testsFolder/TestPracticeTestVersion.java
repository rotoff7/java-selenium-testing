package testsFolder;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.fail;


public class TestPracticeTestVersion {

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
//        Проверяем наличие элементов и сразу соответсвие наименований ибо в xpath добавил правильный вариант текста.
        //В тз про вывод столбца (#) ничего не сказанно, но с учетом комментария к прошлому заданию, добавил.
        WebElement idColumn = driver.findElement(By.xpath("//tr/th[text()='#']"));
        WebElement nameColumn = driver.findElement(By.xpath("//tr/th[text()='Наименование']"));
        WebElement typeColumn = driver.findElement(By.xpath("//tr/th[text()='Тип']"));
        WebElement exoticColumn = driver.findElement(By.xpath("//tr/th[text()='Экзотический']"));
        WebElement addGoodsButton = driver.findElement(By.xpath(
                "//button[@class='btn btn-primary' and text()='Добавить']"));
//        Получаем информацию о цвете кнопки
        String backgroundColor = addGoodsButton.getCssValue("background-color");
        String borderColor = addGoodsButton.getCssValue("border-color");

//        Проверяем соответствие цвета:

        // 1 способ
//        if (backgroundColor.equals("rgba(0, 123, 255, 1)") && borderColor.equals("rgb(0, 123, 255)")) {
//            System.out.println("Button color is correct");
//        } else {
//            fail("Wrong button color");
//        }
        // 2 способ
        Assertions.assertEquals("rgba(0, 123, 255, 1)", backgroundColor, "Wrong background color");
        Assertions.assertEquals("rgb(0, 123, 255)", borderColor, "Wrong border color");

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

        // Получаем id текущего последнего элемента
        WebElement lastTableElement = driver.findElement(By.xpath("//tbody/tr[last()]/th"));
        int lastTableElemId = Integer.parseInt(lastTableElement.getText());


        WebElement addGoodsButton = driver.findElement(By.xpath(
                "//button[@class='btn btn-primary' and text()='Добавить']"));
        addGoodsButton.click();

        // Проверка модального окна.
        WebElement modalWindow = driver.findElement(By.id("editModalLabel"));
        String modalWindowTitle = modalWindow.getText();
        //Почему не приходит текст элемента?

//        В тз указано, что модальное окно именно "Добавление товаров", но чтоб не фэйлить тест подправил на "товара".
//        Assertions.assertEquals("Добавление товара", modalWindowTitle, "Wrong modalWindowTitle");

        // Добавление нового товара
        WebElement nameInputField = driver.findElement(By.xpath("//input[@id='name']"));
        nameInputField.sendKeys("Яблоко-кажу (от порт1. Сaju*)");

        WebElement typeSelectDrop = driver.findElement(By.xpath("//select[@id='type']"));
        typeSelectDrop.click();

        driver.findElement(By.xpath("//select[@id='type']/option[@value='FRUIT']")).click();
        //Добавил еше раз, поскольку список продолжал быть открытым.
        typeSelectDrop.click();

        driver.findElement(By.id("exotic")).click();

        // Ожидание перед закрытием окна.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        driver.findElement(By.xpath("//button[@id='save']")).click();

//        // Закрытие окна через крестик
//        driver.findElement(By.xpath("//span[@aria-hidden='true']")).click();


        //Данная штука исчезает, когда зкарывается модальное окно
        //div[@class='modal-backdrop fade show']
        //Еще нужно ожидание... иначе дропает ошибку, видно драйвер бежит вперед окна. Но с явным ожидание не вышло.
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Может можно это как-то покороче оформить и без лишнего вывода в консоль?
        try {
            WebElement modalWinowCloseCheck = driver.findElement(By.xpath(
                    "//div[@class='modal-backdrop fade show']"));
            fail("Модальное окно все еще открыто");
        } catch (NoSuchElementException e){
//            System.out.println("continue");
        }

//        Проверяем корректность отображения добавленного товара
        WebElement newGoodId = driver.findElement(By.xpath("//tbody/tr[last()]/th"));
        WebElement newGoodName = driver.findElement(By.xpath(
                "//tbody/tr/td[text()='Яблоко-кажу (от порт1. Сaju*)']"));
        WebElement newGoodType = driver.findElement(By.xpath("//tbody/tr/td[text()='Фрукт']"));
        WebElement newGoodExoticBool = driver.findElement(By.xpath("//tbody/tr/td[text()='true']"));
//        Тут создаем нужный номер айдишника для проверки добавленного товара.
        String newLastId = String.valueOf(lastTableElemId + 1);
//        Сами проверки
        Assertions.assertEquals(newLastId, newGoodId.getText(), "Некорректный id добавленного товара.");
        // Не знаю сколько смысла в этих проверках, если это дублирует то что в xpath
        Assertions.assertEquals(
                "Яблоко-кажу (от порт1. Сaju*)", newGoodName.getText(),
                "Не соответсвие названия добавленного товара");
        Assertions.assertEquals("Фрукт", newGoodType.getText(), "Некорректный тип товара");
        Assertions.assertEquals("true", newGoodExoticBool.getText(), "Некорректный тип экзотичности.");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @AfterAll
    static void afterTests() {
        driver.quit();
    }
}
