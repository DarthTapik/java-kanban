package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<SubTask> subTaskList;

    public Epic(String name, String description){
        super(name,description);
        subTaskList = new ArrayList<>();
        super.type = TaskType.EPIC;
    }

    public Epic(Epic epic){
        super((Task) epic);
        this.subTaskList = epic.subTaskList;
        super.type = TaskType.EPIC;
    }

    public Epic(int id, String name, Status status, String description, TaskType type) {
        super(id, name, status, description, type);
        subTaskList = new ArrayList<>();
    }

    public boolean addSubTask(SubTask subTask){
        return  subTaskList.add(subTask);
    }

    public boolean removeSubTask(SubTask subTask){
        return  subTaskList.remove(subTask);
    }

    public ArrayList<SubTask> getSubTaskList(){
        return subTaskList;
    }

    public void updateStatus() {
        boolean doneFlag = true;
        boolean newFlag = true;
        if (subTaskList.isEmpty())
            this.status = Status.NEW;
        for (SubTask subTask : subTaskList) {
            if (subTask.getStatus() != Status.DONE) {
                doneFlag = false;
                if (subTask.getStatus() != Status.NEW) {
                    newFlag = false;
                }
            }
            if (doneFlag) {
                status = Status.DONE;
            } else if (newFlag) {
                status = Status.NEW;
            } else {
                status = Status.IN_PROGRESS;
            }
        }
    }


}
