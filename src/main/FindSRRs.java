package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FindSRRs {
    public static String findSRRs(String string) throws IOException {
        String first_mrak = "b_results";
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
}
