package org.example.line;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LineFormatCheckerTest {

    @Test
    void testIntegerValues() {
        assertEquals(LineType.INTEGER, LineFormatChecker.check("0").lineType());
        assertEquals(LineType.INTEGER, LineFormatChecker.check("0012").lineType());
        assertEquals(LineType.INTEGER, LineFormatChecker.check("1234").lineType());
        assertEquals(LineType.INTEGER, LineFormatChecker.check("-1234").lineType());
        assertEquals(LineType.INTEGER, LineFormatChecker.check("+1234").lineType());
        assertEquals(LineType.INTEGER, LineFormatChecker.check(" 1 ").lineType());
        assertEquals(LineType.INTEGER, LineFormatChecker.check(" +1234").lineType());
        assertEquals(LineType.INTEGER, LineFormatChecker.check(" -1234    ").lineType());
    }

    @Test
    void testFloatValues() {
        assertEquals(LineType.FLOAT, LineFormatChecker.check("0.00").lineType());
        assertEquals(LineType.FLOAT, LineFormatChecker.check("1.2345").lineType());
        assertEquals(LineType.FLOAT, LineFormatChecker.check("+0.001").lineType());
        assertEquals(LineType.FLOAT, LineFormatChecker.check("-0.001").lineType());
        assertEquals(LineType.FLOAT, LineFormatChecker.check("1.23456789E-25").lineType());
        assertEquals(LineType.FLOAT, LineFormatChecker.check("  1.23456789E-25 ").lineType());
        assertEquals(LineType.FLOAT, LineFormatChecker.check("+5.5").lineType());
        assertEquals(LineType.FLOAT, LineFormatChecker.check("  +1.23456789E-25 ").lineType());
        assertEquals(LineType.FLOAT, LineFormatChecker.check("  -1.23456789E-25 ").lineType());
    }

    @Test
    void testStringValues() {
        assertEquals(LineType.STRING, LineFormatChecker.check("abcde").lineType());
        assertEquals(LineType.STRING, LineFormatChecker.check("12345abcd").lineType());
        assertEquals(LineType.STRING, LineFormatChecker.check("1.2.3.4.5").lineType());
        assertEquals(LineType.STRING, LineFormatChecker.check("1e3e5e5").lineType());
        assertEquals(LineType.STRING, LineFormatChecker.check("1E-25.F").lineType());
        assertEquals(LineType.STRING, LineFormatChecker.check("++12345").lineType());
        assertEquals(LineType.STRING, LineFormatChecker.check("--12345").lineType());
        assertEquals(LineType.STRING, LineFormatChecker.check("1,2345").lineType());
        assertEquals(LineType.STRING, LineFormatChecker.check("NaN").lineType());
        assertEquals(LineType.STRING, LineFormatChecker.check("Infinity").lineType());
        assertEquals(LineType.STRING, LineFormatChecker.check("+Infinity").lineType());
        assertEquals(LineType.STRING, LineFormatChecker.check("-Infinity").lineType());
        assertEquals(LineType.STRING, LineFormatChecker.check("infinity").lineType());
        assertEquals(LineType.STRING, LineFormatChecker.check("абвгдежз").lineType());
        assertEquals(LineType.STRING, LineFormatChecker.check("12.абвгдежз").lineType());
        assertEquals(LineType.STRING, LineFormatChecker.check("123 абвгдежз").lineType());
    }
}

