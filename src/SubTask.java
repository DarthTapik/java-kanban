public class SubTask extends Task {
    protected int epicId;
    public SubTask(String name, String description, int epicId){
        super(name, description);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, Status status, int epicId){
        super(name, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}

