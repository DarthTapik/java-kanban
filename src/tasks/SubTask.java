package tasks;

public class SubTask extends Task {
    protected int epicId;
    public SubTask(String name, String description, int epicId){
        super(name, description);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, Status status, int epicId){
        super(name, description, status);
        this.epicId = epicId;
        super.type = TaskType.SUBTASK;
    }

    public SubTask(SubTask subTask){
        super((Task) subTask);
        this.epicId = subTask.epicId;
        super.type = TaskType.SUBTASK;
    }

    public SubTask(int id, String name, Status status, String description, int epicId, TaskType type) {
        super(id, name, status, description, type);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}

