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
        Utils utils;
        Map<FileType, FolderPackage> fileMetaData;
        File tmpFile = new File("all.tmp");
        BufferedReader bufferedReader;
        BufferedWriter bufferedWriter = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(tmpFile));
            String line;
            File actualFile = null;
            if ((line = bufferedReader.readLine()) != null && !line.isEmpty() && line.charAt(0) == '∑') {
                String meta = line.substring(2);
                utils = new Utils("src/main/java/", meta, "project/");
                fileMetaData = utils.getFileMetaData();
            } else {
                throw new IOException("Illegal file format. Can't parse start meta data correctly.");
            }
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.isEmpty() && line.charAt(0) == '√') {
                    if (bufferedWriter != null) {
                        bufferedWriter.flush();
                    }
                    String[] meta = line.substring(2).split(":");
                    FolderPackage folderPackage = fileMetaData.get(FileType.byString(meta[1]));
                    File actualFolder = folderPackage.getFolder();
                    FileUtils.forceMkdir(actualFolder);
                    actualFile = new File(actualFolder, meta[0]);
                    bufferedWriter = new BufferedWriter(new FileWriterWithEncoding(actualFile, Charset.forName("UTF-8")));
                    continue;
                }
                if (actualFile == null && bufferedWriter == null) {
                    throw new IOException("Illegal file format. Can't parse meta data correctly.");
                }
                bufferedWriter.write(line);
                bufferedWriter.newLine();

            }
            if (bufferedWriter != null) {
                bufferedWriter.flush();
            }
            utils.createMainClass();
            utils.createDataSourceConfiguration();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
