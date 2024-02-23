package manager;

import manager.exceptions.ManagerLoadException;
import manager.exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private File fileName;

    public FileBackedTaskManager(File file) {
        super();
        fileName = file;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println("Ошибка записи файла");
        }
    }

    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println("Ошибка записи файла");
        }
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println("Ошибка записи файла");
        }
    }

    @Override
    public List<Task> getAllTask() {
        return super.getAllTask();

    }

    @Override
    public List<SubTask> getAllSubTask() {
        return super.getAllSubTask();
    }

    @Override
    public List<Epic> getAllEpic() {
        return super.getAllEpic();
    }

    @Override
    public List<SubTask> getSubTasksByEpicId(int id) {
        return super.getSubTasksByEpicId(id);

    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println("Ошибка записи файла");
        }
        return task;

    }

    @Override
    public SubTask getSubTask(int id) {
        SubTask subTask = super.getSubTask(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println("Ошибка записи файла");
        }
        return subTask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println("Ошибка записи файла");
        }
        return epic;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println("Ошибка записи файла");
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println("Ошибка записи файла");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println("Ошибка записи файла");
        }
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println("Ошибка записи файла");
        }
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println("Ошибка записи файла");
        }
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println("Ошибка записи файла");
        }
    }

    @Override
    public void deleteAll() {
        super.deleteAll();
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println("Ошибка записи файла");
        }
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println("Ошибка записи файла");
        }
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println("Ошибка записи файла");
        }
    }

    @Override
    public void deleteAllSubTask() {
        super.deleteAllSubTask();
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println("Ошибка записи файла");
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> history = super.getHistory();
        return history;
    }

    private void save() throws ManagerSaveException {

        try  {
            Writer out = new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8);
            List<Epic> epicList = getAllEpic();
            List<Task> taskList = getAllTask();
            List<SubTask> subTaskList = getAllSubTask();
            List<Task> history = getHistory();
            out.write("id,type,name,status,description,epic \n");
            for (Epic epic : epicList){
                out.write(CSVTaskFormatter.epicToString(epic) + "\n");
            }

            for (Task task : taskList){
                out.write(CSVTaskFormatter.taskToString(task) + "\n");
            }

            for (SubTask subTask : subTaskList){
                out.write(CSVTaskFormatter.subTaskToString(subTask) + "\n");
            }
            out.write(" \n");

            for (Task task : history){
                out.write(task.getId() + ",");
            }

            out.close();


        } catch(FileNotFoundException e) {
            throw new ManagerSaveException(e);
        } catch(IOException e){
            throw new ManagerSaveException(e);
        }
    }

    public void loadFromFile(File file) throws ManagerLoadException {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    new FileInputStream(fileName), StandardCharsets.UTF_8));
            in.readLine();
            boolean stringTask = true;
            while (in.ready()){

                String line = in.readLine();

                if (line.isBlank()){
                    stringTask = false;
                }

                if (stringTask){
                    Task task = CSVTaskFormatter.stringToTask(line);

                    if (task == null){
                        return;
                    }

                    if (task.getType().equals(TaskType.TASK)){
                        super.forceAddTask(task);
                    } else if (task.getType().equals(TaskType.EPIC)){
                        Epic epic = (Epic) task;
                        super.forceAddEpic(epic);
                    } else if (task.getType().equals(TaskType.SUBTASK)){
                        super.forceAddSubTask((SubTask) task);
                    }
                } else {
                    if (in.ready()) {
                        line = in.readLine();
                        String history[] = line.split(",");
                        for (int i = 0; i < history.length; i++) {
                            int id = Integer.parseInt(history[i]);
                            historyManager.add(super.findTask(id));
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new ManagerLoadException(e);
        } catch (IOException e) {
            throw new ManagerLoadException(e);
        }
    }


}
