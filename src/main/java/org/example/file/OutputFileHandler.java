package org.example.file;

import org.example.line.CheckLineFormatResult;
import org.example.line.LineType;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.EnumMap;
import java.util.Map;

/**
 * This Class writes data to the output files based on data type.
 * It also implements AutoCloseable for try-with-resources
 */
public class OutputFileHandler implements AutoCloseable {

    private final Path outputPath;
    private final String prefix;
    private final boolean appendMode;

    private final Map<LineType, BufferedWriter> writersForLineType = new EnumMap<>(LineType.class);

    public OutputFileHandler(Path outputPath, String prefix, boolean appendMode) {
        this.outputPath = outputPath;
        this.prefix = prefix;
        this.appendMode = appendMode;
    }

    /**
     * Writes a line to the output file based on its type.
     * <p>
     * @param result The result of the line format check. It contains the line and its type.
     * @throws IOException if an I/O error occurs when writing.
     */
    public void writeToFile(CheckLineFormatResult result) throws IOException {
        BufferedWriter bufferedWriter = setWriterForLineType(result.lineType());
        bufferedWriter.write(result.string());
        bufferedWriter.newLine();
    }

    /**
     * Gets or creates a BufferedWriter for a specific LineType.
     * <p>
     * @param lineType The type of data (INTEGER, FLOAT, STRING) for a writer.
     * @return The BufferedWriter for the given LineType.
     * @throws IOException if an error occurs when creating the new writer.
     */
    private BufferedWriter setWriterForLineType(LineType lineType) throws IOException {

        BufferedWriter bufferedWriter = writersForLineType.get(lineType);

        if (bufferedWriter == null) {
            Path filePath = outputPath.resolve(prefix + lineType.getFileName());

            Path parentDir = filePath.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }

            OpenOption[] options;
            if (appendMode) {
                options = new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.APPEND};
            } else {
                options = new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING};
            }
            System.out.println("Creating output file: " + filePath.toAbsolutePath().normalize());
            bufferedWriter = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8, options);
            writersForLineType.put(lineType, bufferedWriter);
        }

        return bufferedWriter;
    }

    /**
     * Closes all opened BufferedWriter streams.
     * Called automatically at the end of a try-with-resources block.
     *
     * @throws IOException if an I/O error occurs when closing a writer.
     */
    @Override
    public void close() throws IOException {
        for (BufferedWriter bufferedWriter : writersForLineType.values()) {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    System.err.println("Error closing a bufferedWriter: " + e.getMessage());
                }
            }
        }
    }
}