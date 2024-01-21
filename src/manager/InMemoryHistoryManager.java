package manager;

import tasks.Task;

import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager{

    final static int maxHistorySize = 10;
    LinkedList<Task> history = new LinkedList<>();

    @Override
    public LinkedList<Task> getHistory() {
        return history;
    }

    @Override
    public void add(Task task){
        if (task == null){
            return;
        }

        if (history.size() == maxHistorySize){
            history.removeFirst();
        }

        history.add(task);
    }
}
