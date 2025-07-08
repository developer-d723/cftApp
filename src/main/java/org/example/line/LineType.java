package org.example.line;

public enum LineType {
    INTEGER("integers.txt"),
    FLOAT("floats.txt"),
    STRING("strings.txt");

    private final String fileName;

    LineType(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}