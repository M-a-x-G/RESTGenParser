package de.fhb;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

public class Generator {
    public static void main(String[] args) {
        Utils utils = new Utils("src/main/", "de/fhb/java/", "project/");
        Map<FileType, File> fileFolderPaths = utils.getfileFolderPaths();
        File tmpFile = new File("all.tmp");
        BufferedReader bufferedReader;
        BufferedWriter bufferedWriter = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(tmpFile));
            String line;
            File actualFile = null;
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.isEmpty() && line.charAt(0) == '√') {
                    if (bufferedWriter != null){
                        bufferedWriter.flush();
                    }
                    String[] meta = line.substring(2).split(":");
                    File actualFolder = fileFolderPaths.get(FileType.byString(meta[1]));
                    FileUtils.forceMkdir(actualFolder);
                    actualFile = new File(actualFolder, meta[0]);
                    bufferedWriter = new BufferedWriter(new FileWriterWithEncoding(actualFile, Charset.forName("UTF8")));
                    continue;
                }
                if (actualFile == null && bufferedWriter == null) {
                    throw new IOException("Illegal file format. Can't parse meta data correctly.");
                }
                bufferedWriter.newLine();
                bufferedWriter.write(line);

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
