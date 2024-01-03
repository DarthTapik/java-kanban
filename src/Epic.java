import java.util.ArrayList;

public class Epic extends Task{
    ArrayList<SubTask> subTaskList;

    public Epic(String name, String description){
        super(name,description);
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

    public void isCompleted() {
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
