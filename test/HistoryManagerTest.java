import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void add_shouldAddTaskToHistory() {
        Task task = new Task(1, "Test task", "Desc", TaskStatus.NEW);
        historyManager.add(task);

        final List<Task> history = historyManager.getHistory();

        assertNotNull(history, "История не должна быть null.");
        assertEquals(1, history.size(), "История должна содержать 1 задачу.");
        assertEquals(task, history.get(0), "Добавленная задача не совпадает.");
    }

    @Test
    void add_shouldLimitHistorySizeToTen() {
        for (int i = 1; i <= 11; i++) {
            historyManager.add(new Task(i, "Task " + i, "Desc", TaskStatus.NEW));
        }

        List<Task> history = historyManager.getHistory();

        assertEquals(10, history.size(), "Размер истории должен быть ограничен 10.");


        assertEquals(2, history.get(0).getId(), "Самая старая оставшаяся задача должна быть ID=2.");

        assertEquals(11, history.get(9).getId(), "Последняя добавленная задача должна быть ID=11.");
    }

    @Test
    void add_shouldHandleNullTask() {
        historyManager.add(null);
        assertTrue(historyManager.getHistory().isEmpty(), "Добавление null не должно менять историю.");
    }
}