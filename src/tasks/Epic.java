package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    List<SubTask> subTaskList;
    LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description, Duration.ofMillis(0), LocalDateTime.of(0, 1, 1, 0, 0));
        subTaskList = new ArrayList<>();
        super.type = TaskType.EPIC;
        endTime = startTime.plus(duration);
    }

    public Epic(Epic epic) {
        super((Task) epic);
        this.subTaskList = epic.subTaskList;
        super.type = TaskType.EPIC;
        endTime = startTime.plus(duration);
    }

    public Epic(int id, String name, Status status, String description, TaskType type,
                Duration duration, LocalDateTime startTime) {
        super(id, name, status, description, type, duration, startTime);
        subTaskList = new ArrayList<>();
        endTime = startTime.plus(duration);
    }

    public boolean addSubTask(SubTask subTask) {
        return subTaskList.add(subTask);
    }

    public boolean removeSubTask(SubTask subTask) {
        return subTaskList.remove(subTask);
    }

    public List<SubTask> getSubTaskList() {
        return subTaskList;
    }

    public void updateStatusAndTime() {
        boolean doneFlag = true;
        boolean newFlag = true;
        if (subTaskList.isEmpty()) {
            this.status = Status.NEW;
            this.setStartTime(null);
            this.endTime = null;
            this.setDuration(null);
        }
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

            if (subTaskList.size() == 1) {
                this.setStartTime(subTask.getStartTime());
                this.endTime = subTask.getEndTime();
            }

            if (subTask.getStartTime().isBefore(this.getStartTime())) {
                this.setStartTime(subTask.getStartTime());
            }

            if (subTask.getEndTime().isAfter(this.endTime)) {
                this.endTime = subTask.getEndTime();
            }

            this.setDuration(Duration.between(this.getStartTime(), endTime));
        }
    }

    public void setSubTaskList(List<SubTask> subTaskList) {
        this.subTaskList = subTaskList;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
