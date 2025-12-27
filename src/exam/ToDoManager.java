package exam;

import java.util.ArrayList;

public class ToDoManager {

	public static void main(String[] args) {
		TaskManager manager = new TaskManager();
		manager.addTask(new Task(1, "Study", Priority.HIGH));
		manager.addTask(new Task(2, "Sleep", Priority.HIGH));
		manager.addTask(new Task(3, "Shop", Priority.LOW));
		manager.printAllTasks();
		manager.markTaskCompleteById(2);
		System.out.println("----------");
		
		ArrayList<Task> activeTasks = manager.filterByStatus(TaskStatus.ACTIVE); // tikai aktīvos uzdevumus
		for (Task t : activeTasks) {
		    System.out.println(t);
		}



	}

}
