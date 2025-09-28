import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest {
    private TaskManager taskManager;
    private Task task;
    private Epic epic;
    private Subtask subtask;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();

        Task createdTask = new Task("Test Task", "Desc Task", TaskStatus.NEW);
        task = taskManager.createTask(createdTask);

        Epic createdEpic = new Epic("Test Epic", "Desc Epic");
        epic = taskManager.createEpic(createdEpic);

        Subtask createdSubtask = new Subtask("Test Subtask", "Desc Subtask", TaskStatus.NEW, epic.getId());
        subtask = taskManager.createSubtask(createdSubtask);
    }

    @Test
    void getById_shouldAddTasksToHistoryInOrder() {
        taskManager.getTaskById(task.getId());
        taskManager.getEpicById(epic.getId());
        taskManager.getSubtaskById(subtask.getId());

        List<Task> history = taskManager.getHistory();

        assertEquals(3, history.size(), "История должна содержать 3 просмотра.");

        // Проверяем порядок
        assertEquals(task.getId(), history.get(0).getId(), "Первым должен быть Task.");
        assertEquals(epic.getId(), history.get(1).getId(), "Вторым должен быть Epic.");
        assertEquals(subtask.getId(), history.get(2).getId(), "Третьим должен быть Subtask.");
    }


    @Test
    void createTask_shouldAddNewTaskAndFindItById() {
        final Task savedTask = taskManager.getTaskById(task.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task.getId(), savedTask.getId(), "ID задачи не совпадает.");
        assertEquals(task.getName(), savedTask.getName(), "Имя задачи не совпадает.");
    }

    @Test
    void tasksWithId_shouldNotConflictWithGeneratedId() {

        Task manualIdTask = new Task(100, "Manual Task", "Desc", TaskStatus.NEW);
        taskManager.createTask(manualIdTask); // ID = 100

        Task autoIdTask = new Task("Auto Task", "Desc", TaskStatus.NEW);
        Task savedAutoTask = taskManager.createTask(autoIdTask);

        assertNotNull(taskManager.getTaskById(savedAutoTask.getId()), "Задача с авто-ID не найдена.");
        assertNotNull(taskManager.getTaskById(100), "Задача с ручным ID не найдена.");
    }


    @Test
    void createTask_shouldKeepTaskImmutableAfterAddition() {
        Task savedTask = taskManager.getTaskById(task.getId());
        String originalName = savedTask.getName();
        int originalId = savedTask.getId();

        savedTask.setStatus(TaskStatus.DONE);
        savedTask.setName("NEW NAME");

        Task checkTask = taskManager.getTaskById(originalId);


        assertEquals(originalName, checkTask.getName(), "Имя задачи в менеджере изменилось локально.");
        assertEquals(TaskStatus.NEW, checkTask.getStatus(), "Статус задачи в менеджере изменился локально.");
    }


    @Test
    void epicCycle_shouldNotAddEpicAsItsOwnSubtask() {
        Subtask selfReferencingSubtask = new Subtask(epic.getId(), "Bad Subtask", "Desc", TaskStatus.NEW, epic.getId());
        Subtask createdSelfRef = taskManager.createSubtask(selfReferencingSubtask);
        assertNull(createdSelfRef, "Подзадача не должна создаваться, если ее ID равен ID эпика.");


        Subtask newSubtaskWithWrongId = new Subtask("Test Ref", "Desc", task.getId());
        Subtask createdWrongId = taskManager.createSubtask(newSubtaskWithWrongId);
        assertNull(createdWrongId, "Подзадача не должна создаваться с не-Epic ID.");
    }
}