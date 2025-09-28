import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ManagersTest {

    @Test
    void getDefault_shouldReturnInitializedTaskManager() {
        final TaskManager manager = Managers.getDefault();

        assertNotNull(manager, "Метод getDefault должен возвращать не null объект TaskManager.");
        assertNotNull(manager.getAllTasks(), "Менеджер должен быть готов вернуть список задач.");
    }

    @Test
    void getDefaultHistory_shouldReturnInitializedHistoryManager() {
        final HistoryManager historyManager = Managers.getDefaultHistory();

        assertNotNull(historyManager, "Метод getDefaultHistory должен возвращать не null объект HistoryManager.");
        assertNotNull(historyManager.getHistory(), "Менеджер истории должен быть готов вернуть список истории.");
    }
}