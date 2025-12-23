package exam;

import java.util.*;

public class TaskManager {
	private ArrayList<Task> tasks;
	
	public TaskManager() {
		this.tasks = new ArrayList<>();
	}
	
	public void addTask(Task task) {
		tasks.add(task);
	}
	
	public boolean removeTaskById(int id) {
		for (int i = 0; i<tasks.size(); i++) {
			if (tasks.get(i).getId() == id) {
				tasks.remove(i);
				return true;
			}

		}
		return false;
	}
	
	public void printAllTasks() {
		if (tasks.isEmpty()) {
			System.out.println("You don't have any tasks!");
		}
		else {
			for (int i = 0; i<tasks.size(); i++) {
				System.out.println(tasks.get(i));
			}
		}
	}
}
