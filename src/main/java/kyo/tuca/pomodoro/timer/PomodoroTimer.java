package kyo.tuca.pomodoro.timer;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.util.UUID;

/**
 * A single timer, with a user associated.
 * It manages the internal state and the timers
 */
public class PomodoroTimer {
    private final UUID player;
    private final long taskTime;
    private final long pauseTime;
    private final int tickPerSeconds = 20;
    private long timeLeft;
    private boolean isTaskActive; //true if it's a task, false if it's a pause
    private boolean tickable;

    public PomodoroTimer(UUID playerID, long taskTime, long pauseTime){
        this.player = playerID;
        this.timeLeft = taskTime * 20;
        this.taskTime = taskTime * 20;
        this.pauseTime = pauseTime * 20;
        this.isTaskActive = true;
        this.tickable = true;
    }

    public PomodoroTimer(UUID playerID){
        this.player = playerID;
        this.taskTime = 15 * 60 * 20;
        this.pauseTime = 5 * 60 * 20;
        this.isTaskActive = true;
        this.timeLeft = taskTime;
        this.tickable = true;
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

    /**
     * Swap the state, moving from task to pause and vice versa
     */
    private void changeTask(){
        if(isTaskActive){
            timeLeft = pauseTime;
        }
        else timeLeft = taskTime;

        isTaskActive = !isTaskActive;
    }

    /**
     * Sends a notification
     * @param server
     */
    private void notifyTaskEnd(MinecraftServer server){
        Text message = (isTaskActive) ? Text.of("End Focus") : Text.of("End Pause");
        ServerPlayerEntity timedPlayer = server.getPlayerManager().getPlayer(player);
        if(timedPlayer == null) return; //TODO
        timedPlayer.playSoundToPlayer(SoundEvents.BLOCK_NOTE_BLOCK_PLING.value(), SoundCategory.MASTER, 1.0f, 1.0f);
        timedPlayer.sendMessage(message);
    }

    public boolean isTickable(){
        return tickable;
    }

    public void setTickable(boolean b){
        tickable = b;
    }

    /**
     * get the current state
     * @return true if it's running a task, false otherwise
     */
    public boolean getState(){
        return isTaskActive;
    }

}
