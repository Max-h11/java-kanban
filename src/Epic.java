import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final ArrayList<Integer> idListOfSubtask;

    public Epic(String name, String description) {
        super(name, description);
        this.idListOfSubtask = new ArrayList<>();
    }

    public Epic(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
        this.idListOfSubtask = new ArrayList<>();
    }

    // КОНСТРУКТОР-КОПИРОВЩИК
    public Epic(Epic other) {
        super(other);
        this.idListOfSubtask = new ArrayList<>(other.idListOfSubtask);
    }

    public List<Integer> getIdListOfSubtask() {
        return idListOfSubtask;
    }

    public void addIdOfSubtask(int subtaskId) {
        idListOfSubtask.add(subtaskId);
    }

    public void deleteSubtaskId(int subtaskId) {
        idListOfSubtask.remove(Integer.valueOf(subtaskId));
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subtaskIds=" + idListOfSubtask +
                '}';
    }
}