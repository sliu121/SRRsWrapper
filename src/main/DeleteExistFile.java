package main;

import java.io.File;

public class DeleteExistFile {
    public static void deletefile(String filename){
        File file = new File(filename);
        file.delete();
    }
}
