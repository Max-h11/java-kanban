import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TaskTestOnEquality {

    @Test
    void tasksWithSameId_shouldBeEqual() {

        Task task1 = new Task(1, "Task A", "Desc A", TaskStatus.NEW);
        Task task2 = new Task(1, "Task B", "Desc B", TaskStatus.DONE);

        assertEquals(task1, task2, "Задачи с одинаковым ID должны быть равны.");
        assertEquals(task1.hashCode(), task2.hashCode(), "Хэш-коды задач с одинаковым ID должны быть равны.");
    }

    @Test
    void subtasksWithSameId_shouldBeEqual() {

        Subtask subtask1 = new Subtask(2, "Sub 1", "Desc", TaskStatus.NEW, 10);
        Subtask subtask2 = new Subtask(2, "Sub 2", "Desc", TaskStatus.DONE, 20);

        assertEquals(subtask1, subtask2, "Подзадачи с одинаковым ID должны быть равны.");
    }

    @Test
    void epicsWithSameId_shouldBeEqual() {
        Epic epic1 = new Epic(3, "Epic 1", "Desc", TaskStatus.NEW);
        Epic epic2 = new Epic(3, "Epic 2", "Desc", TaskStatus.IN_PROGRESS);

        assertEquals(epic1, epic2, "Эпики с одинаковым ID должны быть равны.");
    }

    @Test
    void differentTaskTypesWithSameId_shouldNotBeEqual() {

        Task task = new Task(5, "Task", "Desc", TaskStatus.NEW);
        Epic epic = new Epic(5, "Epic", "Desc", TaskStatus.NEW);

        assertNotEquals(task, epic, "Задачи разных типов не должны быть равны, даже с одинаковым ID.");
    }
}