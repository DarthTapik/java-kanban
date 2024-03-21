package manager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager historyManager;
    Task task, task2, task3;

    @BeforeEach
    void BeforeEach() {
        historyManager = new InMemoryHistoryManager();
        LocalDateTime dateTime = LocalDateTime.of(2024, Month.MARCH, 12, 6, 0);
        task = new Task("Название 1", "Описание 1", Duration.ofMinutes(15), dateTime);
        task2 = new Task("Название 2", "Описание 2", Duration.ofMinutes(15), dateTime.plusMinutes(20));
        task2.setId(1);
        task3 = new Task("Название 3", "Описание 3", Duration.ofMinutes(15), dateTime.plusMinutes(20));
        task3.setId(2);
    }

    @Test
    void add() {
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void checkThatDuplicatesNotSave() {
        for (int i = 0; i < 3; i++) {
            historyManager.add(task);
        }
        int size = historyManager.getHistory().size();
        assertEquals(1, size);
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

    @Test
    void removeFromEnd() {
        historyManager.add(task);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task3.getId());
        assertEquals(2, historyManager.getHistory().size());
        List<Task> expectedHistory = new ArrayList<>();
        expectedHistory.add(task2);
        expectedHistory.add(task);

        assertEquals(expectedHistory, historyManager.getHistory());
    }

    @Test
    void removeFirst() {
        historyManager.add(task);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task.getId());
        assertEquals(2, historyManager.getHistory().size());
        List<Task> expectedHistory = new ArrayList<>();
        expectedHistory.add(task3);
        expectedHistory.add(task2);
        assertEquals(expectedHistory, historyManager.getHistory());
    }

    @Test
    void removeFrom() {
        historyManager.add(task);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task2.getId());
        assertEquals(2, historyManager.getHistory().size());
        List<Task> expectedHistory = new ArrayList<>();
        expectedHistory.add(task3);
        expectedHistory.add(task);
        assertEquals(expectedHistory, historyManager.getHistory());
    }

    @Test
    void emptyHistory() {
        assertTrue(historyManager.getHistory().isEmpty());
    }
}