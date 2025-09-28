public class Main {
    public static void main(String[] args) {
        TaskManager manager = new InMemoryTaskManager();

        Task task1 = manager.createTask(new Task("Посмотреть погоду", "Для выезда за город"));
        Task task2 = manager.createTask(new Task("Разгрузить подвал", "Чтобы освободить место"));

        Epic epic1 = manager.createEpic(new Epic("Переезд", "Организовать переезд в новую квартиру"));
        Subtask subtask1 = manager.createSubtask(new Subtask("Собрать коробки", "Собрать вещи в коробки", epic1.getId()));
        Subtask subtask2 = manager.createSubtask(new Subtask("Разобрать мебель", "Разобрать старую мебель", epic1.getId()));

        Epic epic2 = manager.createEpic(new Epic("Отпуск", "Спланировать летний отпуск"));
        Subtask subtask3 = manager.createSubtask(new Subtask("Купить билеты", "Билеты на самолет", epic2.getId()));

        System.out.println("--- Начальное состояние ---");
        System.out.println("Все задачи: " + manager.getAllTasks());
        System.out.println("Все эпики: " + manager.getAllEpics());
        System.out.println("Все подзадачи: " + manager.getAllSubtasks());

        System.out.println("\n--- Обновление статусов ---");
        System.out.println("Статус эпика 'Переезд' изначально: " + manager.getEpicById(epic1.getId()).getStatus());
        subtask1.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask1);
        System.out.println("Статус эпика 'Переезд' после завершения одной подзадачи: " + manager.getEpicById(epic1.getId()).getStatus());

        subtask2.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask2);
        System.out.println("Статус эпика 'Переезд' после завершения всех подзадач: " + manager.getEpicById(epic1.getId()).getStatus());

        System.out.println("\n--- Удаление задач ---");
        manager.deleteTaskById(task2.getId());
        manager.deleteEpicById(epic2.getId());
        System.out.println("Все задачи после удаления: " + manager.getAllTasks());
        System.out.println("Все эпики после удаления: " + manager.getAllEpics());
        System.out.println("Все подзадачи после удаления эпика 'Отпуск': " + manager.getAllSubtasks());
    }
}