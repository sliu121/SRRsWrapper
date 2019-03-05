package main;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.chrono.IsoChronology;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Scanner;


public class main {
    public static void main(String[] args) throws IOException {
        System.out.println("Input the website you want to check:");
        Scanner url = new Scanner(System.in);
        String link = url. nextLine();
        connectwBing(link);
        File file = new File("save.html");
        String string = findSRRs(file);
        if(string.isEmpty()) System.out.println(string);
        HashMap<Integer,String> sep_SRR;

        sep_SRR = DivideSRRs(string);
//        System.out.println(sep_SRR.size());
//        I will write a function here to check whether the result is different every time.**/
//        saveSRRs(sep_SRR);

        HashMap<String,String > title_link;

        title_link = GetFinalResults(sep_SRR);

//        System.out.println(title_link);

        sep_SRR.clear();
        title_link.clear();
//        for(int i = 0; i<sep_SRR.size();i++){
//            System.out.println(i + sep_SRR.get(i));
//        }
//       System.out.println(sep_SRR.size());

    }
/**
 * In this part, I will save SRRs in a text file.
 * **/
//    private static void saveSRRs(HashMap<Integer, String> sep_srr) throws IOException {
//        FileWriter out = new FileWriter("new.txt",false);
//        for(int i= 0;i<sep_srr.size();i++){
//            out.write(sep_srr.get(i));
//        }
//        out.close();
//    }

    /**just a function that connect and save the html file**/
    public static void connectwBing(String string){
        try{
//            URL bing = new URL("https://www.bing.com/search?q=metasearch&qs=n&form=QBLH&sp=-1&pq=metasearch&sc=8-10&sk=&cvid=AC81497FB0FC4BA1A972470454753FF2");
            URL bing = new URL(string);
            URLConnection connectwithbing = bing.openConnection();
            BufferedReader res_1=new BufferedReader(new InputStreamReader(connectwithbing.getInputStream()));
            String res;
            BufferedWriter write2file = new BufferedWriter(new FileWriter("save.html",false));
            while((res = res_1.readLine())!= null){
//                System.out.println("It's writing.");
                write2file.write(res);
                write2file.flush();
//               System.out.println(res);
            }
            write2file.close();
        }  catch (MalformedURLException e) {
            System.out.println("Wrong URL");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Connection Failed in first try");
            e.printStackTrace();
        }
    }

    /**
     *
     * In this part, I will find Search Result Division.
     *
     * **/


    public static String findSRRs(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        try{
            while ((line = reader.readLine())!= null){
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
        }finally {
            reader.close();
        }
        String string = stringBuilder.toString();
//        System.out.println(string);
        String first_mrak = "Search Results";
        String SRRs = Compared2String(string,first_mrak);

        if(SRRs.equals("Wrong str1") || SRRs.equals("Wrong str2") || SRRs.equals("No Substring!")){
            System.out.println(SRRs);
            return null;
        }else {
            return SRRs;
        }

    }
    public static String Compared2String(String str1, String str2){
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

    /**
     * In this method, I am going to use <h2></h2> as a tag,
     * and I will separate SRRs with the tag,
     * and save the results in hashmap.
     * **/

    public static HashMap<Integer,String> DivideSRRs(String string){
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

    public static HashMap<String,String> GetFinalResults(HashMap<Integer,String> map){
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
                        title = linkWtitle.substring(left_position,i);
                        res.put(title,link);
                        System.out.println((num+1) +" "+ title+" : "+link);
                        ISleft = false;
                        left_position = 0;
                    }
                }
            }
            num++;

        }
        return res;
    }
}
