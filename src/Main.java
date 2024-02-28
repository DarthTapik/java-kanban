import manager.FileBackedTaskManager;
import manager.Managers;
import manager.TaskManager;
import manager.exceptions.ManagerLoadException;
import manager.exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.File;


public class Main {
    public static void main(String[] args) throws ManagerSaveException {
        Task task1;
        Task task2;
        Epic epic1;
        Epic epic2;
        SubTask subTask1;
        SubTask subTask2;
        SubTask subTask3;

            File file = new File("src/resources/task.csv");

        TaskManager manager = Managers.getDefault();
        System.out.println("Инициализация задач");
        task1 = new Task("Задача 1", "Описание");
        manager.addTask(task1);
        task2 = new Task("Задача 2", "Описание");
        manager.addTask(task2);
        epic1 = new Epic("Эпик с подзадачами","Описание");
        manager.addEpic(epic1);
        subTask1 = new SubTask("Подзадача 1", "Описание", 2);
        subTask2 = new SubTask("Подзадача 2", "Описание", 2);
        subTask3 = new SubTask("Подзадача 3", "Описание", 2);
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        manager.addSubTask(subTask3);
        epic2 = new Epic("Эпик без позадач", "Описание");
        manager.addEpic(epic2);
        printAllTasks(manager);

        manager.getTask(0);
        manager.getTask(0);
        manager.getEpic(2);
        manager.getTask(1);
        manager.getSubTask(4);
        manager.getSubTask(3);
        manager.getEpic(6);
        System.out.println(manager.getHistory());
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        try {
            fileBackedTaskManager.loadFromFile(file);
        } catch (ManagerLoadException e) {
            System.out.println("Ошибка чтения файла");
            throw new ManagerSaveException(e);
        }
        printAllTasks(fileBackedTaskManager);

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
