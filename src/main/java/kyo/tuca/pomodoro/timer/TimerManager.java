package kyo.tuca.pomodoro.timer;

import kyo.tuca.pomodoro.util.TimerOperationStatus;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * manages all the timers, passing ticks and removing them
 */
public class TimerManager {
    private static final List<PomodoroTimer> timers = new ArrayList<>();
    private static boolean hasAnyTimer = false;

    public static TimerOperationStatus addTimer(UUID playerID, long taskLength, long pauseLength){
        if(timers.stream().anyMatch(timer -> playerID == timer.getPlayer())){
            //TODO  "you already have a timer running"
            return TimerOperationStatus.TIMER_EXISTS;
        }
        if(!hasAnyTimer) {
            hasAnyTimer = true;
            cleanSub();
        }
        timers.add(new PomodoroTimer(playerID, taskLength, pauseLength));
        return TimerOperationStatus.OK;
    }

    public static TimerOperationStatus addDefaultTimer(UUID playerID){
        if(timers.stream().anyMatch(timer -> playerID == timer.getPlayer())){
            return TimerOperationStatus.TIMER_EXISTS;
        }
        timers.add(new PomodoroTimer(playerID));
        return TimerOperationStatus.OK;
    }

    /**
     * removes a timer
     * @param playerID
     */
    public static void removeTimer(UUID playerID){
        if(timers.removeIf(timer -> timer.getPlayer().equals(playerID))){
            //TODO timer removed
        }
        else {
            //TODO you don't have a timer
        }
        if(timers.isEmpty()) hasAnyTimer = false;
    }

    public static void tick(MinecraftServer server){
        for(PomodoroTimer timer : timers){
            if(timer.isTickable()) timer.tick();
        }
    }

    /**
     * clears timers if a player leaves
     */
    private static void cleanSub(){
        ServerPlayerEvents.LEAVE.register((player) -> {
            timers.removeIf(timer -> timer.getPlayer().equals(player.getUuid()));
        });
    }

    /**
     *
     * @param player the UUID of the player to check
     * @return whether a player has an active timer
     */
    public static boolean taskActive(UUID player){
        for(PomodoroTimer timer : timers){
            if(timer.getPlayer() == player && timer.isTickable() && timer.isTaskActivity()){
                return true;
            }
        }
        return false;
    }

}
