package main;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class connectBing {
    public static WebDriver connectwBing(String input){
        String url = "https://www.bing.com";
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);

        WebDriver browser = new ChromeDriver(options);

        browser.get(url);
        WebElement search_box = browser.findElement(By.id("sb_form_q"));
        search_box.sendKeys(input);
        search_box.sendKeys(Keys.ENTER);

        return browser;
    }
}
