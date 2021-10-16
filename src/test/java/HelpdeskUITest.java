import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import pages.AbstractPage;
import pages.LoginPage;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertTrue;

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
        getScreenshot("openTargetPage");
        System.getProperties().load(ClassLoader.getSystemResourceAsStream("user.properties"));
        LoginPage loginPage = new LoginPage();
        loginPage.login(System.getProperty("user"), System.getProperty("password"));
        getScreenshot("enterLoginAndPassword");
        Select filterKeyWordSelect = new Select(driver.findElement(By.xpath("//select[@class='custom-select custom-select-sm mb-0']")));
        filterKeyWordSelect.selectByValue("Keywords");
        String expected = "smth was happens wrong";
        driver.findElement(By.id("id_query")).sendKeys(expected);
        getScreenshot("enterTicketKeywords");
        driver.findElement(By.xpath("//input[@class='btn btn-primary btn-sm']")).click();
        getScreenshot("TableWithTargetTicket");
        String actual = driver.findElement(By.xpath("//div[@class='tickettitle']/a")).getText();
        assertTrue(actual.contains(expected));
        driver.findElement(By.xpath("//div[@class=\"tickettitle\"]/a")).click();
        getScreenshot("TargetTicketDetails");
        assertTrue(driver.findElement(By.xpath("//h3")).getText().contains(expected));
        driver.close();
    }

    public void getScreenshot(String methodName) throws IOException {
        Screenshot screenshot = new AShot().takeScreenshot(driver);
        Date today = new Date();
        File dst = new File(".\\target\\test-screens\\" +
                + today.getDate() + "." + today.getMonth() + "." + (today.getYear()+1900) + "_"
                + today.getHours() + "H" + today.getMinutes() + "M" + today.getSeconds() + "S"
                + "_" + methodName + "_screenshot.png");
        ImageIO.write(screenshot.getImage(), "png", dst);

//        Вариант обычного скриншота
//        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//        Date today = new Date();
//        File dst = new File(".\\target\\test-screens\\" +
//                + today.getDate() + "." + today.getMonth() + "." + (today.getYear()+1900) + "_"
//                + today.getHours() + "H" + today.getMinutes() + "M" + today.getSeconds() + "S"
//                + "_" + methodName + "_screenshot.png");
//        FileHandler.copy(src, dst);
    }
}
