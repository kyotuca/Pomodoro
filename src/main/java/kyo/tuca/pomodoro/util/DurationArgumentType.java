package kyo.tuca.pomodoro.util;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.text.Text;

import java.time.Duration;
import java.util.Arrays;

/**
 * Defines a duration argument as a command argument
 */
public class DurationArgumentType implements ArgumentType<Duration> {
    public static final SimpleCommandExceptionType DURATION_CONVERSION_EXCEPTION =
            new SimpleCommandExceptionType(Text.of("Invalid Arguments for duration"));

    @Override
    public Duration parse(StringReader stringReader) throws CommandSyntaxException {

        Duration duration;
        int start = stringReader.getCursor();
        while (stringReader.canRead() && stringReader.peek() != ' '){
            stringReader.read();
        }
        String inputString = stringReader.getString().substring(start, stringReader.getCursor());
        String[] timings = inputString.split(":");

        try {
            duration = switch (timings.length) {
                case 1 -> Duration.ofSeconds(Integer.parseInt(timings[0]));
                case 2 -> Duration.ofMinutes(Integer.parseInt(timings[1]))
                        .plusSeconds(Integer.parseInt(timings[0]));
                case 3 -> Duration.ofHours(Integer.parseInt(timings[2]))
                        .plusMinutes(Integer.parseInt(timings[1]))
                        .plusSeconds(Integer.parseInt(timings[0]));
                default -> throw DURATION_CONVERSION_EXCEPTION.create();
            };
        } catch (NumberFormatException e){
            throw DURATION_CONVERSION_EXCEPTION.create();
        }
        return duration;
    }
}
