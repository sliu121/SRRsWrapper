package main;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.*;
import java.util.*;


public class Main_for_10k {


    static class Connector{
        /**
         *
         * @param input
         * @return the first page of SRRs of BingSearch.
         */
        private  WebDriver connectwBing(String input){
            String url = "https://www.bing.com";
            ChromeOptions options = new ChromeOptions();
            options.setHeadless(true);

            WebDriver browser = new ChromeDriver(options);
            browser.get(url);

            return InputQuery(input,browser);
        }

        private WebDriver InputQuery(String input, WebDriver browser){
            WebElement search_box = browser.findElement(By.id("sb_form_q"));
            search_box.sendKeys(input);
            search_box.sendKeys(Keys.ENTER);
            return browser;
        }

    }

    static class Ex_Wrapper{
        /**
         * Find SRRs area in web
         **/
        private String findSRRs(String string) throws IOException {
            String first_mrak = "b_results";
            String SRRs = Compared2String(string,first_mrak);

            if(SRRs.equals("Wrong str1") || SRRs.equals("Wrong str2") || SRRs.equals("No Substring!")){
                System.out.println(SRRs);
                return null;
            }else {
                return SRRs;
            }

        }
        private String Compared2String(String str1, String str2){
            if(str1.isEmpty()) return "Wrong str1";
            if(str2.isEmpty()) return "Wrong str2";
            int str2_len = str2.length();
            for(int i = 0;i<str1.length();i++){
                if(str1.substring(i,i+str2_len).equals(str2)){
                    return str1.substring(i,str1.length()-1);
                }
            }
            return "No Substring!";
        }

        /**
         * Find each SRR, and save them  --Using a Set to save them
         * @param string: SRRs area,
         * @return
         **/
        private Set<String> divideSRRs(String string){
            Set<String> res = new HashSet<>();
            String left_side = "<h2>";
            String right_side = "</h2>";
            int i = 0,position= 0,count = 0;
            boolean foundh2head = false;
            while(i<string.length()){
                if(string.substring(i,i+left_side.length()).equals(left_side)){
                    foundh2head = true;
                    position = i+left_side.length();
                }

                if(i+right_side.length()> string.length()) return res;

                if(foundh2head && string.substring(i,i+right_side.length()).equals(right_side)){
                    res.add(string.substring(position,i));
//                System.out.println(count + res.get(count));
                    count++;
                    foundh2head = false;
                    position = 0;
                }
                i++;
            }
            return res;
        }

        /**
         * How about we use a Set to save link only here.
         * @param stringSet: storage of SRRs
         * @return
         **/
        private Set<String> savetitleandlink(Set<String> stringSet){
            Set<String> res = new HashSet<>();
            String link_left = "http";
            String link_right = "\"";

            int left_position = 0;
            boolean ISleft = false;

            for(String linkWtitle:stringSet){
                //Now we separate each SRRs, one part is title and the other is link,
                //All we need is link
                //However, Link is before Title, so we can leave title??

                for(int i = 0;i<linkWtitle.length();i++){
                    if(i+link_left.length() == linkWtitle.length() || i + link_right.length() == linkWtitle.length()) break;
                    if(!ISleft && linkWtitle.substring(i,i+link_left.length()).equals(link_left)){
                        ISleft = true;
                        left_position = i;
                    }

                    if(ISleft && linkWtitle.substring(i,i+link_right.length()).equals(link_right)){
                        res.add(linkWtitle.substring(left_position,i));
                        ISleft = false;
                        left_position = 0;
                    }

                }
            }
            return res;
        }


    }
    private static String chooselevel(int num){
        String file_full = "category_path_3_level.txt";
        String file_2 = "category_path_2_level.txt";
        if(num == 1){
            return file_full;
        }else {
            return file_2;
        }
    }


    /**
     * Save SRRs to file
     * @param stringSet
     * @param number
     * @param catogory
     * @throws IOException
     */


    private static void saveSRRs(Set<String> stringSet,int number,String catogory) throws IOException {
        String file_3_level = "category_SRRs_3_level.csv";
        String file_2_level = "category_SRRs_2_level.csv";
        String file_name;

        if(number == 1){
            file_name = file_3_level;
        }else {
            file_name = file_2_level;
        }

        File cvsfile=new File(file_name);
        catogory = catogory.replaceAll(" "," > ");

        FileOutputStream fos=new FileOutputStream(cvsfile,true);
        PrintWriter printWriter=new PrintWriter(fos);

        for(String link: stringSet){
//            String save = link+ " "+catogory;
//            printWriter.println(save);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(link);
            stringBuilder.append(",");
            stringBuilder.append(catogory);
            stringBuilder.append("\n");
            printWriter.write(stringBuilder.toString());

        }
        printWriter.flush();
        printWriter.close();
        fos.close();
    }

    /**
     * The main function
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");

        Set<String> sep_SRR = new HashSet<>();
        Set<String> store_link = new HashSet<>();
        BufferedReader reader;

/**
 * Choose file name and read it.
 */
        int level_choose = 2;
        String filename = chooselevel(level_choose);
        reader = new BufferedReader(new FileReader(filename));
        String input = reader.readLine();

        while (input!= null){
            int SRRs_count = 0;
            WebDriver browser = new Connector().connectwBing(input);
            while (SRRs_count<= 100){
                Ex_Wrapper wrapper = new Ex_Wrapper();
                String SRRs_area = wrapper.findSRRs(browser.getPageSource());

                sep_SRR = wrapper.divideSRRs(SRRs_area);
                store_link = wrapper.savetitleandlink(sep_SRR);
                saveSRRs(store_link,level_choose, input);

                WebElement link = browser.findElement(By.className("sb_pagN"));
                link.click();
                SRRs_count+=store_link.size();

                sep_SRR.clear();
                store_link.clear();
//                numberofSRRs(level_choose);
            }
            browser.close();
            input = reader.readLine();
        }

    }
}
