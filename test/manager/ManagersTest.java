package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.HttpURLConnection;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void CheckThatMethodsReturnsNotNullObjects() {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();
        Assertions.assertNotNull(taskManager);
        Assertions.assertNotNull(historyManager);

    }
}