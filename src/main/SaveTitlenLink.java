package main;

import java.util.HashMap;

public class SaveTitlenLink {
    public static HashMap<String,String> savetitleandlink(HashMap<Integer,String> map){
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

    public static String replaceJSmark(String string){
        String strongleft ="<strong>";
        String strongright = "</strong>";
        String amp = "&amp;";
        string = string.replaceAll(strongleft,"");
        string = string.replaceAll(strongright,"");
        string = string.replaceAll(amp,"&");

        return string;

    }
}
