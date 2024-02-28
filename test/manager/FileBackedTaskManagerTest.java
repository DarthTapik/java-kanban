package manager;

import manager.exceptions.ManagerLoadException;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest {
    @Test
    public void testSaveAndLoadEmptyFile() throws IOException {
        File testFile;
        testFile = File.createTempFile("test", ".csv");

        FileBackedTaskManager taskManager = new FileBackedTaskManager(testFile);

        taskManager.deleteAll();
        try {
            taskManager.loadFromFile(testFile);
        } catch (ManagerLoadException e) {
            System.out.println("Ошибка чтения файла");
            throw new RuntimeException();
        }

        assertTrue(taskManager.getAllTask().isEmpty());
        assertTrue(taskManager.getAllSubTask().isEmpty());
        assertTrue(taskManager.getAllEpic().isEmpty());
    }

    @Test
    public void testSaveAndLoadMultipleTasks() throws IOException {
        File testFile = File.createTempFile("test", ".csv");
        FileBackedTaskManager taskManager = new FileBackedTaskManager(testFile);

        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        taskManager = new FileBackedTaskManager(testFile);
        try {
            taskManager.loadFromFile(testFile);
        } catch (ManagerLoadException e) {
            System.out.println("Ошибка чтения файла");
        }
        assertEquals(2, taskManager.getAllTask().size());
    }

}
