package kyo.tuca.pomodoro.timer;

import kyo.tuca.pomodoro.timer.event.TimerEndCallback;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.util.OptionalLong;
import java.util.UUID;

/**
 * A single timer, with a user associated.
 * It manages the internal state and the timers
 */
public class PomodoroTimer {
    private final int tickPerSeconds = 20;
    private final UUID player;
    private final long activityTime;
    private final long shortPauseTime;
    private OptionalLong longPauseTime;

    private long timeLeft;
    private boolean tickable;
    private int loopCounter;
    private int loopIndexForLongPause;
    private TaskType currentTask;

    public PomodoroTimer(UUID playerID, long taskTime, long pauseTime){
        this.player = playerID;
        this.timeLeft = taskTime * 20;
        this.activityTime = taskTime * 20;
        this.shortPauseTime = pauseTime * 20;
        this.loopCounter = 0;
        this.tickable = true;
        longPauseTime = OptionalLong.empty();
        currentTask = TaskType.ACTIVITY;
    }

    public PomodoroTimer(UUID playerID){
        this.player = playerID;
        this.activityTime = 15 * 60 * 20;
        this.shortPauseTime = 5 * 60 * 20;
        this.loopCounter = 0;
        this.timeLeft = activityTime;
        this.tickable = true;
        longPauseTime = OptionalLong.empty();
        currentTask = TaskType.ACTIVITY;
    }

    public PomodoroTimer(UUID playerID, long taskTime, long pauseTime, long longPauseTime, int onWhichCycleLongPause){
        this(playerID, taskTime, pauseTime);
        this.longPauseTime = OptionalLong.of(longPauseTime);
        this.loopIndexForLongPause = onWhichCycleLongPause;
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
        TaskType oldState = currentTask;
        if(loopCounter == loopIndexForLongPause && currentTask == TaskType.ACTIVITY && longPauseTime.isPresent()){
            currentTask = TaskType.LONG_PAUSE;
            timeLeft = longPauseTime.getAsLong();
        }
        else if(currentTask == TaskType.SHORT_PAUSE || currentTask == TaskType.LONG_PAUSE){
            currentTask = TaskType.ACTIVITY;
            timeLeft = activityTime;
            loopCounter++;
        }
        else{
            currentTask = TaskType.SHORT_PAUSE;
            timeLeft = shortPauseTime;
        }
        TimerEndCallback.EVENT.invoker().onTimerEnd(getPlayer(), oldState);
    }

    /**
     * Sends a notification
     * @param server
     */
    private void notifyTaskEnd(MinecraftServer server){
        Text message = (currentTask == TaskType.ACTIVITY) ? Text.of("End Focus") : Text.of("End Pause");
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

    public TaskType getCurrentTask(){
        return currentTask;
    }

    /**
     * get the current state
     * @return true if it's running a task, false otherwise
     */
    public boolean isTaskActivity(){
        return currentTask == TaskType.ACTIVITY;
    }

}