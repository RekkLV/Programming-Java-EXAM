package exam;

import java.util.*;
import java.io.*;


public class TaskManager {
	private ArrayList<Task> tasks;
	private int nextId = 1;

	public TaskManager() {
		this.tasks = new ArrayList<>();
	}
	
	public void addTask(String title, Priority priority) {
	    tasks.add(new Task(nextId++, title, priority));
	}
	
	public boolean removeTaskById(int id) { // dzēst uzdevumu pēc ID
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
	
	public boolean markTaskCompleteById(int id) { // atzīmēt uzdevumu kā izpildītu pēc ID
		for (int i = 0; i<tasks.size(); i++) {
			if (tasks.get(i).getId() == id) {
				tasks.get(i).markCompleted();
				return true;
			}
		}
		return false;
	}
	
	public boolean editTaskById(int id, String newTitle, Priority newPriority) { // rediget uzdevumu pēc ID
		for (int i = 0; i<tasks.size(); i++) {
			if (tasks.get(i).getId() == id) {
				tasks.get(i).setTitle(newTitle);
				tasks.get(i).setPriority(newPriority);
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Task> filterByStatus(TaskStatus status) { // filtrēt uzdevumus pēc statusa
		ArrayList<Task> result = new ArrayList<>();
		for (int i = 0; i<tasks.size(); i++) {
			if(tasks.get(i).getStatus() == status) {
				result.add(tasks.get(i));
			}
		}
		return result;
	}
	
	public ArrayList<Task> getAllTasks() {
	    return tasks;
	}
	
	public void saveToFile(String filename) {
	    try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
	        for (Task t : tasks) {
	            writer.println(
	                t.getId() + ";" +
	                t.getTitle() + ";" +
	                t.getStatus() + ";" +
	                t.getPriority()
	            );
	        }
	    } catch (IOException e) {
	        System.out.println("Error saving file.");
	    }
	}
	
	public void loadFromFile(String filename) {
	    tasks.clear();
	    nextId = 1;

	    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            String[] parts = line.split(";");

	            int id = Integer.parseInt(parts[0]);
	            String title = parts[1];
	            TaskStatus status = TaskStatus.valueOf(parts[2]);
	            Priority priority = Priority.valueOf(parts[3]);

	            Task task = new Task(id, title, priority);
	            if (status == TaskStatus.COMPLETED) {
	                task.markCompleted();
	            }

	            tasks.add(task);
	            nextId = Math.max(nextId, id + 1);
	        }
	    } catch (IOException e) {
	        System.out.println("Error loading file.");
	    }
	}

	
}
