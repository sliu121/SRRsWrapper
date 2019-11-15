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


    static int count;
    static Set<String> pageSet = new HashSet<>();
    static int  Ncount = Integer.MAX_VALUE;
    static int Npages = Integer.MAX_VALUE;
//


    static class Connector{
        /**
         *
         * @param input
         * @return the first page of SRRs of BingSearch.
         */
        private  WebDriver connectwBing(String input){
            String url = "https://www.bing.com";
            ChromeOptions options = new ChromeOptions();
            options.setHeadless(false);

            WebDriver browser = new ChromeDriver(options);
            browser.get(url);

            return InputQuery(input,browser);
        }

        private WebDriver InputQuery(String input, WebDriver browser){
            WebElement search_box = browser.findElement(By.id("sb_form_q"));
            search_box.clear();
            search_box.sendKeys(input);
//            search_box.submit();
            search_box.sendKeys(Keys.ENTER);
            String currentUrl = browser.getCurrentUrl();
            String orginalUrl = "https://www.bing.com/";

            while(currentUrl.hashCode() == orginalUrl.hashCode()){
                WebElement submit = browser.findElement(By.id("sb_form_go"));
                submit.submit();
            }

//            search_box.sendKeys(Keys.ENTER);
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
            int i = 0,position= 0;
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
//                if(count==10) return res;
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

    static class CheckSystem{
        private boolean NoDuplicate(Set<String> set, String pageNo){
            if(set.contains(pageNo)){
                return true;
            }else {
                set.add(pageNo);
                return false;
            }
        }

        private boolean CheckNextPageExist(WebDriver broswer){
            return (broswer.findElements(By.className("sb_pagN")).size()>0);
        }

        private String CheckPageNo(WebDriver browser){
            WebElement CurrentPage = browser.findElement(By.className("sb_pagS"));
            String PageNo= CurrentPage.getAttribute("text");
            return PageNo;
        }

        private boolean CheckQuery(String input, WebDriver browser){
            WebElement search_box = browser.findElement(By.id("sb_form_q"));
            String checkInput = search_box.getAttribute("value");
            if(checkInput.hashCode() == input.hashCode()){
                return true;
            }
            return false;
        }

        private void GivenLimitation(){
            System.out.println("Give system a limitation, which one do you prefer:");
            System.out.println("1. The number of pages;");
            System.out.println("2. The number of SRRs;");
            System.out.println("3. No limitation!");
            Scanner scanner = new Scanner(System.in);
            String res = scanner.next();

            switch (res){
                case "1":
                    System.out.print("you have chosen 1, and the number of pages you want to is?:");
                    scanner = new Scanner(System.in);
                    int pnumber = scanner.nextInt();
                    Npages = pnumber;
                    break;
                case "2":
                    System.out.print("you have chosen 2, and the number of SRRs you want to is?:");
                    scanner = new Scanner(System.in);
                    int snumber = scanner.nextInt();
                    Ncount = snumber;
                    break;
                case "3":
                    System.out.println("you have chosen 3, system will keep processing till the last page.");

                    break;

                    default:
                        System.out.println("Wrong input, system will keep processing till the last page.");

                        break;
            }
        }
    }

    static class GetFileIn{
        private boolean CheckFileName(String filename){
            File file = new File(filename);
            return file.exists();
        }

        private String GetFile(){
            System.out.print("Please input the correct file name with path: ");
            Scanner scanner = new Scanner(System.in);
            String res = scanner.next();
            if(CheckFileName(res)){
                System.out.println("Reading categories from file, System processing");
                return res;
            }else{
                System.out.println("Wrong filename, check your file path or name again.");
                return "";
            }
        }
        public void TNaP(int count, String input, String pageno) throws IOException {
            String fileName = "2ndRe.txt";
            File file= new File(fileName);
            FileOutputStream fos=new FileOutputStream(file,true);
            PrintWriter printWriter=new PrintWriter(fos);

            String res = ("Topic is: " + input + ", total SRRs is: " + count + ", total pages are: " + pageno+".\n");
            printWriter.write(res);

            printWriter.flush();
            printWriter.close();
            fos.close();

        }
    }
    static class SaveToFile{
        // old method, discarded.
        /**
         * Save SRRs to file !!! this function is useless now.
         * @param stringSet
         * @param number
         * @param catogory
         * @throws IOException
         */

        private void saveSRRs(Set<String> stringSet,int number,String catogory) throws IOException {
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

        private String FindOutputFile(){
            System.out.print("Please input the correct file name you want to save the results: ");
            Scanner scanner = new Scanner(System.in);
            String filename = scanner.next();
            File file = new File(filename);
            if(file.exists()){
                System.out.println("The file is found, the results will add content at the bottom of the file");
                return filename;
            }else {
                System.out.println("Can not find the file, System will make a new file.");
                return filename;
            }
        }

        /**
         * Save the result into the given file, one page save once.
         * The file name is supposed to be csv file here
         * @param filename
         */
        private void SaveIntoCSVFile(String filename,Set<String> stringSet,String category) throws IOException {
            File cvsfile=new File(filename);
            category = category.replaceAll(" "," > ");

            FileOutputStream fos=new FileOutputStream(cvsfile,true);
            PrintWriter printWriter=new PrintWriter(fos);

            for(String link: stringSet){
//            String save = link+ " "+catogory;
//            printWriter.println(save);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(link);
                stringBuilder.append(",");
                stringBuilder.append(category);
                stringBuilder.append("\n");
                printWriter.write(stringBuilder.toString());

            }
            printWriter.flush();
            printWriter.close();
            fos.close();
        }
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
 * Input file name and read it.
 */

        String Readinfilename = "test_sample.txt";//new GetFileIn().GetFile();
        while (Readinfilename.isEmpty()){
            Readinfilename = new GetFileIn().GetFile();
        }
        String WriteOutfilename = "category_SRRs.csv";//new SaveToFile().FindOutputFile();
        while (WriteOutfilename.isEmpty()){
            WriteOutfilename = new SaveToFile().FindOutputFile();
        }

        new CheckSystem().GivenLimitation();



        reader = new BufferedReader(new FileReader(Readinfilename));
        String category = reader.readLine();

        while (category!= null){
//            int SRRs_count = 0;

            WebDriver browser = new Connector().connectwBing(category);
            boolean correctQuery = new CheckSystem().CheckQuery(category,browser);
            while (!correctQuery){
                browser = new Connector().InputQuery(category,browser);
                correctQuery = new CheckSystem().CheckQuery(category,browser);
            }
            String pageno = new CheckSystem().CheckPageNo(browser);
            boolean isPageExist = new CheckSystem().NoDuplicate(pageSet,pageno);


            while (!isPageExist && count<=Ncount && Integer.parseInt(pageno) <=Npages-1){
                Ex_Wrapper wrapper = new Ex_Wrapper();
                String SRRs_area = wrapper.findSRRs(browser.getPageSource());

                sep_SRR = wrapper.divideSRRs(SRRs_area);
                store_link = wrapper.savetitleandlink(sep_SRR);
                new SaveToFile().SaveIntoCSVFile(WriteOutfilename,store_link,category);

                 WebElement link = browser.findElement(By.className("sb_pagN"));
                 link.click();
                boolean HasNextPage = new CheckSystem().CheckNextPageExist(browser);


                sep_SRR.clear();
                store_link.clear();
                if(HasNextPage){
                    pageno = new CheckSystem().CheckPageNo(browser);
                    isPageExist = new CheckSystem().NoDuplicate(pageSet,pageno);
                }else{
                    break;
                }
//                SRRs_count+=store_link.size();

            }

            browser.close();
            category = reader.readLine();
            count = 0;
            pageSet.clear();
        }

    }
}
