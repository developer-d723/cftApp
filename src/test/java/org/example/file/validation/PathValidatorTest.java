package org.example.file.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class PathValidatorTest {

    private final PathValidator pathValidator = new PathValidator();

    @ParameterizedTest
    @DisplayName("Should throw for file names with invalid characters, <>:\"/\\|?*")
    @ValueSource(strings = {"<filename", "filename>", "filename:", "\"filename\"", "filename/",
            "filename\\", "filename|", "filename?", "filename*"})
    void shouldThrow_whenFileNameContainsInvalidCharacters(String invalidName) {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> pathValidator.validateNameSyntax(invalidName)
        );

        assertEquals(ErrorType.FILE_NAME_INVALID_CHARS, exception.getCode());
    }


    @Test
    @DisplayName("Should allow null as a valid file name")
    void shouldNotThrow_whenFileNameIsNull() {
        assertDoesNotThrow(() -> pathValidator.validateNameSyntax(null));
    }

    @ParameterizedTest
    @DisplayName("Should allow valid file names")
    @ValueSource(strings = {"file-name", "file.txt", "file_name", "12345", ""})
    void shouldNotThrow_whenFileNameIsValid(String validName) {
        assertDoesNotThrow(() -> pathValidator.validateNameSyntax(validName));
    }

    @Test
    @DisplayName("Should throw when input file is null")
    void shouldThrow_whenInputFileIsNull() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> pathValidator.validateInputFile(null)
        );

        assertEquals(ErrorType.INPUT_FILE_NOT_PROVIDED, exception.getCode());
    }

    @Test
    @DisplayName("Should allow valid input file")
    void shouldNotThrow_whenInputFileIsValid(@TempDir Path tempDir) throws IOException {
        File validFile = Files.createFile(tempDir.resolve("file.txt")).toFile();
        assertDoesNotThrow(() -> pathValidator.validateInputFile(validFile));
    }

    @Test
    @DisplayName("Should throw when output path is a file, not a directory")
    void shouldThrow_whenOutputPathIsFile(@TempDir Path tempDir) throws IOException {
        Path filePath = Files.createFile(tempDir.resolve("file.txt"));
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> pathValidator.validateOutputPath(filePath)
        );

        assertEquals(ErrorType.OUTPUT_PATH_IS_NOT_DIRECTORY, exception.getCode());
    }

    @Test
    @DisplayName("Should allow empty output path")
    void shouldNotThrow_whenOutputPathIsEmpty() {
        assertDoesNotThrow(() -> pathValidator.validateOutputPath(Path.of("")));
    }

    @Test
    @DisplayName("Should allow existing output directory")
    void shouldNotThrow_whenOutputPathIsExistingDirectory(@TempDir Path tempDir) {
        assertDoesNotThrow(() -> pathValidator.validateOutputPath(tempDir));
    }

    @Test
    @DisplayName("Should create missing output directories")
    void shouldCreateDirectories_whenOutputPathDoesNotExist(@TempDir Path tempDir) {
        Path newDir = tempDir.resolve("new/sub/directory");
        assertDoesNotThrow(() -> pathValidator.validateOutputPath(newDir));
        assertTrue(Files.isDirectory(newDir));
    }


    @Test
    @DisplayName("Should throw when output directory cannot be created")
    void shouldThrow_whenCannotCreateOutputDirectory(@TempDir Path tempDir) throws IOException {
        Path file = Files.createFile(tempDir.resolve("file"));
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> pathValidator.validateOutputPath(file.resolve("subdir"))
        );
        assertEquals(ErrorType.OUTPUT_DIRECTORY_NOT_CREATABLE, exception.getCode());
    }


    @Test
    @DisabledOnOs(OS.WINDOWS)
    @DisplayName("Should throw when output directory is not writable")
    void shouldThrow_whenOutputDirectoryIsUnwritable(@TempDir Path tempDir) throws IOException {
        Path unwritableDir = tempDir.resolve("unwritableDirectory");
        Files.createDirectory(unwritableDir);
        if (!unwritableDir.toFile().setWritable(false)) {
            System.out.println("Not supported on this OS");
            return;
        }
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> pathValidator.validateOutputPath(unwritableDir)
        );
        assertEquals(ErrorType.DIRECTORY_NOT_WRITABLE, exception.getCode());
    }
}