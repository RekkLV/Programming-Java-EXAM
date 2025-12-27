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
	
	public boolean markTaskCompleteById(int id) {
		for (int i = 0; i<tasks.size(); i++) {
			if (tasks.get(i).getId() == id) {
				tasks.get(i).markCompleted();
				return true;
			}
		}
		return false;
	}
	
	public boolean editTaskById(int id, String newTitle, Priority newPriority) {
		for (int i = 0; i<tasks.size(); i++) {
			if (tasks.get(i).getId() == id) {
				tasks.get(i).setTitle(newTitle);
				tasks.get(i).setPriority(newPriority);
				return true;
			}
		}
		return false;
	}
	
}
