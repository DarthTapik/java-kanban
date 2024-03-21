package tasks;

import java.time.LocalDateTime;
import java.util.Comparator;

public class TaskByStartTimeComparator implements Comparator<Task> {
    @Override
    public int compare(Task o1, Task o2) {

        return o1.startTime.compareTo(o2.startTime);
    }
}
