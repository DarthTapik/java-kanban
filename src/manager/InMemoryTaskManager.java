package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskByStartTimeComparator;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private Map<Integer, Task> taskList = new HashMap<>();
    private Map<Integer, SubTask> subTaskList = new HashMap<>();
    private Map<Integer, Epic> epicList = new HashMap<>();
    private Comparator<Task> taskComparator = new TaskByStartTimeComparator();
    private Set<Task> sortedList = new TreeSet<>(taskComparator);
    protected HistoryManager historyManager = Managers.getDefaultHistory();
    private int id = 1;

    public InMemoryTaskManager() {
        System.out.println("Менеджер задач инициализирован");
    }

    public void addTask(Task task) {
        if (validateTask(task)) {
            System.out.println("Задача " + id + " Добавлена");
            task.setId(id);
            taskList.put(id++, new Task(task));
            sortedList.add(new Task(task));
        }
    }

    public void addSubTask(SubTask subTask) {
        if (validateTask(subTask)) {
            subTask.setId(id);
            if (epicList.containsKey(subTask.getEpicId())) {
                System.out.println("Подзадача " + id + " Добавлена");
                Epic epic = epicList.get(subTask.getEpicId());
                SubTask subTaskCopy = new SubTask(subTask);
                subTaskList.put(id++, subTaskCopy);
                epic.addSubTask(subTaskCopy);
                epic.updateStatusAndTime();
                sortedList.add(new SubTask(subTask));
                return;
            }
            System.out.println("Отсутствует Эпик заданный в подзадаче");
        }
    }

    public void addEpic(Epic epic) {
        System.out.println("Эпик " + id + " Добавлен");
        epic.setId(id);
        epicList.put(id++, new Epic(epic));
        if (epic.getStartTime().isAfter(LocalDateTime.of(1970, 1, 1, 0, 0))) {
            sortedList.add(new Epic(epic));
        }
    }

    public List<Task> getAllTask() {
        return new ArrayList<>(taskList.values());
    }

    public List<SubTask> getAllSubTask() {
        return new ArrayList<>(subTaskList.values());
    }

    public List<Epic> getAllEpic() {
        return new ArrayList<>(epicList.values());
    }


    public List<SubTask> getSubTasksByEpicId(int id) {
        if (epicList.containsKey(id)) {
            return epicList.get(id).getSubTaskList();
        }
        System.out.println("Ключ " + id + " не найден");
        return null;
    }

    public Task getTask(int id) {
        if (taskList.containsKey(id)) {
            Task task = taskList.get(id);
            historyManager.add(task);
            return task;
        } else {
            System.out.println("Задача не найдена");
            return null;
        }
    }

    public SubTask getSubTask(int id) {
        if (subTaskList.containsKey(id)) {
            SubTask subTask = subTaskList.get(id);
            historyManager.add(subTask);
            return subTask;
        } else {
            System.out.println("Подзадача не найдена");
            return null;
        }
    }

    public Epic getEpic(int id) {
        if (epicList.containsKey(id)) {
            Epic epic = epicList.get(id);
            historyManager.add(epic);
            return epic;
        } else {
            System.out.println("Эпик не найден");
            return null;
        }
    }

    public void updateTask(Task task) {

        if (taskList.containsKey(task.getId())) {
            sortedList.remove(taskList.get(task.getId()));
            if (validateTask(task)) {
                taskList.put(task.getId(), task);
                sortedList.add(task);
                System.out.println("Задача " + task.getId() + " Обновлена");
            } else {
                sortedList.add(taskList.get(task.getId()));
            }
        } else {
            System.out.println("Ключ не найден");
        }

    }

    public void updateSubTask(SubTask subTask) {

        if (subTaskList.containsKey(subTask.getId())) {
            sortedList.remove(getSubTask(subTask.getId()));
            if (validateTask(subTask)) {
                if (epicList.containsKey(subTask.getEpicId())) {
                    Epic epic = epicList.get(subTask.getEpicId());
                    epic.removeSubTask(getSubTask(subTask.getId())); // Удаление для избежания повторений в списке
                    subTaskList.put(subTask.getId(), subTask);
                    epic.addSubTask(subTask);
                    epic.updateStatusAndTime();
                    sortedList.add(subTask);
                    System.out.println("Подзадача " + subTask.getId() + " Обновлена");
                } else {
                    System.out.println("Отстутствует эпик заданный в подзадаче");
                }
            } else {
                sortedList.add(getSubTask(subTask.getId()));
            }
        } else {
            System.out.println("Ключ не найден");
        }
    }


    public void updateEpic(Epic epic) {

        if (epicList.containsKey(epic.getId())) {
            sortedList.remove(getEpic(epic.getId()));
            if (validateTask(epic)) {
                epicList.put(epic.getId(), epic);
                if (epic.getStartTime().isAfter(LocalDateTime.of(1970, 1, 1, 0, 0))) {
                    sortedList.add(new Epic(epic));
                }
            } else {
                sortedList.add(getEpic(epic.getId()));
            }
        } else {
            System.out.println("Ключ не найден");
        }

    }

    public void deleteTask(int id) {
        if (taskList.containsKey(id)) {
            sortedList.remove(taskList.get(id));
            taskList.remove(id);
            historyManager.remove(id);

            System.out.println("Задача " + id + " удалена");
        } else {
            System.out.println("Ключ " + id + " не найден");
        }
    }

    public void deleteSubTask(int id) {
        if (subTaskList.containsKey(id)) {
            SubTask subTask = subTaskList.get(id);
            sortedList.remove(subTask);
            subTaskList.remove(id);
            historyManager.remove(id);
            Epic epic = epicList.get(subTask.getEpicId());
            epic.removeSubTask(subTask);
            epic.updateStatusAndTime();
            System.out.println("Подзадача " + id + " удалена");
        } else {
            System.out.println("Ключ " + id + " не найден");
        }
    }

    public void deleteEpic(int id) {
        if (epicList.containsKey(id)) {
            Epic epic = epicList.get(id);
            sortedList.remove(epic);
            subTaskList.values().stream()
                    .filter(subTask -> subTask.getEpicId() == epic.getId())
                    .forEach(subTask -> {
                                sortedList.remove(subTask);
                                historyManager.remove(subTask.getId());
                                deleteSubTask(subTask.getId());
                            }
                    );
            epicList.remove(id);
            historyManager.remove(id);
            System.out.println("Эпик " + id + " удален");

        } else {
            System.out.println("Ключ " + id + " не найден");
        }
    }

    public void deleteAll() {
        epicList.clear();
        taskList.clear();
        subTaskList.clear();
        sortedList.clear();
        historyManager = Managers.getDefaultHistory();
        System.out.println("Все задачи удалены");
    }

    public void deleteAllTasks() {
        taskList.values().stream()
                .forEach(task -> deleteTask(task.getId()));
        System.out.println("Задачи удалены");
    }

    public void deleteAllEpic() {

        epicList.values().stream()
                .forEach(epic -> deleteEpic(epic.getId()));

        System.out.println("Эпики и их подзадачи удалены");
    }

    public void deleteAllSubTask() {
        subTaskList.values().stream()
                .forEach(subTask -> deleteSubTask(subTask.getId()));
        System.out.println("Подзадачи удалены");
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(sortedList);
    }


    public boolean validateTask(Task task) {
        return !(sortedList.stream()
                .anyMatch(anyTask -> anyTask.taskOverlap(task)));
    }

    protected void forceAddTask(Task task) { // Метод для добавления задачи, без присвоения id самим менеджером
        taskList.put(task.getId(), task);
        sortedList.add(task);
        if (task.getId() > id) { // Сравниваю id для того, чтобы избежать повторов и конфликтов при добавлении
            id = task.getId() + 1; // из файла, а после через менеджер
        }
    }

    protected void forceAddEpic(Epic epic) {
        epicList.put(epic.getId(), epic);
        if (epic.getStartTime().isAfter(LocalDateTime.of(1970, 1, 1, 0, 0))) {
            sortedList.add(new Epic(epic));
        }
        if (epic.getId() > id) {
            id = epic.getId() + 1;
        }
    }

    protected void forceAddSubTask(SubTask subTask) {
        if (epicList.containsKey(subTask.getEpicId())) {
            Epic epic = epicList.get(subTask.getEpicId());
            subTaskList.put(subTask.getId(), subTask);
            epic.addSubTask(subTask);
            sortedList.add(subTask);
            if (subTask.getId() > id) {
                id = subTask.getId() + 1;
            }
        }
    }

    protected Task findTask(int id) {
        Task task = null;

        if (taskList.containsKey(id)) {
            task = taskList.get(id);
        } else if (subTaskList.containsKey(id)) {
            task = subTaskList.get(id);
        } else if (epicList.containsKey(id)) {
            task = epicList.get(id);
        }

        return task;
    }
}
