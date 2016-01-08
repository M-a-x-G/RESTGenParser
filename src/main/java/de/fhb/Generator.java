package de.fhb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class Generator {
    public static void main(String[] args) {
        Utils utils = new Utils("src/main/", "de/fhb/java/");
        Map<FileType, File> fileFolderPaths = utils.getfileFolderPaths();
        File tmpFile = new File("all.tmp");
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader(tmpFile));
            String line;
            File actualFileFolder = null;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.charAt(0) == '√') {
                    String[] meta = line.substring(1).split(":");
                    actualFileFolder = new File(fileFolderPaths.get(FileType.byString(meta[1]))+ ""+meta[0]);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        String tmpFileString = "";


    }
}
