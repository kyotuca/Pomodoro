package kyo.tuca.pomodoro.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import kyo.tuca.pomodoro.timer.TimerManager;
import kyo.tuca.pomodoro.util.DurationArgumentType;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;

public class PomodoroCommand {
    public static final Logger LOGGER = LogManager.getLogger(PomodoroCommand.class);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess registryAccess,
                                CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(
                CommandManager.literal("pomodoro")
                        .then(CommandManager.argument("task time", new DurationArgumentType())
                                .then(CommandManager.argument("pause time", new DurationArgumentType())
                                        .executes(PomodoroCommand::startTimer))));
        LOGGER.log(Level.INFO, "\"pomodoro\" command registered");
    }

    public static int startTimer(CommandContext<ServerCommandSource> context){
        ServerPlayerEntity player = context.getSource().getPlayer();
        if(player == null) return -1;
        TimerManager.addTimer(player.getUuid(),
                context.getArgument("task time", Duration.class).getSeconds(),
                context.getArgument("pause time", Duration.class).getSeconds());
        return 0;
    }


}
