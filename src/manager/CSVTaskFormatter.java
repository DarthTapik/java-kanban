package manager;

import tasks.*;

public class CSVTaskFormatter {

    public static String taskToString(Task task){

        return String.format("%d, %s, %s, %s, %s",
                task.getId(), "TASK", task.getName(), task.getStatus().name(), task.getDescription());
    }

    public static String epicToString(Epic epic){
        return String.format("%d, %s, %s, %s, %s",
                epic.getId(),"EPIC", epic.getName(), epic.getStatus().name(), epic.getDescription());
    }

    public static String subTaskToString(SubTask subTask){
        return String.format("%d, %s, %s, %s, %s, %d",
                subTask.getId(),"SUBTASK", subTask.getName(), subTask.getStatus().name(), subTask.getDescription(),
                subTask.getEpicId());
    }

    public static Task stringToTask(String taskString){
            String[] taskValues = taskString.split(", ");

            int id = Integer.parseInt(taskValues[0]);
            String type = taskValues[1];
            String name = taskValues[2];
            String status = taskValues[3];
            String description = taskValues[4];

            Task task;

            switch (type) {
                case("TASK"):
                    task = new Task(id, name, Status.valueOf(status), description, TaskType.TASK);
                    break;
                case ("EPIC"):
                    task = new Epic(id, name, Status.valueOf(status), description, TaskType.EPIC);
                    break;
                case ("SUBTASK"):
                    int epicId = Integer.parseInt(taskValues[5]);
                    task = new SubTask(id, name, Status.valueOf(status), description, epicId, TaskType.SUBTASK);
                    break;
                default:
                    return null;
            }

            return task;
    }
}
