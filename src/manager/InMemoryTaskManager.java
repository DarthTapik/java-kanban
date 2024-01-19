package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private Map<Integer, Task> taskList = new HashMap<>();
    private Map<Integer, SubTask> subTaskList = new HashMap<>();
    private Map<Integer, Epic> epicList = new HashMap<>();
    private HistoryManager historyManager = Managers.getDefaultHistory();
    private int id = 0;
    public InMemoryTaskManager(){
        System.out.println("Менеджер задач инициализирован");
    }

    public void addTask(Task task){
        System.out.println("Задача " + id + " Добавлена");
        task.setId(id);
        taskList.put(id++,task);
        /*
         Не совсем понимаю зачем тут преинкремент, я ввожу в таблицу id после инкрементирую для следующих записей
        */
    }

    public void addSubTask(SubTask subTask){
        subTask.setId(id);
        if (epicList.containsKey(subTask.getEpicId())) {
            System.out.println("Подзадача " + id + " Добавлена");
            Epic epic = epicList.get(subTask.getEpicId());
            subTaskList.put(id++, subTask);
            epic.addSubTask(subTask);
            epic.updateStatus();
            return;
        }
        System.out.println("Отсутствует Эпик заданный в подзадаче");
    }

    public void addEpic(Epic epic){
        System.out.println("Эпик " + id + " Добавлен");
        epic.setId(id);
        epicList.put(id++, epic);
    }

    public List<Task> getAllTask(){
        return new ArrayList<>(taskList.values());
    }

    public List<SubTask> getAllSubTask(){
        return new ArrayList<>(subTaskList.values());
    }

    public List<Epic> getAllEpic(){
        return new ArrayList<>(epicList.values());
    }


    public List<SubTask> getSubTasksByEpicId(int id){
        if (epicList.containsKey(id)){
            return epicList.get(id).getSubTaskList();
        }
        System.out.println("Ключ " + id + " не найден");
        return null;
    }

    public Task getTask(int id){
        if (taskList.containsKey(id)){
            Task task = taskList.get(id);
            historyManager.add(task);
            return task;
        } else{
            System.out.println("Задача не найдена");
            return null;
        }
    }

    public SubTask getSubTask(int id){
        if (subTaskList.containsKey(id)){
            SubTask subTask = subTaskList.get(id);
            historyManager.add(subTask);
            return subTask;
        } else{
            System.out.println("Подзадача не найдена");
            return null;
        }
    }

    public Epic getEpic(int id){
        if (epicList.containsKey(id)){
            Epic epic = epicList.get(id);
            historyManager.add(epic);
            return epic;
        } else{
            System.out.println("Эпик не найден");
            return null;
        }
    }

    public void updateTask(Task task){
        if (taskList.containsKey(task.getId())){
            taskList.put(task.getId(), task);
            System.out.println("Задача " + task.getId() + " Обновлена");
        } else {
            System.out.println("Ключ не найден");
        }
    }

    public void updateSubTask(SubTask subTask){
        if (subTaskList.containsKey(subTask.getId())) {
            if (epicList.containsKey(subTask.getEpicId())) {
                Epic epic = epicList.get(subTask.getEpicId());
                epic.removeSubTask(getSubTask(subTask.getId())); // Удаление для избежания повторений в списке
                subTaskList.put(subTask.getId(), subTask);
                epic.addSubTask(subTask);
                epic.updateStatus();
                System.out.println("Подзадача " + subTask.getId() + " Обновлена");
            } else {
                System.out.println("Отстутствует эпик заданный в подзадаче");
            }
        } else {
            System.out.println("Ключ не найден");
        }
    }

    public void updateEpic(Epic epic){
        if (epicList.containsKey(epic.getId())) {
            epicList.put(epic.getId(), epic);
        } else {
            System.out.println("Ключ не найден");
        }
    }

    public void deleteTask(int id){
        if (taskList.containsKey(id)){
            taskList.remove(id);
            System.out.println("Задача " + id + " удалена");
        } else {
            System.out.println("Ключ " + id + " не найден");
        }
    }

    public void deleteSubTask(int id){
        if (subTaskList.containsKey(id)){
            SubTask subTask = subTaskList.get(id);
            subTaskList.remove(id);
            Epic epic = epicList.get(subTask.getEpicId());
            epic.removeSubTask(subTask);
            epic.updateStatus();
            System.out.println("Подзадача " + id + " удалена");
        } else {
            System.out.println("Ключ " + id + " не найден");
        }
    }

    public void deleteEpic(int id) {
        if (epicList.containsKey(id)) {
            Epic epic = epicList.get(id);
            ArrayList<SubTask> subTasks = new ArrayList<>(epic.getSubTaskList()); // клонирование списка
            for (SubTask subTask : subTasks) {
                deleteSubTask(subTask.getId());
            }
            epicList.remove(id);
            System.out.println("Эпик " + id + " удален");

        } else {
            System.out.println("Ключ " + id + " не найден");
        }
    }

    public void deleteAll(){
        epicList.clear();
        taskList.clear();
        subTaskList.clear();
        System.out.println("Все задачи удалены");
    }

    public void deleteAllTasks(){
        taskList.clear();
        System.out.println("Задачи удалены");
    }

    public void deleteAllEpic(){
        epicList.clear();
        subTaskList.clear();
        System.out.println("Эпики и их подзадачи удалены");
    }

    public void deleteAllSubTask(){
        subTaskList.clear();
        System.out.println("Подзадачи удалены");
    }

    public List<Task> getHistory(){
        return historyManager.getHistory();
    }
}
