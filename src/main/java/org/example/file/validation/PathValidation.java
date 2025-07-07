package org.example.file.validation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class PathValidation {

    private static final String INVALID_FILENAME_CHARS = "<>:\"/\\|?*";

    public void validateName(String name) throws ValidationException {
        if (name == null) return;
        for (char ch : INVALID_FILENAME_CHARS.toCharArray()) {
            if (name.indexOf(ch) >= 0) {
                throw new ValidationException(ErrorType.FILE_NAME_INVALID_CHARS,
                        "Error: Name '" + name + "' contains an invalid character: '" + ch + "'");
            }
        }
    }

    public void validateInputFile(File file) throws ValidationException {
        if (!file.exists()) {
            throw new ValidationException(ErrorType.INPUT_FILE_NOT_FOUND,
                    "Input file not found '" + file.getPath() + "' not found");
        }

        if (!file.isFile()) {
            throw new ValidationException(ErrorType.INPUT_PATH_IS_DIRECTORY,
                    "Input file '" + file.getPath() + "' is a directory, not a file");
        }

        if (!file.canRead()) {
            throw new ValidationException(ErrorType.INPUT_FILE_NOT_READABLE,
                    "Input file cannot be read '" + file.getPath() + "'");
        }

    }

    public void validateOutputPath(Path outputPath) throws ValidationException {
        if (outputPath.toString().isEmpty()) return;

        if (!Files.exists(outputPath)) {
            try {
                Files.createDirectories(outputPath);
            } catch (IOException e) {
                throw new ValidationException(ErrorType.OUTPUT_DIRECTORY_NOT_CREATABLE,
                        "Could not create output directory '" + outputPath + "'. Error: " + e.getMessage());
            }
        }
        if (!Files.isDirectory(outputPath)) {
            throw new ValidationException(ErrorType.OUTPUT_PATH_IS_NOT_DIRECTORY,
                    "Output path '" + outputPath + "' is not a directory");
        }
        if (!Files.isWritable(outputPath)) {
            throw new ValidationException(ErrorType.DIRECTORY_NOT_WRITABLE,
                    "Cannot write to directory '" + outputPath);
        }
    }
}