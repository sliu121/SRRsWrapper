package main;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.io.*;
import java.util.*;



public class Main_test {
    
    public static boolean confirmnextpage(String string){
        return (string.contains("title=\"下一页\""))? true:false;
    }

    private static void saveSRRs(HashMap<String,String> map,int number,String filename) throws IOException {


        File txtfile=new File(filename);

        FileOutputStream fos=new FileOutputStream(txtfile,true);
        PrintWriter printWriter=new PrintWriter(fos);

        printWriter.println("Page "+number+": ");


        for(Map.Entry<String,String> m :map.entrySet()){
            printWriter.println(m.getKey()+": "+m.getValue());
        }
        printWriter.println("\n********************************************************\n");

        printWriter.flush();
        printWriter.close();
        fos.close();
    }
    
    private static HashMap<String,String> savetitleandlink(HashMap<Integer,String> map){
        HashMap<String,String> res = new HashMap<>();

        String link_left = "http";
        String link_right = "\"";

        String titile_left = "\">";
        String titile_right = "</a>";

        int num = 0, left_position = 0;
        boolean ISleft = false;

        while(num<map.size()){
            String title = "";
            String link = "";
            String linkWtitle = "";
            linkWtitle = map.get(num);

            for(int i = 0;i<linkWtitle.length();i++){
                if(link.isEmpty()){
                    if(i+link_left.length() == linkWtitle.length() || i + link_right.length() == linkWtitle.length()) break;
                    if(!ISleft && linkWtitle.substring(i,i+link_left.length()).equals(link_left)){
                        ISleft = true;
                        left_position = i;
                    }
                    if(ISleft && linkWtitle.substring(i,i+link_right.length()).equals(link_right)){
                        link = linkWtitle.substring(left_position,i);

                        ISleft = false;
                        left_position = 0;
                    }
                } else{
                    if(i+titile_left.length() > linkWtitle.length() || i + titile_right.length() > linkWtitle.length()) break;
                    if(!ISleft && linkWtitle.substring(i,i+titile_left.length()).equals(titile_left))
                    {
                        ISleft = true;
                        left_position = i+titile_left.length();
                    }
                    if(ISleft && linkWtitle.substring(i,i+titile_right.length()).equals(titile_right)){
                        title = replaceJSmark(linkWtitle.substring(left_position,i));
                        res.put(title,link);
//                        System.out.println((num+1) +" "+ title+" : "+link);
                        ISleft = false;
                        left_position = 0;
                    }
                }
            }
            num++;

        }
        return res;
    }
    private static String replaceJSmark(String string){
        String strongleft ="<strong>";
        String strongright = "</strong>";
        String amp = "&amp;";
        string = string.replaceAll(strongleft,"");
        string = string.replaceAll(strongright,"");
        string = string.replaceAll(amp,"&");

        return string;

    }

    private static String findSRRs(String string) throws IOException {
        String first_mrak = "b_results";
        String SRRs = Compared2String(string,first_mrak);

        if(SRRs.equals("Wrong str1") || SRRs.equals("Wrong str2") || SRRs.equals("No Substring!")){
            System.out.println(SRRs);
            return null;
        }else {
            return SRRs;
        }

    }
    private static String Compared2String(String str1, String str2){
        if(str1.isEmpty()) return "Wrong str1";
        if(str2.isEmpty()) return "Wrong str2";
        int str2_len = str2.length();
        int str_diff_len = str1.length() - str2_len;
        for(int i = 0;i<str_diff_len;i++){
            if(str1.substring(i,i+str2_len).equals(str2)){
                return str1.substring(i,str1.length()-1);
            }
        }
        return "No Substring!";
    }
    
    private static HashMap<Integer,String> divideSRRs(String string){
        HashMap<Integer,String> res = new HashMap<>();
        String left_side = "<h2>";
        String right_side = "</h2>";
        int i = 0,count = 0,position= 0;
        boolean foundh2head = false;
        while(i<string.length()){
            if(string.substring(i,i+left_side.length()).equals(left_side)){
                foundh2head = true;
                position = i+left_side.length();
            }

            if(i+right_side.length()> string.length()) return res;

            if(foundh2head && string.substring(i,i+right_side.length()).equals(right_side)){
                res.put(count,string.substring(position,i));
//                System.out.println(count + res.get(count));
                count++;
                foundh2head = false;
                position = 0;
            }
            i++;
        }
        return res;
    }

    private static void deletefile(String filename){
        File file = new File(filename);
        file.delete();
    }

    private static WebDriver connectwBing(String input){
        String url = "https://www.bing.com";
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(false);

        WebDriver browser = new ChromeDriver(options);

        browser.get(url);
        WebElement search_box = browser.findElement(By.id("sb_form_q"));
        search_box.sendKeys(input);
        search_box.sendKeys(Keys.ENTER);

        return browser;
    }



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
                        String string = findSRRs(browser.getPageSource());
                        if (string.isEmpty()) {
                            System.out.println(string);
                            break;
                        }
                        sep_SRR = divideSRRs(string);

                        title_link = savetitleandlink(sep_SRR);
                        saveSRRs(title_link, page_number, filename);


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
                        String string = findSRRs(browser.getPageSource());
                        if (string.isEmpty()) {
                            System.out.println(string);
                            break;
                        }
                        sep_SRR = divideSRRs(string);

                        title_link = savetitleandlink(sep_SRR);
                        saveSRRs(title_link, page_number, filename);


                        link = browser.findElement(By.className("sb_pagN"));
                        link.click();
//                        if (store_link.contains(browser.getCurrentUrl())) {
//                            break;
//                        } else {
//                            store_link.add(browser.getCurrentUrl());
//                            page_number++;
//                        }
                        page_number++;

                    }
                    sep_SRR.clear();
                    title_link.clear();
                    break;


                case 2:
                    deletefile(filename);
                    page_number = 1;
                    browser = connectwBing(input);
                    store_link.add(browser.getCurrentUrl());

                    while (true) {
                        String string = findSRRs(browser.getPageSource());
                        if (string.isEmpty()) {
                            System.out.println(string);
                            break;
                        }
                        sep_SRR = divideSRRs(string);

                        title_link = savetitleandlink(sep_SRR);
                        saveSRRs(title_link, page_number, filename);


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

