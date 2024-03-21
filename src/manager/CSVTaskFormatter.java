package manager;

import tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;

public class CSVTaskFormatter {

    public static String taskToString(Task task){

        return String.format("%d, %s, %s, %s, %s, %s, %d",
                task.getId(), "TASK", task.getName(), task.getStatus().name(), task.getDescription(),
                task.getStartTime().toString(), task.getDuration().toMillis());
    }

    public static String epicToString(Epic epic){
        return String.format("%d, %s, %s, %s, %s, %s, %d",
                epic.getId(),"EPIC", epic.getName(), epic.getStatus().name(), epic.getDescription(),
                epic.getStartTime().toString(), epic.getDuration().toMillis());
    }

    public static String subTaskToString(SubTask subTask){
        return String.format("%d, %s, %s, %s, %s, %s, %d, %d",
                subTask.getId(),"SUBTASK", subTask.getName(), subTask.getStatus().name(), subTask.getDescription(),
                subTask.getStartTime().toString(), subTask.getDuration().toMillis(),
                subTask.getEpicId());
    }

    public static Task stringToTask(String taskString){
            String[] taskValues = taskString.split(", ");

            int id = Integer.parseInt(taskValues[0]);
            String type = taskValues[1];
            String name = taskValues[2];
            String status = taskValues[3];
            String description = taskValues[4];
            LocalDateTime startTime = LocalDateTime.parse(taskValues[5]);
            Duration duration = Duration.ofMillis(Integer.parseInt(taskValues[6]));

            Task task;

            switch (type) {
                case("TASK"):
                    task = new Task(id, name, Status.valueOf(status), description, TaskType.TASK, duration, startTime);
                    break;
                case ("EPIC"):
                    task = new Epic(id, name, Status.valueOf(status), description, TaskType.EPIC, duration, startTime);
                    break;
                case ("SUBTASK"):
                    int epicId = Integer.parseInt(taskValues[7]);
                    task = new SubTask(id, name, Status.valueOf(status), description, epicId,
                            TaskType.SUBTASK,duration, startTime);
                    break;
                default:
                    return null;
            }

            return task;
    }
}
