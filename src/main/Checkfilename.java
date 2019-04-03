package main;

import java.io.File;
import java.util.Scanner;

public class Checkfilename {
    public static int Isfileexisted(String filename){
        int res = 0;
        File file = new File(filename);
        if(file.exists()){
            System.out.println("The file "+ filename+ " is existed, what do you want to do?");
            System.out.println("1. Keep write SRRs into file.(It will add content in the file in brute way)");
            System.out.println("2. Delete the original file, and save SRRs in the same file");
            System.out.println("3. Skip this search query, and look for next one.");
            System.out.print("Make your choice: ");
            Scanner scanner = new Scanner(System.in);
            res = scanner.nextInt();
        }else {
            return res;
        }

        return res;
    }
}
