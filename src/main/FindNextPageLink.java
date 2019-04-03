package main;

public class FindNextPageLink {
    static public String findnextpagelink(String string,String url){
        String tag_nextpage = "title=\"下一页\"";
        int num = string.indexOf(tag_nextpage);
        string=string.substring(num+tag_nextpage.length());
        int num_right = 0,j = 0,num_left =0;
        String tag_left = "href=\"";
        String tag_right = "\"";

        while(j<string.length()){
            String test = string.substring(j,j+tag_left.length());
            if(test.equals(tag_left)){
                for(int i = j+tag_left.length();i<string.length();i++){
                    if(string.substring(i,i+tag_right.length()).equals(tag_right)){
                        num_left = j;
                        num_right = i;
                        break;
                    }
                }
                break;
            }
            j++;
        }
//        System.out.println(string.substring(num,num_right));
        return url+string.substring(num_left+tag_left.length(),num_right);

    }
}
