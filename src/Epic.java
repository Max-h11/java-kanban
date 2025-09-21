import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> idListOfSubtask;

    public Epic(String name, String description) {
        super(name, description);
        this.idListOfSubtask = new ArrayList<>();
    }

    public ArrayList<Integer> getIdListOfSubtask() {
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
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", subtaskIds=" + idListOfSubtask +
                '}';
    }
}
