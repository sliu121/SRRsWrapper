package main;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static main.connectBing.connectwBing;

//import java.io.File;


public class main {
    public static void main(String[] args) throws IOException {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

        HashMap<Integer,String> sep_SRR = new HashMap<>();
        HashMap<String,String> title_link = new HashMap<>();
        Set<String> store_link = new HashSet<>();
        BufferedReader reader;

        WebElement link;
        int page_number;
        WebDriver browser;

        reader = new BufferedReader(new FileReader("test.txt"));
        String input = reader.readLine();
        while (input!= null) {

            String filename = input + ".txt";
            int checkfile = Checkfilename.Isfileexisted(filename);

            switch (checkfile) {

                case 0:

                    page_number = 1;
                    browser = connectwBing(input);
                    store_link.add(browser.getCurrentUrl());

                    while (true) {
//            if (pre_link.equals(cur_link)) break;
                        String string = FindSRRs.findSRRs(browser.getPageSource());
                        if (string.isEmpty()) {
                            System.out.println(string);
                            break;
                        }
                        sep_SRR = Divided.divideSRRs(string);

                        title_link = SaveTitlenLink.savetitleandlink(sep_SRR);
                        SaveSRRs2Txt.saveSRRs(title_link, page_number, filename);


                        link = browser.findElement(By.className("sb_pagN"));
                        link.click();
                        if (store_link.contains(browser.getCurrentUrl())) {
                            break;
                        } else {
                            store_link.add(browser.getCurrentUrl());
                            page_number++;
                        }
//            pre_link = cur_link;
//            cur_link = browser.getCurrentUrl();
//            if(pre_link.equals(cur_link)){
//                break;
//            }else{
//                page_number++;
//            }
                    }
                    sep_SRR.clear();
                    title_link.clear();
                    break;


                case 1:

                    page_number = 1;
                    browser = connectwBing(input);
                    store_link.add(browser.getCurrentUrl());

                    while (true) {
                        String string = FindSRRs.findSRRs(browser.getPageSource());
                        if (string.isEmpty()) {
                            System.out.println(string);
                            break;
                        }
                        sep_SRR = Divided.divideSRRs(string);

                        title_link = SaveTitlenLink.savetitleandlink(sep_SRR);
                        SaveSRRs2Txt.saveSRRs(title_link, page_number, filename);


                        link = browser.findElement(By.className("sb_pagN"));
                        link.click();
                        if (store_link.contains(browser.getCurrentUrl())) {
                            break;
                        } else {
                            store_link.add(browser.getCurrentUrl());
                            page_number++;
                        }

                    }
                    sep_SRR.clear();
                    title_link.clear();
                    break;


                case 2:
                    DeleteExistFile.deletefile(filename);
                    page_number = 1;
                    browser = connectwBing(input);
                    store_link.add(browser.getCurrentUrl());

                    while (true) {
                        String string = FindSRRs.findSRRs(browser.getPageSource());
                        if (string.isEmpty()) {
                            System.out.println(string);
                            break;
                        }
                        sep_SRR = Divided.divideSRRs(string);

                        title_link = SaveTitlenLink.savetitleandlink(sep_SRR);
                        SaveSRRs2Txt.saveSRRs(title_link, page_number, filename);


                        link = browser.findElement(By.className("sb_pagN"));
                        link.click();
                        if (store_link.contains(browser.getCurrentUrl())) {
                            break;
                        } else {
                            store_link.add(browser.getCurrentUrl());
                            page_number++;
                        }
//            pre_link = cur_link;
//            cur_link = browser.getCurrentUrl();
//            if(pre_link.equals(cur_link)){
//                break;
//            }else{
//                page_number++;
//            }
                    }
                    sep_SRR.clear();
                    title_link.clear();
                    break;

                case 3:
                    break;

            }

            input = reader.readLine();
        }
        reader.close();
        }

}

