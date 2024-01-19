import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

public class Main {
    public static void main(String[] args) {
        Task task1;
        Epic epic1;
        SubTask subTask1;
        SubTask subTask2;
        TaskManager manager = Managers.getDefault();

        System.out.println("Проверка истории простмотров задач");
        System.out.println("Инициализация задач");
        task1 = new Task("Название 1", "Описание 1");
        manager.addTask(task1);
        task1 = new Task("Название 2", "Описание 2");
        manager.addTask(task1);
        task1 = new Task("Название 3", "Описание 3");
        manager.addTask(task1);
        task1 = new Task("Название 4", "Описание 4");
        manager.addTask(task1);

        epic1 = new Epic("Название 5", "Описание 5");
        manager.addEpic(epic1);
        epic1 = new Epic("Название 6", "Описание 6");
        manager.addEpic(epic1);

        subTask1 = new SubTask("Название 7", "Описание 7", 4);
        manager.addSubTask(subTask1);
        subTask1 = new SubTask("Название 8", "Описание 8", 4);
        manager.addSubTask(subTask1);
        subTask1 = new SubTask("Название 9", "Описание 9", 5);
        manager.addSubTask(subTask1);
        subTask1 = new SubTask("Название 10", "Описание 10", 5);
        manager.addSubTask(subTask1);

        System.out.println("Пустая история");
        printAllTasks(manager);

        System.out.println("Просматриваем 1 задачу");
        System.out.println(manager.getTask(0));
        System.out.println("История:");
        System.out.println(manager.getHistory());
        System.out.println("Просматриваем остальные 9 задач");
        manager.getTask(1);
        manager.getTask(2);
        manager.getTask(3);
        manager.getEpic(4);
        manager.getEpic(5);
        manager.getSubTask(6);
        manager.getSubTask(7);
        manager.getSubTask(8);
        manager.getSubTask(9);
        System.out.println("История:");
        System.out.println(manager.getHistory());
        System.out.println("Просматриваем еще 1 задачу"); // Задача с id 0 должна удалиться из истории
        manager.getTask(3);
        System.out.println("История:");
        System.out.println(manager.getHistory());
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
