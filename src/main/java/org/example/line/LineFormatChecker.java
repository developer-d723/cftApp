package org.example.line;

public class LineFormatChecker {

    public static CheckLineFormatResult check(String line) {
        if (line.isBlank()) {
            return new CheckLineFormatResult(line, LineType.STRING);
        }

        try {
            Integer.parseInt(line);
            return new CheckLineFormatResult(line, LineType.INTEGER);
        } catch (NumberFormatException ignored) {
        }

        try {
            Float.parseFloat(line);
            return new CheckLineFormatResult(line, LineType.FLOAT);
        } catch (NumberFormatException ignored) {
        }

        return new CheckLineFormatResult(line, LineType.STRING);
    }
}
