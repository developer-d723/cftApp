package org.example.line;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LineFormatCheckerTest {

    private LineFormatChecker lineFormatChecker;

    @BeforeEach
    void setUp() {
       lineFormatChecker = new LineFormatChecker();
    }

    @Test
    @DisplayName("Should be integer numbers")
    void testIntegerValues() {
        assertEquals(LineType.INTEGER, lineFormatChecker.check("0").lineType());
        assertEquals(LineType.INTEGER, lineFormatChecker.check("0012").lineType());
        assertEquals(LineType.INTEGER, lineFormatChecker.check("1234").lineType());
        assertEquals(LineType.INTEGER, lineFormatChecker.check("-1234").lineType());
        assertEquals(LineType.INTEGER, lineFormatChecker.check("+1234").lineType());
        assertEquals(LineType.INTEGER, lineFormatChecker.check(" 1 ").lineType());
        assertEquals(LineType.INTEGER, lineFormatChecker.check(" +1234").lineType());
        assertEquals(LineType.INTEGER, lineFormatChecker.check(" -1234    ").lineType());
        assertEquals(LineType.INTEGER, lineFormatChecker.check("1234567890123456789").lineType());
    }

    @Test
    @DisplayName("Should be floating-point numbers")
    void testFloatValues() {
        assertEquals(LineType.FLOAT, lineFormatChecker.check("0.00").lineType());
        assertEquals(LineType.FLOAT, lineFormatChecker.check("1.2345").lineType());
        assertEquals(LineType.FLOAT, lineFormatChecker.check("+0.001").lineType());
        assertEquals(LineType.FLOAT, lineFormatChecker.check("-0.001").lineType());
        assertEquals(LineType.FLOAT, lineFormatChecker.check("1.23456789E-25").lineType());
        assertEquals(LineType.FLOAT, lineFormatChecker.check("  1.23456789E-25 ").lineType());
        assertEquals(LineType.FLOAT, lineFormatChecker.check("+5.5").lineType());
        assertEquals(LineType.FLOAT, lineFormatChecker.check("  +1.23456789E-25 ").lineType());
        assertEquals(LineType.FLOAT, lineFormatChecker.check("  -1.23456789E-25 ").lineType());
    }

    @Test
    @DisplayName("Should be strings")
    void testStringValues() {
        assertEquals(LineType.STRING, lineFormatChecker.check("abcde").lineType());
        assertEquals(LineType.STRING, lineFormatChecker.check("12345abcd").lineType());
        assertEquals(LineType.STRING, lineFormatChecker.check("1.2.3.4.5").lineType());
        assertEquals(LineType.STRING, lineFormatChecker.check("1e3e5e5").lineType());
        assertEquals(LineType.STRING, lineFormatChecker.check("1E-25.F").lineType());
        assertEquals(LineType.STRING, lineFormatChecker.check("++12345").lineType());
        assertEquals(LineType.STRING, lineFormatChecker.check("--12345").lineType());
        assertEquals(LineType.STRING, lineFormatChecker.check("1,2345").lineType());
        assertEquals(LineType.STRING, lineFormatChecker.check("NaN").lineType());
        assertEquals(LineType.STRING, lineFormatChecker.check("Infinity").lineType());
        assertEquals(LineType.STRING, lineFormatChecker.check("+Infinity").lineType());
        assertEquals(LineType.STRING, lineFormatChecker.check("-Infinity").lineType());
        assertEquals(LineType.STRING, lineFormatChecker.check("infinity").lineType());
        assertEquals(LineType.STRING, lineFormatChecker.check("абвгдежз").lineType());
        assertEquals(LineType.STRING, lineFormatChecker.check("12.абвгдежз").lineType());
        assertEquals(LineType.STRING, lineFormatChecker.check("123 абвгдежз").lineType());
    }
}

