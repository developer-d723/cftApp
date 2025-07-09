package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Application Integration Tests")
class AppIntegrationTest {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("Should correctly filter mixed data into separate files")
    void shouldCorrectlyFilterMixedData() throws IOException {

        System.out.println("Should correctly filter mixed data into separate files");
        Path inputFile = tempDir.resolve("input1.txt");
        List<String> inputLines = List.of("abcdef абвг", "1234", "1.23", "-1", "1фыв", "-1.23");
        Files.write(inputFile, inputLines);

        Path outputDir = tempDir.resolve("output");
        Files.createDirectory(outputDir);

        String[] args = {"-f", "-o", outputDir.toString(), inputFile.toString()};

        // Main.main(args) replacement. System.exit(exitCode) fails tests
        ProcessFilesApp processFilesApp = new ProcessFilesApp();
        CommandLine commandLine = new CommandLine(processFilesApp);
        int exitCode = commandLine.execute(args);
        assertEquals(0, exitCode);

        Path intFile = outputDir.resolve("integers.txt");
        Path floatFile = outputDir.resolve("floats.txt");
        Path stringFile = outputDir.resolve("strings.txt");

        assertTrue(Files.exists(intFile));
        assertTrue(Files.exists(floatFile));
        assertTrue(Files.exists(stringFile));

        assertEquals(List.of("1234", "-1"), Files.readAllLines(intFile));
        assertEquals(List.of("1.23", "-1.23"), Files.readAllLines(floatFile));
        assertEquals(List.of("abcdef абвг", "1фыв"), Files.readAllLines(stringFile));
    }

    @Test
    @DisplayName("Should correctly handle edge cases and special float literals")
    void shouldHandleEdgeCasesAndSpecialLiterals() throws IOException {

        Path inputFile = tempDir.resolve("input.txt");

        List<String> inputLines = List.of("NaN", "Infinity", "  -9223372036854775808    ", "   ", "  1.23E10", "-infinity");
        Files.write(inputFile, inputLines);

        Path outputDir = tempDir.resolve("edge_cases");
        String[] args = {"-s", "-o", outputDir.toString(), inputFile.toString()};

        ProcessFilesApp processFilesApp = new ProcessFilesApp();
        CommandLine commandLine = new CommandLine(processFilesApp);
        int exitCode = commandLine.execute(args);
        assertEquals(0, exitCode);

        Path integerFile = outputDir.resolve("integers.txt");
        Path floatFile = outputDir.resolve("floats.txt");
        Path stringFile = outputDir.resolve("strings.txt");

        assertTrue(Files.exists(integerFile));
        assertTrue(Files.exists(floatFile));
        assertTrue(Files.exists(stringFile));

        assertEquals(List.of("-9223372036854775808"), Files.readAllLines(integerFile));
        assertEquals(List.of("1.23E10"), Files.readAllLines(floatFile));
        assertEquals(List.of("NaN", "Infinity", "   ", "-infinity"), Files.readAllLines(stringFile));
    }

    @Test
    @DisplayName("Should append to existing files when -a option is used")
    void shouldAppendToExistingFiles() throws IOException {

        Path outputDir = tempDir.resolve("output_append_mode");
        Files.createDirectory(outputDir);
        String prefix = "append_mode-";

        Path oldIntegerFile = outputDir.resolve(prefix + "integers.txt");
        Files.write(oldIntegerFile, List.of("1"));


        Path inputFile2 = tempDir.resolve("input3.txt");
        Files.write(inputFile2, List.of("200", "string"));
        Path inputFile3 = tempDir.resolve("input4.txt");
        Files.write(inputFile3, List.of("-3000", "4.0"));

        String[] args = {"-s", "-a", "-p", prefix, "-o", outputDir.toString(),
                inputFile2.toString(), inputFile3.toString()};


        ProcessFilesApp processFilesApp = new ProcessFilesApp();
        CommandLine commandLine = new CommandLine(processFilesApp);
        int exitCode = commandLine.execute(args);
        assertEquals(0, exitCode);


        Path intFile = outputDir.resolve(prefix + "integers.txt");
        Path floatFile = outputDir.resolve(prefix + "floats.txt");
        Path stringFile = outputDir.resolve(prefix + "strings.txt");

        assertTrue(Files.exists(intFile));
        assertTrue(Files.exists(floatFile));
        assertTrue(Files.exists(stringFile));


        assertEquals(List.of("1", "200", "-3000"), Files.readAllLines(intFile));
        assertEquals(List.of("4.0"), Files.readAllLines(floatFile));
        assertEquals(List.of("string"), Files.readAllLines(stringFile));
    }

    @Test
    @DisplayName("Integration: Should continue processing after encountering a non-existent file")
    void shouldContinueAfterNonExistentFile() throws IOException {

        Path outputDir = tempDir.resolve("output_continue");
        Path validFile = tempDir.resolve("valid.txt");
        Files.writeString(validFile, "1234" + System.lineSeparator() + "string");


        String nonExistentPath = tempDir.resolve("missing.txt").toString();
        String[] args = {"-f", "-o", outputDir.toString(), nonExistentPath, validFile.toString()};

        ProcessFilesApp processFilesApp = new ProcessFilesApp();
        CommandLine commandLine = new CommandLine(processFilesApp);
        int exitCode = commandLine.execute(args);
        assertEquals(0, exitCode);

        Path integerFile = outputDir.resolve("integers.txt");
        Path stringFile = outputDir.resolve("strings.txt");
        assertTrue(Files.exists(integerFile), "Integer file should be created from the valid input file.");
        assertEquals(List.of("1234"), Files.readAllLines(integerFile));
        assertEquals(List.of("string"), Files.readAllLines(stringFile));
    }


}