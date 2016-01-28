package de.fhb;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class UI extends Application {
    @Override
    public void start(final Stage stage) {
        stage.setTitle("RESTGen");

        final FileChooser fileChooser = new FileChooser();
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        final Button inputButton = new Button("Input File");
        final Label inputLabel = new Label("Path to all.tmp");
        final Button outputButton = new Button("Output Project");
        final Label outputLabel = new Label("Output project path");
        final Button generateButton = new Button("Generate");
        generateButton.setDisable(true);
        final File[] inputOutputFile = new File[2];

        inputButton.setOnAction(
                e -> {
                    configureFileChooserForTmp(fileChooser);
                    File file = fileChooser.showOpenDialog(stage);
                    if (file != null) {
                        inputLabel.setText(file.getAbsolutePath());
                        inputOutputFile[0] = file;
                        if (inputOutputFile[1] != null){
                            generateButton.setDisable(false);
                        }else{
                            generateButton.setDisable(true);
                        }
                    }
                });

        outputButton.setOnAction(
                e -> {
                    File file = directoryChooser.showDialog(stage);
                    if (file != null) {
                        outputLabel.setText(file.getAbsolutePath());
                        inputOutputFile[1] = file;
                        if (inputOutputFile[0] != null){
                            generateButton.setDisable(false);
                        }else{
                            generateButton.setDisable(true);
                        }
                    }
                });

        generateButton.setOnAction(event -> {
            generate(inputOutputFile[0], inputOutputFile[1]);
        });


        final GridPane inputGridPane = new GridPane();

        GridPane.setConstraints(inputLabel, 0, 0);
        GridPane.setConstraints(inputButton, 0, 1);
        GridPane.setConstraints(outputLabel, 0, 2);
        GridPane.setConstraints(outputButton, 0, 3);
        GridPane.setConstraints(generateButton, 0, 4);
        inputGridPane.setHgap(6);
        inputGridPane.setVgap(6);
        inputGridPane.getChildren().addAll(inputButton, inputLabel, outputButton, outputLabel, generateButton);

        final Pane rootGroup = new VBox(12);
        rootGroup.getChildren().addAll(inputGridPane);
        rootGroup.setPadding(new Insets(12, 12, 12, 12));

        stage.setScene(new Scene(rootGroup));
        stage.setWidth(400);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    private static void configureFileChooserForTmp(
            final FileChooser fileChooser) {
        fileChooser.setTitle("Select all.tmp");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("all.tmp", "all.tmp")
        );
    }

    private void generate(File tmpFile, File output) {
        Utils utils;
        Map<FileType, FolderPackage> fileMetaData;
        BufferedReader bufferedReader;
        BufferedWriter bufferedWriter = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(tmpFile));
            String line;
            File actualFile = null;
            if ((line = bufferedReader.readLine()) != null && !line.isEmpty() && line.charAt(0) == '∑') {
                String meta = line.substring(2);
                utils = new Utils("src/main/java/", meta, output.getAbsolutePath());
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
            Logger.getLogger(
                    UI.class.getName()).log(
                    Level.SEVERE, null, e);
        } catch (IOException e) {
            Logger.getLogger(
                    UI.class.getName()).log(
                    Level.SEVERE, null, e);
        }
    }
}
