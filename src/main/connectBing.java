package main;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class connectBing {
    public static WebDriver connectwBing(String input){
            String url = "https://www.bing.com";

//            ChromeDriver browser = new ChromeDriver();
            WebDriver browser = new ChromeDriver();

            browser.get(url);
            WebElement search_box = browser.findElement(By.id("sb_form_q"));
            search_box.sendKeys(input);
            search_box.submit();
//            String content = browser.getPageSource();

            return browser;
    }
}
