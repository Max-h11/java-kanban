import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, Subtask> subtasks;
    private final HistoryManager historyManager;
    private int nextId;

    public InMemoryTaskManager(HistoryManager historyManager) {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        nextId = 1;
        this.historyManager = historyManager;
    }

    public InMemoryTaskManager() {
        this(Managers.getDefaultHistory());
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
            return new Task(task);
        }
        return null;
    }

    @Override
    public Task createTask(Task task) {
        Task taskToSave = new Task(task);

        if (taskToSave.getId() > 0) {
            if (tasks.containsKey(taskToSave.getId())) {
                return null;
            }
            tasks.put(taskToSave.getId(), taskToSave);
            if (taskToSave.getId() >= nextId) {
                nextId = taskToSave.getId() + 1;
            }
            return taskToSave;
        }

        taskToSave.setId(generateId());
        tasks.put(taskToSave.getId(), taskToSave);
        return taskToSave;
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), new Task(task));
        }
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllEpics() {
        for (Epic epic : epics.values()) {
            for (Integer subtaskId : epic.getIdListOfSubtask()) {
                subtasks.remove(subtaskId);
            }
        }
        epics.clear();
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
            return new Epic(epic);
        }
        return null;
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic epicToSave = new Epic(epic);

        if (epicToSave.getId() > 0) {
            if (epics.containsKey(epicToSave.getId())) {
                return null;
            }
            epics.put(epicToSave.getId(), epicToSave);
            if (epicToSave.getId() >= nextId) {
                nextId = epicToSave.getId() + 1;
            }
            return epicToSave;
        }

        epicToSave.setId(generateId());
        epics.put(epicToSave.getId(), epicToSave);
        return epicToSave;
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Epic savedEpic = epics.get(epic.getId());
            savedEpic.setName(epic.getName());
            savedEpic.setDescription(epic.getDescription());
        }
    }

    @Override
    public void deleteEpicById(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            for (Integer subtaskId : epic.getIdListOfSubtask()) {
                subtasks.remove(subtaskId);
            }
            epics.remove(id);
        }
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getIdListOfSubtask().clear();
            updateEpicStatus(epic);
        }
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
            return new Subtask(subtask);
        }
        return null;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        if (epics.containsKey(epicId)) {
            Subtask subtaskToSave = new Subtask(subtask);

            if (subtaskToSave.getId() > 0 && subtaskToSave.getId() == epicId) {
                return null;
            }

            if (subtaskToSave.getId() > 0) {
                if (subtasks.containsKey(subtaskToSave.getId())) {
                    return null;
                }
                subtasks.put(subtaskToSave.getId(), subtaskToSave);
                if (subtaskToSave.getId() >= nextId) {
                    nextId = subtaskToSave.getId() + 1;
                }
            } else {
                subtaskToSave.setId(generateId());
                subtasks.put(subtaskToSave.getId(), subtaskToSave);
            }

            Epic epic = epics.get(epicId);
            epic.addIdOfSubtask(subtaskToSave.getId());
            updateEpicStatus(epic);
            return subtaskToSave;
        }
        return null;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            Subtask oldSubtask = subtasks.get(subtask.getId());
            Epic epic = epics.get(oldSubtask.getEpicId());
            if (epic != null) {
                if (oldSubtask.getEpicId() != subtask.getEpicId()) {
                    return;
                }
                subtasks.put(subtask.getId(), subtask);
                updateEpicStatus(epic);
            }
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.get(id);
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.deleteSubtaskId(id);
                updateEpicStatus(epic);
            }
            subtasks.remove(id);
        }
    }

    @Override
    public List<Subtask> getEpicSubtasks(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic != null) {
            List<Subtask> epicSubtasks = new ArrayList<>();
            for (Integer subtaskId : epic.getIdListOfSubtask()) {
                Subtask subtask = subtasks.get(subtaskId);
                if (subtask != null) {
                    epicSubtasks.add(subtask);
                }
            }
            return epicSubtasks;
        }
        return new ArrayList<>();
    }

    private void updateEpicStatus(Epic epic) {
        if (epic.getIdListOfSubtask().isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }

        int doneCount = 0;
        int newCount = 0;
        int activeSubtasks = 0;

        for (Integer subtaskId : epic.getIdListOfSubtask()) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask == null) {
                continue;
            }

            activeSubtasks++;
            if (subtask.getStatus() == TaskStatus.DONE) {
                doneCount++;
            } else if (subtask.getStatus() == TaskStatus.NEW) {
                newCount++;
            }
        }

        if (activeSubtasks == 0 || newCount == activeSubtasks) {
            epic.setStatus(TaskStatus.NEW);
        } else if (doneCount == activeSubtasks) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    private int generateId() {
        return nextId++;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}