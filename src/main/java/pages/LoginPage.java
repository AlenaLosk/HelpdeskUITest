package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends AbstractPage {

    private boolean isPasswordRemember;
    private WebElement user;
    private WebElement password;

    public LoginPage() {
        this.user = driver.findElement(By.id("username"));
        this.password = driver.findElement(By.id("password"));
        this.isPasswordRemember = driver.findElement(By.xpath("//input[@type='checkbox']")).isSelected();
    }

    public void login(String user, String password) {
        this.user.sendKeys(user);
        this.password.sendKeys(password);
        driver.findElement(By.xpath("//input[@class='btn btn-lg btn-primary btn-block']")).click();
    }
}
