package kyo.tuca.pomodoro.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import kyo.tuca.pomodoro.timer.PomodoroTimer;
import kyo.tuca.pomodoro.timer.TimerManager;
import kyo.tuca.pomodoro.util.DurationArgumentType;
import kyo.tuca.pomodoro.util.TimerOperationStatus;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;

/**
 * Contains the logic of the /pomodoro command, including registration
 */
public class PomodoroCommand {
    public static final Logger LOGGER = LogManager.getLogger(PomodoroCommand.class);

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess registryAccess,
                                CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(
                CommandManager.literal("pomodoro")
                        .then(CommandManager.literal("stop")
                                .executes(PomodoroCommand::stopTimer))
                        .then(CommandManager.literal("start")
                                .executes(PomodoroCommand::defaultStart))
                        .then(CommandManager.argument("task time", new DurationArgumentType())
                                .then(CommandManager.argument("pause time", new DurationArgumentType())
                                        .executes(PomodoroCommand::startTimer))));
        LOGGER.log(Level.INFO, "\"pomodoro\" command registered");

    }

    public static int defaultStart(CommandContext<ServerCommandSource> context){
        ServerPlayerEntity player = context.getSource().getPlayer();
        if(player == null) {
            context.getSource().sendFeedback(() -> Text.literal("Command not valid, player not found"), false);
            return -1;
        }
        notifyStart(TimerManager.addDefaultTimer(player.getUuid()), context);
        return 0;
    }

    public static int startTimer(CommandContext<ServerCommandSource> context){
        ServerPlayerEntity player = context.getSource().getPlayer();
        if(player == null) {
            context.getSource().sendFeedback(() -> Text.literal("Command not valid, player not found"), false);
            return -1;
        }
        notifyStart(TimerManager.addTimer(new PomodoroTimer(
                player.getUuid(),
                context.getArgument("task time", Duration.class).getSeconds(),
                context.getArgument("pause time", Duration.class).getSeconds())), context
        );
        return 0;
    }

    public  static int stopTimer(CommandContext<ServerCommandSource> context){
        ServerPlayerEntity player = context.getSource().getPlayer();
        if(player == null) {
            context.getSource().sendFeedback(() -> Text.literal("Command not valid, player not found"), false);
            return -1;
        }
        TimerManager.removeTimer(player.getUuid());
        return 0;
    }

    private static void notifyStart(TimerOperationStatus status, CommandContext<ServerCommandSource> context){
        switch (status) {
            case OK -> context.getSource().sendFeedback(() -> Text.literal("Pomodoro started"), false);
            case TIMER_EXISTS -> context.getSource().sendFeedback(() -> Text.literal("A timer is already running, stop it first"), false);
            default -> context.getSource().sendFeedback(() -> Text.literal("Generic error setting timer"), false);
        }
    }

}
