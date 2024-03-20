package manager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager historyManager;
    Task task;
    @BeforeEach
    void BeforeEach(){
        historyManager = new InMemoryHistoryManager();
        LocalDateTime dateTime = LocalDateTime.of(2024, Month.MARCH, 12, 6,0);
        task = new Task("Название 1", "Описание 1", Duration.ofMinutes(15), dateTime);
    }

    @Test
    void add() {
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void checkThatDuplicatesNotSave(){
        for (int i = 0; i < 3; i++){
            historyManager.add(task);
        }
        int size =  historyManager.getHistory().size();
        assertEquals(1,size);
    }

    @Test
    void remove() {
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        historyManager.remove(task.getId());
        history = historyManager.getHistory();
        assertEquals(0, history.size());
    }

}