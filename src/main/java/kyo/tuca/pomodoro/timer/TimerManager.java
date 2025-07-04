package kyo.tuca.pomodoro.timer;

import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TimerManager {
    private static final List<PomodoroTimer> timers = new ArrayList<>();

    public static void addTimer(UUID playerID, long taskLength, long pauseLength){
        if(timers.stream().anyMatch(timer -> playerID == timer.getPlayer())){
            //TODO  "you already have a timer running"
            return;
        }
        timers.add(new PomodoroTimer(playerID, taskLength, pauseLength));

    }

    public static void removeTimer(UUID playerID){
        if(timers.removeIf(timer -> timer.getPlayer().equals(playerID))){
            //TODO timer removed
        }
        else {
            //TODO you don't have a timer
        }
    }

    public static void tick(MinecraftServer server){
        for(PomodoroTimer timer : timers){
            timer.tick(server);
        }
    }

}
