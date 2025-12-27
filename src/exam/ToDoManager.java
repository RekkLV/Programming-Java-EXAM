package exam;

public class ToDoManager {

	public static void main(String[] args) {
		TaskManager manager = new TaskManager();
		manager.addTask(new Task(1, "Study", Priority.HIGH));
		manager.printAllTasks();
		manager.editTaskById(1, "Study math", Priority.LOW);
		manager.printAllTasks();



	}

}
