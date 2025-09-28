import java.util.List;

public class Main {
    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Epic epic : manager.getAllEpics()) {
            System.out.println(epic);

            for (Subtask subtask : manager.getEpicSubtasks(epic.getId())) {
                System.out.println("--> " + subtask);
            }
        }
        System.out.println("Подзадачи (отдельно):");
        for (Subtask subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("\n--- ИСТОРИЯ ПРОСМОТРОВ ---");
        List<Task> history = manager.getHistory();
        if (history.isEmpty()) {
            System.out.println("История пуста.");
        } else {
            for (Task task : history) {
                System.out.println("ID=" + task.getId() + ", " + task.getClass().getSimpleName() + ": " + task.getName());
            }
        }
        System.out.println("--------------------------");
    }

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        Task task1 = manager.createTask(new Task("Посмотреть погоду", "Для выезда"));
        Task task2 = manager.createTask(new Task("Почистить почту", "Удалить спам"));

        Epic epic1 = manager.createEpic(new Epic("Переезд", "Организовать переезд"));
        Subtask subtask1 = manager.createSubtask(new Subtask("Собрать коробки", "Собрать вещи", epic1.getId()));
        Subtask subtask2 = manager.createSubtask(new Subtask("Заказать машину", "Грузовое такси", epic1.getId()));

        Epic epic2 = manager.createEpic(new Epic("Отпуск", "Спланировать отпуск"));
        Subtask subtask3 = manager.createSubtask(new Subtask("Купить билеты", "Билеты на самолет", epic2.getId()));

        System.out.println("--- НАЧАЛЬНОЕ СОСТОЯНИЕ ПРОЕКТА ---");
        printAllTasks(manager);

        System.out.println("\n--- ФОРМИРОВАНИЕ ИСТОРИИ ---");
        manager.getTaskById(task1.getId());
        manager.getEpicById(epic1.getId());
        manager.getSubtaskById(subtask2.getId());
        manager.getTaskById(task2.getId());
        manager.getEpicById(epic2.getId());
        manager.getSubtaskById(subtask1.getId());

        System.out.println("\n--- ИСТОРИЯ ПОСЛЕ 6 ПРОСМОТРОВ ---");
        printAllTasks(manager);

        System.out.println("\n--- ПРОВЕРКА ОБНОВЛЕНИЯ СТАТУСА ---");
        System.out.println("Статус Epic1 до: " + manager.getEpicById(epic1.getId()).getStatus());
        subtask1.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask1);
        System.out.println("Статус Epic1 после: " + manager.getEpicById(epic1.getId()).getStatus());
    }
}