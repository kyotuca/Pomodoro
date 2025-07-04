package kyo.tuca.pomodoro.timer;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.util.UUID;

public class PomodoroTimer {
    private final UUID player;
    private final long taskTime;
    private final long pauseTime;
    private final int tickPerSeconds = 20;
    private long timeLeft;
    private boolean isTaskActive; //true if it's a task, false if it's a pause

    public PomodoroTimer(UUID playerID, long taskTime, long pauseTime){
        this.player = playerID;
        this.timeLeft = taskTime * 20;
        this.taskTime = taskTime * 20;
        this.pauseTime = pauseTime * 20;
        this.isTaskActive = true;
    }

    /**
     * ticks a timer and notifies updates
     * @param server the server to send notifications
     */
    public void tick(MinecraftServer server){
        timeLeft--;
        if(timeLeft<= 0){
            notifyTaskEnd(server);
            changeTask();
        }
    }

    public UUID getPlayer(){
        return player;
    }

    private void changeTask(){
        if(isTaskActive){
            timeLeft = pauseTime;
        }
        else timeLeft = taskTime;

        isTaskActive = !isTaskActive;
    }

    private void notifyTaskEnd(MinecraftServer server){
        Text message = (isTaskActive) ? Text.of("Fine Pomodoro") : Text.of("Fine Pausa");
        ServerPlayerEntity timedPlayer = server.getPlayerManager().getPlayer(player);
        if(timedPlayer == null) return; //TODO
        timedPlayer.playSound(SoundEvents.BLOCK_NOTE_BLOCK_PLING.value(), 1.0f, 1.0f);
        timedPlayer.sendMessage(message);
    }

}
