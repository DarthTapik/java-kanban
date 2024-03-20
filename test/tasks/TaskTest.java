package tasks;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskTest {

    @Test
    void overLapTask() {
        LocalDateTime dateTime = LocalDateTime.of(2024, Month.MARCH, 12, 6, 0);
        Task task1 = new Task("Name", "Description", Duration.ofMinutes(15), dateTime);
        Task task2 = new Task("Name2", "Descriotion2", Duration.ofMinutes(1), dateTime.plusMinutes(17));
        assertFalse(task1.taskOverlap(task2));
        Task task3 = new Task(task1);
        assertTrue(task1.taskOverlap(task3));
        Task task4 = new Task("Name4", "Description4", Duration.ofMinutes(3), dateTime.plusMinutes(1));
        assertTrue(task1.taskOverlap(task4));
    }
}
