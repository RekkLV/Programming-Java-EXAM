package exam;

public class ToDoManager {

	public static void main(String[] args) {
		TaskManager manager = new TaskManager();
		
		manager.addTask(new Task(1, "Study", Priority.HIGH));
		manager.addTask(new Task(2, "Buy stuff", Priority.LOW));
		manager.addTask(new Task(3, "Sleep", Priority.HIGH));
		
		manager.printAllTasks();
		
		boolean removed = manager.removeTaskById(2);
		System.out.println("Removed: " + removed);
		
		manager.printAllTasks();

	}

}
