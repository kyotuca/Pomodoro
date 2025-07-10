package kyo.tuca.pomodoro.timer;

import kyo.tuca.pomodoro.timer.event.TimerEndCallback;
import net.minecraft.server.MinecraftServer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TimerManagerTest {
    MinecraftServer server = Mockito.mock();

    void tick(long seconds){
        for(int i = 0; i < seconds * 20; i++){
            TimerManager.tick(server);
        }
    }

    @Test
    void testTimerControl(){
        UUID id = UUID.randomUUID();

        TimerManager.addTimer(new PomodoroTimer(UUID.randomUUID(), 1, 1));
        tick(1);

        TimerEndCallback.EVENT.register((userID, task) -> {
            assertEquals(id, userID);
            assertEquals(TaskType.SHORT_PAUSE, task);
        });
    }

}