package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class ManagersTest {

    @Test
    void CheckThatMethodsReturnsNotNullObjects() {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();
        Assertions.assertNotNull(taskManager);
        Assertions.assertNotNull(historyManager);

    }
}