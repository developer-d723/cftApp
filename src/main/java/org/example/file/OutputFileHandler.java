package org.example.file;

import java.nio.file.Path;

public class OutputFileHandler {
    private Path outputPath;
    private String prefix;

    public OutputFileHandler(Path outputPath, String prefix) {
        this.outputPath = outputPath;
        this.prefix = prefix;
    }
}
