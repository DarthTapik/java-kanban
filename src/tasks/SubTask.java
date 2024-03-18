package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    protected int epicId;
    public SubTask(String name, String description, int epicId, Duration duration, LocalDateTime startTime){
        super(name, description, duration, startTime);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, Status status,
                   int epicId, Duration duration, LocalDateTime startTime){
        super(name, description, status, duration, startTime);
        this.epicId = epicId;
        super.type = TaskType.SUBTASK;
    }

    public SubTask(SubTask subTask){
        super((Task) subTask);
        this.epicId = subTask.epicId;
        super.type = TaskType.SUBTASK;
    }

    public SubTask(int id, String name, Status status,
                   String description, int epicId, TaskType type, Duration duration, LocalDateTime startTime) {
        super(id, name, status, description, type, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}

