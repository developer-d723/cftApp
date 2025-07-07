package org.example.file;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileValidation {

    private static final String INVALID_FILENAME_CHARS = "<>:\"/\\|?*";

    private String validateFileName(String fileName) throws ValidationException {
        if (fileName == null || fileName.isEmpty()) {
            throw new ValidationException(ErrorType.FILE_NAME_EMPTY, "Error: File name cannot be empty");
        }

        try {
            Path path = Paths.get(fileName);
            Path nameOnly = path.getFileName();

            if (nameOnly == null) {
                throw new ValidationException(ErrorType.FILE_PATH_INVALID_SYNTAX, "Error: The path is a directory, not a file");
            }

            String nameStr = nameOnly.toString();

            for (char ch : INVALID_FILENAME_CHARS.toCharArray()) {
                if (nameStr.indexOf(ch) >= 0) {
                    throw new ValidationException(ErrorType.FILE_NAME_INVALID_CHARS, "Error: File name contains an invalid character: '" + ch + "'");
                }
            }

            if (Files.exists(path)) {
                throw new ValidationException(ErrorType.FILE_ALREADY_EXISTS, "Error: File '" + fileName + "' already exists.");
            }

            Path parentDir = path.getParent();
            if (parentDir != null && Files.exists(parentDir) && !Files.isWritable(parentDir)) {
                throw new ValidationException(ErrorType.DIRECTORY_NOT_WRITABLE, "Error: No permission to write to directory: " + parentDir);
            }
        } catch (InvalidPathException e) {
            throw new ValidationException(ErrorType.FILE_PATH_INVALID_SYNTAX, "Error: Invalid file path syntax. " + e.getMessage());
        }
        return fileName;
    }

}
