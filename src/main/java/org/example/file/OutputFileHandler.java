package org.example.file;

import org.example.line.CheckLineFormatResult;
import org.example.line.LineType;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;

public class OutputFileHandler implements AutoCloseable {
    private Path outputPath;
    private String prefix;
    private boolean appendMode;

    private final Map<LineType, BufferedWriter> writersForLineType = new EnumMap<>(LineType.class);

    public OutputFileHandler(Path outputPath, String prefix, boolean appendMode) {
        this.outputPath = outputPath;
        this.prefix = prefix;
        this.appendMode = appendMode;
    }

    public void writeToFile(CheckLineFormatResult result) throws IOException {
        BufferedWriter bufferedWriter = setWriterForLineType(result.lineType());
        bufferedWriter.write(result.string());
        bufferedWriter.newLine();
    }

    public BufferedWriter setWriterForLineType(LineType lineType) {
        return null;
    }

    @Override
    public void close() throws IOException {
        for (BufferedWriter bufferedWriter : writersForLineType.values()) {
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        }
    }
}
