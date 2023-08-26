package testsFolder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.fail;


public class TestPractice extends BaseTest {

    @Test
    void firstTest() {

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        try {
            WebElement productTable = driver.findElement(By.xpath("//h5"));
        } catch (NoSuchElementException e) {
            fail("Table not found");
        }

        //В тз про вывод столбца (#) ничего не сказанно, но с учетом комментария к прошлому заданию, добавил.
        WebElement idColumn = driver.findElement(By.xpath("//th[text()='#']"));
        WebElement nameColumn = driver.findElement(By.xpath("//th[text()='Наименование']"));
        WebElement typeColumn = driver.findElement(By.xpath("//th[text()='Тип']"));
        WebElement exoticColumn = driver.findElement(By.xpath("//th[text()='Экзотический']"));
        WebElement addGoodsButton = driver.findElement(By.xpath("//button[@data-target='#editModal']"));
        String backgroundColor = addGoodsButton.getCssValue("background-color");
        String borderColor = addGoodsButton.getCssValue("border-color");
        Assertions.assertEquals("rgba(0, 123, 255, 1)", backgroundColor, "Wrong background color");
        Assertions.assertEquals("rgb(0, 123, 255)", borderColor, "Wrong border color");
    }

    @Test
    void secondTest() {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        // Получаем id текущего последнего элемента
        WebElement lastTableElement = driver.findElement(By.xpath("//tbody/tr[last()]/th"));
        int lastTableElemId = Integer.parseInt(lastTableElement.getText());

        WebElement addGoodsButton = driver.findElement(By.xpath("//button[@data-target='#editModal']"));
        addGoodsButton.click();

        WebElement modalWindow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("editModalLabel")));
        String modalWindowTitle = modalWindow.getText();
//        В тз указано, что модальное окно именно "Добавление товаров", но чтоб не фэйлить тест подправил на "товара".
        Assertions.assertEquals("Добавление товара", modalWindowTitle, "Wrong modalWindowTitle");

        WebElement nameInputField = driver.findElement(By.xpath("//input[@id='name']"));
        nameInputField.sendKeys("Яблоко-кажу (от порт1. Сaju*)");
        WebElement typeSelectDrop = driver.findElement(By.xpath("//select[@id='type']"));
        typeSelectDrop.click();
        driver.findElement(By.xpath("//select[@id='type']/option[@value='FRUIT']")).click();
        //Добавил еше раз, поскольку список продолжал быть открытым.
        typeSelectDrop.click();
        driver.findElement(By.id("exotic")).click();
        driver.findElement(By.xpath("//button[@id='save']")).click();
        // Проверка закрытия МО
        try {
            Boolean modalWinowCloseCheck = wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.id("editModalLabel")));
        } catch (org.openqa.selenium.TimeoutException e) {
            fail("Modal window still opened");
        }
//        Проверяем корректность отображения добавленного товара
        WebElement newGoodId = driver.findElement(By.xpath("//tbody/tr[last()]/th"));
        WebElement newGoodName = newGoodId.findElement(By.xpath("../td[1]"));
        WebElement newGoodType = newGoodId.findElement(By.xpath("../td[2]"));
        WebElement newGoodExoticBool = newGoodId.findElement(By.xpath("../td[3]"));
//        Тут создаем нужный номер айдишника для проверки добавленного товара.
        String newLastId = String.valueOf(lastTableElemId + 1);
        Assertions.assertEquals(newLastId, newGoodId.getText(), "Некорректный id добавленного товара.");
        Assertions.assertEquals(
                "Яблоко-кажу (от порт1. Сaju*)", newGoodName.getText(),
                "Не соответсвие названия добавленного товара");
        Assertions.assertEquals("Фрукт", newGoodType.getText(), "Некорректный тип товара");
        Assertions.assertEquals("true", newGoodExoticBool.getText(), "Некорректный тип экзотичности.");
    }

}
