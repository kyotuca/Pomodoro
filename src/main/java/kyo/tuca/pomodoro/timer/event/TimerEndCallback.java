package kyo.tuca.pomodoro.timer.event;

import kyo.tuca.pomodoro.timer.TaskType;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import java.util.UUID;

public interface TimerEndCallback {
    Event<TimerEndCallback> EVENT = EventFactory.createArrayBacked(TimerEndCallback.class,
            (listeners) -> ((playerID, type) -> {
                for(TimerEndCallback listener : listeners) {
                    listener.onTimerEnd(playerID, type);
                }
            }));

    void onTimerEnd(UUID playerID, TaskType type);
}
