package manager;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.List;


public interface TaskManager {

    void addTask(Task task);

    void addSubTask(SubTask subTask);

    void addEpic(Epic epic);

    List<Task> getAllTask();

    List<SubTask> getAllSubTask();

    List<Epic> getAllEpic();


    List<SubTask> getSubTasksByEpicId(int id);

    Task getTask(int id);

    SubTask getSubTask(int id);

    Epic getEpic(int id);

    void updateTask(Task task);

    void updateSubTask(SubTask subTask);

    void updateEpic(Epic epic);

    void deleteTask(int id);

    void deleteSubTask(int id);

    void deleteEpic(int id);

    void deleteAll();

    void deleteAllTasks();

    void deleteAllEpic();

    void deleteAllSubTask();

    List<Task> getHistory();
}
