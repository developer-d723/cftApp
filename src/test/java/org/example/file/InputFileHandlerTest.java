package org.example.file;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Input File Handler Tests")
class InputFileHandlerTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("Should process all lines from multiple valid files in correct order")
    void shouldProcessAllLines_whenMultipleFilesAreValid() throws IOException {

        Path file1 = tempDir.resolve("file1.txt");
        Files.write(file1, List.of("line1", "line2"));
        Path file2 = tempDir.resolve("file2.txt");
        Files.write(file2, List.of("line3", "line4", "line5", "line6"));

        InputFileHandler handler = new InputFileHandler(List.of(file1.toFile(), file2.toFile()));
        List<String> processedLines = new ArrayList<>();

        handler.handleLines(processedLines::add);
        assertEquals(6, processedLines.size(), "Should process a total of 6 lines.");
        assertLinesMatch(List.of("line1", "line2", "line3", "line4", "line5", "line6"), processedLines);
    }

    @Test
    @DisplayName("Should skip file when file doesn't exist")
    void shouldSkipFile_whenFileDoesNotExist() {

        File nonExistentFile = tempDir.resolve("missing.txt").toFile();
        InputFileHandler handler = new InputFileHandler(List.of(nonExistentFile));
        List<String> processedLines = new ArrayList<>();

        handler.handleLines(processedLines::add);
        assertTrue(processedLines.isEmpty(), "No lines should be processed.");
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    @DisplayName("Should skip unreadable file")
    void shouldSkipFile_whenFileIsNotReadable() throws IOException {

        Path unreadablePath = tempDir.resolve("unreadable.txt");
        Files.writeString(unreadablePath, "content");
        File unreadableFile = unreadablePath.toFile();

        assertTrue(unreadableFile.setReadable(false), "Failed to set file as unreadable.");

        InputFileHandler handler = new InputFileHandler(List.of(unreadableFile));
        List<String> processedLines = new ArrayList<>();

        try {
            handler.handleLines(processedLines::add);
        } finally {
            unreadableFile.setReadable(true);
        }

        assertTrue(processedLines.isEmpty(),
                "No lines should be processed from an unreadable file.");

    }

    @Test
    @DisplayName("Should process nothing when input file list is empty")
    void shouldProcessNothing_whenFileListIsEmpty() {
        InputFileHandler handler = new InputFileHandler(Collections.emptyList());
        List<String> processedLines = new ArrayList<>();
        handler.handleLines(processedLines::add);
        assertTrue(processedLines.isEmpty());
    }

    @Test
    @DisplayName("Should process nothing when input file list is null")
    void shouldProcessNothing_whenFileListIsNull() {
        InputFileHandler handler = new InputFileHandler(null);
        List<String> processedLines = new ArrayList<>();
        handler.handleLines(processedLines::add);
        assertTrue(processedLines.isEmpty());

    }

    @Test
    @DisplayName("Should continue processing other files after file read error")
    void shouldContinueProcessing_afterFileReadError() throws IOException {

        Path validFile1 = tempDir.resolve("valid1.txt");
        Files.writeString(validFile1, "line1");

        File nonExistentFile = tempDir.resolve("missing.txt").toFile();

        Path validFile2 = tempDir.resolve("valid2.txt");
        Files.writeString(validFile2, "line2");

        InputFileHandler handler = new InputFileHandler(List.of(validFile1.toFile(), nonExistentFile, validFile2.toFile()));
        List<String> processedLines = new ArrayList<>();

        handler.handleLines(processedLines::add);

        assertEquals(2, processedLines.size(),
                "Should process lines only from valid files.");
        assertLinesMatch(List.of("line1", "line2"), processedLines);
    }

    @Test
    @DisplayName("Should correctly handle an empty input file")
    void shouldCorrectlyHandleEmptyFile() throws IOException {
        Path emptyFile = tempDir.resolve("empty.txt");
        Files.createFile(emptyFile);
        Path nonEmptyFile = tempDir.resolve("non_empty.txt");
        Files.writeString(nonEmptyFile, "text1");
        InputFileHandler handler = new InputFileHandler(List.of(emptyFile.toFile(), nonEmptyFile.toFile()));
        List<String> processedLines = new ArrayList<>();
        handler.handleLines(processedLines::add);
        assertEquals(1, processedLines.size());
        assertEquals("text1", processedLines.get(0));
    }
}