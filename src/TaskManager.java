import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> taskList;
    private HashMap<Integer, SubTask> subTaskList;
    private HashMap<Integer, Epic> epicList;
    private int id = 0;
    public TaskManager(){
        taskList = new HashMap<>();
        subTaskList = new HashMap<>();
        epicList = new HashMap<>();
        System.out.println("Менеджер задач инициализирован");
    }

    public boolean addTask(Task task){
        System.out.println("Задача " + id + " Добавлена");
        task.setId(id);
        taskList.put(id++,task);
        return true;
    }

    public boolean addSubTask(SubTask subTask){
        subTask.setId(id);
        if (epicList.containsKey(subTask.getEpicId())) {
            System.out.println("Подзадача " + id + " Добавлена");
            Epic epic = epicList.get(subTask.getEpicId());
            subTaskList.put(id++, subTask);
            epic.addSubTask(subTask);
            epic.isCompleted();
            return true;
        }
        System.out.println("Отсутствует Эпик заданный в подзадаче");
        return false;
    }

    public boolean addEpic(Epic epic){
        System.out.println("Эпик " + id + " Добавлен");
        epic.setId(id);
        epicList.put(id++, epic);
        return true;
    }

    public HashMap<Integer, Task> getAllTask(){
        return  taskList;
    }

    public HashMap<Integer, SubTask> getAllSubTask(){
        return  subTaskList;
    }

    public HashMap<Integer, Epic> getAllEpic(){
        return  epicList;
    }


    public ArrayList<SubTask> getSubTasksByEpicId(int id){
        if (epicList.containsKey(id)){
            return epicList.get(id).getSubTaskList();
        }
        System.out.println("Ключ " + id + " не найден");
        return null;
    }

    public Task getTask(int id){
        if (taskList.containsKey(id)){
            return taskList.get(id);
        } else{
            System.out.println("Задача не найдена");
            return null;
        }
    }

    public SubTask getSubTask(int id){
        if (subTaskList.containsKey(id)){
            return subTaskList.get(id);
        } else{
            System.out.println("Подзадача не найдена");
            return null;
        }
    }

    public Epic getEpic(int id){
        if (epicList.containsKey(id)){
            return epicList.get(id);
        } else{
            System.out.println("Эпик не найден");
            return null;
        }
    }

    public boolean updateTask(Task task){
        if (taskList.containsKey(task.getId())){
            taskList.put(task.getId(), task);
            System.out.println("Задача " + task.getId() + " Обновлена");
            return true;
        }
        System.out.println("Ключ не найден");
        return false;
    }

    public boolean updateSubTask(SubTask subTask){
        if (subTaskList.containsKey(subTask.getId())) {
            if (epicList.containsKey(subTask.getEpicId())) {
                Epic epic = epicList.get(subTask.getEpicId());
                epic.removeSubTask(getSubTask(subTask.getId())); // Удаление для избежания повторений в списке
                subTaskList.put(subTask.getId(), subTask);
                epic.addSubTask(subTask);
                epic.isCompleted();
                System.out.println("Подзадача " + subTask.getId() + " Обновлена");
                return true;
            } else {
                System.out.println("Отстутствует эпик заданный в подзадаче");
                return false;
            }
        }
        System.out.println("Ключ не найден");
        return false;
    }

    public boolean updateEpic(Epic epic){
        if (epicList.containsKey(epic.getId())) {
            epicList.put(epic.getId(), epic);
            return true;
        }
        System.out.println("Ключ не найден");
        return false;
    }

    public boolean deleteTask(int id){
        if (taskList.containsKey(id)){
            taskList.remove(id);
            System.out.println("Задача " + id + " удалена");
            return true;
        }
        System.out.println("Ключ "+ id +" не найден");
        return false;
    }

    public boolean deleteSubTask(int id){
        if (subTaskList.containsKey(id)){
            SubTask subTask = subTaskList.get(id);
            subTaskList.remove(id);
            Epic epic = epicList.get(subTask.getEpicId());
            epic.removeSubTask(subTask);
            epic.isCompleted();
            System.out.println("Подзадача " + id + " удалена");
            return true;
        }
        System.out.println("Ключ "+ id +" не найден");
        return false;
    }

    public boolean deleteEpic(int id) {
        if (epicList.containsKey(id)) {
            Epic epic = epicList.get(id);
            ArrayList<SubTask> subTasks = new ArrayList<>(epic.getSubTaskList()); // клонирование списка
            for (SubTask subTask : subTasks) {
                deleteSubTask(subTask.getId());
            }
            epicList.remove(id);

            System.out.println("Эпик " + id + " удален");
            return true;
        }
        System.out.println("Ключ " + id + " не найден");
        return false;
    }

}
