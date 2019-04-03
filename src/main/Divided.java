package main;

import java.util.HashMap;

public class Divided {
    public static HashMap<Integer,String> divideSRRs(String string){
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
}
