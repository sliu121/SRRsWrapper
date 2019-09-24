package main;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SaveSRRs2Txt {
    public static void saveSRRs(HashMap<String,String> map,int number,String filename) throws IOException {


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
}
