package org.example.line;

import java.util.Set;

/**
 * A class for determining the type of given string.
 * <p>
 * Options are integer (in fact, long for bigger range) , float (double) and string
 */

public class LineFormatChecker {
    /**
     * A set of special floating-point strings that Double.ParseDouble(line) falsely identifies as Double
     */
    private static final Set<String> BAD_FLOAT_STRINGS = Set.of("NaN", "Infinity", "+Infinity", "-Infinity");

    public CheckLineFormatResult check(String line) {
        if (line.isBlank() || BAD_FLOAT_STRINGS.contains(line)) {
            return new CheckLineFormatResult(line, LineType.STRING);
        }

        line = line.trim(); // otherwise, numbers like " 1234" is considered a string

        try {
            Long.parseLong(line);
            return new CheckLineFormatResult(line, LineType.INTEGER);
        } catch (NumberFormatException n) {
        }

        try {
            Double.parseDouble(line);
            return new CheckLineFormatResult(line, LineType.FLOAT);
        } catch (NumberFormatException n) {
        }

        return new CheckLineFormatResult(line, LineType.STRING);
    }
}
