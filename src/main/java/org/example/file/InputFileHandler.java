package org.example.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

/**
 * Reads all input files line by line
 */
public class InputFileHandler {
    private final List<File> inputFiles;

    public InputFileHandler(List<File> inputFiles) {
        this.inputFiles = inputFiles;
    }

    /**
     * Reads all files in turn and for each line
     * performs the action passed to 'lineProcessor'
     * Action is performed in lineProcessor.accept()
     *
     * @param lineProcessor An action (Consumer)
     */

    public void handleLines(Consumer<String> lineProcessor) {
        if (inputFiles == null) return;
        for (File inputFile : inputFiles) {
            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lineProcessor.accept(line);
                }
            } catch (IOException e) {
                System.err.println("Warning: Could not read file '" + inputFile.getPath() + "'. Skipping. Reason: " + e.getMessage());
            }
        }
    }
}