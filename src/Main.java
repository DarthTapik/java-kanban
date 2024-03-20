import manager.FileBackedTaskManager;
import manager.Managers;
import manager.TaskManager;
import manager.exceptions.ManagerLoadException;
import manager.exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

public class Main {
    public static void main(String[] args) throws ManagerSaveException {
        Task task1;
        Task task2;
        Epic epic1;
        Epic epic2;
        SubTask subTask1;
        SubTask subTask2;
        SubTask subTask3;
        LocalDateTime dateTime = LocalDateTime.of(2024, Month.MARCH, 12, 6, 0);

            File file = new File("src/resources/task.csv");

        TaskManager manager = Managers.getDefault();
        System.out.println("Инициализация задач");
        task1 = new Task("Задача 1", "Описание", Duration.ofMinutes(15), dateTime);
        manager.addTask(task1);

        task2 = new Task("Задача 2", "Описание", Duration.ofMinutes(15), dateTime);
        manager.addTask(task2); // Задача не добавится
        dateTime = dateTime.plusMinutes(16);
        epic1 = new Epic("Эпик с подзадачами","Описание");
        manager.addEpic(epic1);

        subTask1 = new SubTask("Подзадача 1", "Описание", 1, Duration.ofMinutes(15), dateTime);
        dateTime = dateTime.plusMinutes(16);
        subTask2 = new SubTask("Подзадача 2", "Описание", 1, Duration.ofMinutes(15), dateTime);
        dateTime = dateTime.plusMinutes(16);
        subTask3 = new SubTask("Подзадача 3", "Описание", 1, Duration.ofMinutes(15), dateTime);
        dateTime = dateTime.plusMinutes(16);
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        manager.addSubTask(subTask3);
        epic2 = new Epic("Эпик без позадач", "Описание");
        manager.addEpic(epic2);
        printAllTasks(manager);

        FileBackedTaskManager fileBackedTaskManager = (FileBackedTaskManager) manager;

        System.out.println(fileBackedTaskManager.getPrioritizedTasks());
        System.out.println("EndTime of epic1: " + fileBackedTaskManager.getEpic(epic1.getId()).getEndTime());
        System.out.println("EndTime of lastSubTask" + fileBackedTaskManager.getSubTask(subTask3.getId()).getEndTime());

    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTask()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getAllEpic()) {
            System.out.println(epic);

            for (Task task : manager.getSubTasksByEpicId(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubTask()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
