package manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    InMemoryTaskManager taskManager;

    @BeforeEach
    void BeforeEach(){
        taskManager = new InMemoryTaskManager();
    }
    @AfterEach
    void afterEach(){
        taskManager.deleteAll();
    }

    @Test
    void addNewSubTask() {
        Epic epic = new Epic("0", "0");
        taskManager.addEpic(epic);
        SubTask subTask = new SubTask("Test addNewEpic", "Test addNewEpic description", 0);
        taskManager.addSubTask(subTask);
        final int subTaskId = subTask.getId();

        final SubTask savedSubTask = taskManager.getSubTask(subTaskId);

        assertNotNull(savedSubTask, "Задача не найдена.");
        assertEquals(subTask, savedSubTask, "Задачи не совпадают.");

        final List<SubTask> subTasks = taskManager.getAllSubTask();

        assertNotNull(subTasks, "Задачи не возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество задач.");
        assertEquals(savedSubTask, subTasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addNewTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        taskManager.addTask(task);
        final int taskId = task.getId();

        final Task savedTask = taskManager.getTask(taskId);


        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTask();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addNewEpic() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        taskManager.addEpic(epic);
        final int epicId = epic.getId();

        final Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final List<Epic> epics = taskManager.getAllEpic();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    void tryAddSubTaskToSubTask(){
        Epic epic = new Epic("Test epic", "Test epic description");
        SubTask subTask1 = new SubTask("First test subTask", "First test subTask desc", 0);
        SubTask subTask2 = new SubTask("Second test subTask", "Second test subTask desc", 1);
        taskManager.addEpic(epic);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        int size = taskManager.getAllSubTask().size();
        assertEquals(1, size, "Количество не совпадает");

    }

    @Test
    void checkThatTaskUpdates(){
        Task task = new Task("Before update","Before update desc");
        taskManager.addTask(task);
        task.setDescription("After update desc");
        task.setName("After update");
        taskManager.updateTask(task);
        Task task1 = taskManager.getTask(task.getId());
        assertEquals(task, task1, "Задача не обновилась");
    }

    @Test
    void checkThatStatusOfEpicUpdates(){
        Epic epic = new Epic("Epic", "Epic description");
        taskManager.addEpic(epic);
        assertEquals(Status.NEW ,epic.getStatus());
        SubTask subTask = new SubTask("SubTask", "Subtask description", Status.IN_PROGRESS, 0);
        taskManager.addSubTask(subTask);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
        subTask.setStatus(Status.DONE);
        taskManager.updateSubTask(subTask);
        assertEquals(Status.DONE, epic.getStatus());
    }

}