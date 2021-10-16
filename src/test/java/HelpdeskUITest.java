import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import pages.AbstractPage;
import pages.LoginPage;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class HelpdeskUITest {

    private WebDriver driver;

    @Before
    public void setup() throws IOException {
        // Читаем конфигурационный файл в System.properties
        System.getProperties().load(ClassLoader.getSystemResourceAsStream("config.properties"));
        // Создание экземпляра драйвера
        driver = new ChromeDriver();
        // Устанавливаем размер окна браузера, как максимально возможный
        driver.manage().window().maximize();
        // Установим время ожидания для поиска элементов
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        // Установить созданный драйвер для поиска в веб-страницах
        AbstractPage.setDriver(driver);
    }

    @Test
    public void checkEarlyCreateTicketTest() throws IOException {
        driver.get(System.getProperty("page.login.url"));
        System.getProperties().load(ClassLoader.getSystemResourceAsStream("user.properties"));
        LoginPage loginPage = new LoginPage();
        loginPage.login(System.getProperty("user"), System.getProperty("password"));
        Select filterKeyWordSelect = new Select(driver.findElement(By.xpath("//select[@class='custom-select custom-select-sm mb-0']")));
        filterKeyWordSelect.selectByValue("Keywords");
        String expected = "smth was happens wrong";
        driver.findElement(By.id("id_query")).sendKeys(expected);
        driver.findElement(By.xpath("//input[@class='btn btn-primary btn-sm']")).click();
        String actual = driver.findElement(By.xpath("//div[@class='tickettitle']/a")).getText();
        assertEquals(actual.contains(expected), true);
        driver.findElement(By.xpath("//div[@class=\"tickettitle\"]/a")).click();
        assertEquals(driver.findElement(By.xpath("//h3")).getText().contains(expected), true);
        driver.close();
    }
}
