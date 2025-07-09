package kyo.tuca.pomodoro.timer;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PomodoroTimerTest {
    PomodoroTimer timer;

    private void tick(long seconds){
        for(int i = 0; i < seconds * 20; i++){
            timer.tick();
        }
    }

    @Test
    void testShortPauseCycle(){
        timer = new PomodoroTimer(UUID.randomUUID(), 1, 1); //TODO change pomodoro to allow only task - short
        tick(1);
        assertEquals(TaskType.SHORT_PAUSE, timer.getCurrentTask());
        tick(1);
        assertEquals(TaskType.ACTIVITY, timer.getCurrentTask());
        assertTrue(timer.isTaskActivity());
        tick(1);
        assertEquals(TaskType.SHORT_PAUSE, timer.getCurrentTask());
    }

    @Test
    void testLongPauseCycle(){
        timer = new PomodoroTimer(UUID.randomUUID(), 1, 1, 1, 1);
        tick(1);
        assertEquals(TaskType.SHORT_PAUSE, timer.getCurrentTask());
        tick(1);
        assertEquals(TaskType.ACTIVITY, timer.getCurrentTask());
        tick(1);
        assertEquals(TaskType.LONG_PAUSE, timer.getCurrentTask());
    }

    @Test
    void testSuspendedTimer(){
        timer = new PomodoroTimer(UUID.randomUUID(), 1, 1);
        timer.setTickable(false);
        tick(1);
        assertFalse(timer.isTickable());
        assertTrue(timer.isTaskActivity());
    }

}