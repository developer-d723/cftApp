package org.example.file;

import java.io.*;
import java.util.List;

public class InputFileHandler {
    List<File> inputFiles;

    public InputFileHandler(List<File> inputFiles) {
        this.inputFiles = inputFiles;
    }

    public void process() {
        for (File inputFile : inputFiles) {
            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    //
                }
            } catch (FileNotFoundException e) {
                System.err.println("Error: file not found " + inputFile.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Error reading file " + inputFile.getName() + ": " + e.getMessage());
            }
        }
    }

}
