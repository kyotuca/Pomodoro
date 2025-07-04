package kyo.tuca.pomodoro;

import com.mojang.brigadier.CommandDispatcher;
import kyo.tuca.pomodoro.command.PomodoroCommand;
import kyo.tuca.pomodoro.timer.TimerManager;
import kyo.tuca.pomodoro.util.DurationArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;


public class Pomodoro implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("pomodoro");

    @Override
    public void onInitialize() {

        CommandRegistrationCallback.EVENT.register(PomodoroCommand::register);

        ServerTickEvents.START_SERVER_TICK.register(TimerManager::tick);

        ArgumentTypeRegistry.registerArgumentType(Identifier.of("pomodoro","timer"),
                DurationArgumentType.class,
                ConstantArgumentSerializer.of(DurationArgumentType::new));
    }
}
