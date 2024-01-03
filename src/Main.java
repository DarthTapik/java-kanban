public class Main {
    public static void main(String[] args) {
        Task task1;
        Epic epic1;
        SubTask subTask1;
        SubTask subTask2;
        TaskManager taskManager = new TaskManager();

        System.out.println("Тестирование задач");
        System.out.println("Добавление задачи");
        task1 = new Task("Название","Описание");
        taskManager.addTask(task1);
        System.out.println(taskManager.getAllTask());

        System.out.println("Обновление задачи");
        task1.setStatus(Status.DONE);
        taskManager.updateTask(task1);
        System.out.println(taskManager.getAllTask());

        System.out.println("Удаление задачи");
        taskManager.deleteTask(0);

        System.out.println("Обновление несуществующей задачи");
        task1.setId(99);
        taskManager.updateTask(task1);

        System.out.println("Удаление задачи по несуществующему ключу");
        taskManager.deleteTask(99);

        System.out.println("Добавление зададчи с заданным id");
        taskManager.addTask(task1);
        System.out.println(taskManager.getAllTask());

        System.out.println("Тестирование эпиков и подзадач");
        System.out.println("Добавление эпика");
        epic1 = new Epic("Название","Описание");
        taskManager.addEpic(epic1);
        System.out.println(taskManager.getAllEpic());

        System.out.println("Добавление подзадач");
        subTask1 = new SubTask("Название 1", "Описание 1", 2);
        taskManager.addSubTask(subTask1);
        subTask2 = new SubTask("Название 2", "Описание 2", 2);
        taskManager.addSubTask(subTask2);
        System.out.println(taskManager.getAllSubTask());

        System.out.println("Вывод подзадач эпика по ключу");
        System.out.println(taskManager.getSubTasksByEpicId(2));

        System.out.println("Обновление статуса подзадач");
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.DONE);
        taskManager.updateSubTask(subTask1);
        taskManager.updateSubTask(subTask2);
        System.out.println(taskManager.getAllEpic());

        subTask1.setStatus(Status.IN_PROGRESS);
        subTask2.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubTask(subTask1);
        taskManager.updateSubTask(subTask2);
        System.out.println(taskManager.getAllSubTask());
        System.out.println(taskManager.getAllEpic());

        System.out.println("Статус эпика после удаления подзадач");
        taskManager.deleteSubTask(3);
        taskManager.deleteSubTask(4);
        System.out.println(taskManager.getAllEpic());

        System.out.println("Попытка добовления подзадачи с несуществующим ключем эпика");
        subTask2.setEpicId(99);
        subTask2.setId(99);
        taskManager.addSubTask(subTask2);
        System.out.println(taskManager.getAllSubTask());

        System.out.println("Удаление эпика с подзадачами");
        subTask1 = new SubTask("Название 1", "Описание 1", 2);
        taskManager.addSubTask(subTask1);
        subTask2 = new SubTask("Название 2", "Описание 2", 2);
        taskManager.addSubTask(subTask2);
        System.out.println(taskManager.getEpic(2).getSubTaskList());
        taskManager.deleteEpic(2);
        System.out.println(taskManager.getAllSubTask());

    }
}
