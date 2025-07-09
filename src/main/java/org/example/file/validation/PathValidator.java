package org.example.file.validation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PathValidator {

    private static final String INVALID_FILENAME_CHARS = "<>:\"/\\|?*";

    public void validateNameSyntax(String name) throws ValidationException {
        if (name == null) return;
        for (char ch : INVALID_FILENAME_CHARS.toCharArray()) {
            if (name.indexOf(ch) >= 0) {
                throw new ValidationException(ErrorType.FILE_NAME_INVALID_CHARS,
                        "Name '" + name + "' contains an invalid character: '" + ch + "'");
            }
        }
    }

    public void validateInputFile(File file) throws ValidationException {
        if (file == null) {
            throw new ValidationException(ErrorType.INPUT_FILE_NOT_PROVIDED, "Input file must be provided");
        }


    }

    public void validateOutputPath(Path outputPath) throws ValidationException {
        if (outputPath.toString().isEmpty()) return;

        if (Files.exists(outputPath)) {

            if (!Files.isDirectory(outputPath)) {
                throw new ValidationException(ErrorType.OUTPUT_PATH_IS_NOT_DIRECTORY,
                        "Output path exists but is not a directory: '" + outputPath + "'");
            }
            if (!Files.isWritable(outputPath)) {
                throw new ValidationException(ErrorType.DIRECTORY_NOT_WRITABLE,
                        "Cannot write to output directory: '" + outputPath + "'");
            }
        } else {

            try {
                Files.createDirectories(outputPath);
            } catch (IOException e) {
                throw new ValidationException(ErrorType.OUTPUT_DIRECTORY_NOT_CREATABLE,
                        "Could not create output directory '" + outputPath + "'. Error: " + e.getMessage());
            }
        }
    }




}