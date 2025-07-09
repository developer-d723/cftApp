package org.example.file;

import org.example.line.CheckLineFormatResult;
import org.example.line.LineType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Output File Handler Tests")
class OutputFileHandlerTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("Should write lines to their files according to their types")
    void shouldWriteToCorrectFiles_whenDifferentTypes() throws IOException {

        try (OutputFileHandler handler = new OutputFileHandler(tempDir, "", false)) {
            handler.writeToFile(new CheckLineFormatResult("123", LineType.INTEGER));
            handler.writeToFile(new CheckLineFormatResult("4.567", LineType.FLOAT));
            handler.writeToFile(new CheckLineFormatResult("string", LineType.STRING));
            handler.writeToFile(new CheckLineFormatResult("456", LineType.INTEGER));
        }

        Path intFile = tempDir.resolve("integers.txt");
        Path floatFile = tempDir.resolve("floats.txt");
        Path stringFile = tempDir.resolve("strings.txt");

        assertEquals(List.of("123", "456"), Files.readAllLines(intFile));
        assertEquals(List.of("4.567"), Files.readAllLines(floatFile));
        assertEquals(List.of("string"), Files.readAllLines(stringFile));
    }

    @Test
    @DisplayName("Should overwrite existing files by default")
    void shouldOverwriteExistingFiles_whenNotInAppendMode() throws IOException {

        Path floatFile = tempDir.resolve("floats.txt");
        Files.writeString(floatFile, "old_file");

        try (OutputFileHandler handler = new OutputFileHandler(tempDir, "", false)) {
            handler.writeToFile(new CheckLineFormatResult("1.2345", LineType.FLOAT));
        }

        assertEquals(List.of("1.2345"), Files.readAllLines(floatFile));
    }

    @Test
    @DisplayName("Should append to existing files in append mode")
    void shouldAppendToExistingFiles_whenAppendModeIsEnabled() throws IOException {
        Path intFile = tempDir.resolve("integers.txt");

        String initialContent = "1" + System.lineSeparator();
        Files.writeString(intFile, initialContent);

        try (OutputFileHandler handler = new OutputFileHandler(tempDir, "", true)) {
            handler.writeToFile(new CheckLineFormatResult("20", LineType.INTEGER));
            handler.writeToFile(new CheckLineFormatResult("300", LineType.INTEGER));
        }

        assertEquals(List.of("1", "20", "300"), Files.readAllLines(intFile));
    }

    @Test
    @DisplayName("Should create files with the selected prefix")
    void shouldCreateFilesWithPrefix_whenPrefixIsProvided() throws IOException {
        String prefix = "prefix1-";
        try (OutputFileHandler handler = new OutputFileHandler(tempDir, prefix, false)) {
            handler.writeToFile(new CheckLineFormatResult("text", LineType.INTEGER));
        }
        Path expectedFile = tempDir.resolve(prefix + "integers.txt");
        assertTrue(Files.exists(expectedFile));
    }

    @Test
    @DisplayName("Should not create any files for unused data types")
    void shouldNotCreateFiles_forUnusedDataTypes() throws IOException {

        try (OutputFileHandler handler = new OutputFileHandler(tempDir, "", false)) {
            handler.writeToFile(new CheckLineFormatResult("some text", LineType.STRING));
        }

        assertTrue(Files.exists(tempDir.resolve("strings.txt")),
                "String file should be created.");
        assertFalse(Files.exists(tempDir.resolve("integers.txt")),
                "Integer file shouldn't be created.");
        assertFalse(Files.exists(tempDir.resolve("floats.txt")),
                "Float file shouldn't be created.");
    }

    @Test
    @DisplayName("Should automatically create nested output directories")
    void shouldCreateNestedDirectories_forOutputPath() throws IOException {

        Path nestedOutputDir = tempDir.resolve("dir1").resolve("dir2");
        try (OutputFileHandler handler = new OutputFileHandler(nestedOutputDir, "", false)) {
            handler.writeToFile(new CheckLineFormatResult("text1", LineType.INTEGER));
        }

        Path expectedFile = nestedOutputDir.resolve("integers.txt");
        assertTrue(Files.exists(expectedFile), "Handler should create parent directories");
        assertEquals(List.of("text1"), Files.readAllLines(expectedFile));
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    @DisplayName("Should throw IOException when output directory is read-only")
    void shouldThrow_whenOutputDirectoryIsUnReadOnly() throws IOException {

        File readOnlyDir = tempDir.toFile();
        assertTrue(readOnlyDir.setWritable(false),
                "Failed to set directory as read-only.");

        try (OutputFileHandler handler = new OutputFileHandler(tempDir, "", false)) {
            assertThrows(IOException.class, () ->
                            handler.writeToFile(new CheckLineFormatResult("text1", LineType.INTEGER)),
                    "Writing to a read-only directory should throw IOException.");
        } finally {
            readOnlyDir.setWritable(true);
        }
    }

    @Test
    @DisplayName("Should correctly handle writing of an empty line")
    void shouldWriteEmptyLine_whenInputIsEmpty() throws IOException {

        try (OutputFileHandler handler = new OutputFileHandler(tempDir, "", false)) {
            handler.writeToFile(new CheckLineFormatResult("", LineType.STRING));
        }

        Path stringFile = tempDir.resolve("strings.txt");
        List<String> lines = Files.readAllLines(stringFile);
        assertEquals(1, lines.size());
        assertTrue(lines.get(0).isEmpty());
    }

    @Test
    @DisplayName("Should throw IOException if trying to write after closing")
    void shouldThrow_whenWritingAfterClose() throws IOException {

        OutputFileHandler handler = new OutputFileHandler(tempDir, "", false);
        handler.writeToFile(new CheckLineFormatResult("text1", LineType.INTEGER));
        handler.close();

        assertThrows(IOException.class, () ->
                        handler.writeToFile(new CheckLineFormatResult("text2", LineType.INTEGER))
                , "Should not be able to write");
    }
}