package kyo.tuca.pomodoro.util;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class DurationArgumentTypeTest {
    DurationArgumentType argument = new DurationArgumentType();

    @Test
    void testSecondsFunctional(){
        StringReader reader = new StringReader("1");

        Duration result = assertDoesNotThrow(() -> argument.parse(reader), reader.getString() + " should not throw an exception");
        assertEquals(Duration.ofSeconds(1), result);
    }

    @Test
    void testMinutesFunctional(){
        StringReader reader = new StringReader("1:15");

        Duration result = assertDoesNotThrow(() -> argument.parse(reader), reader.getString() + " should not throw an exception");
        assertEquals(Duration.ofMinutes(1).plusSeconds(15), result);
    }

    @Test
    void testHourFunctional(){
        StringReader reader = new StringReader("1:30:15");

        Duration result = assertDoesNotThrow(() -> argument.parse(reader), reader.getString() + " should not throw an exception");
        assertEquals(Duration.ofHours(1).plusMinutes(30).plusSeconds(15), result);
    }

    @Test
    void testZeroTime(){
        StringReader reader = new StringReader("0:0:0");

        Duration result = assertDoesNotThrow(() -> argument.parse(reader), reader.getString() + " should not throw an exception");
        assertEquals(Duration.ofHours(0), result);
    }

    @Test
    void testIncorrectFormat(){
        StringReader reader = new StringReader("0;0;0");

        assertThrows(CommandSyntaxException.class, () -> argument.parse(reader));
    }

    @Test
    void testNotNumbers(){
        StringReader reader = new StringReader("A:B:C");

        assertThrows(CommandSyntaxException.class, () -> argument.parse(reader));
    }

    @Test
    void testNullString(){
        String input = null;
        StringReader reader = new StringReader(input);

        assertThrows(NullPointerException.class, () -> argument.parse(reader));
    }


}