package exam;

import java.util.*;
import java.io.*;
import java.time.LocalDateTime;


public class TaskManager {
	private ArrayList<Task> tasks;
	private int nextId = 1;

	public TaskManager() {
		this.tasks = new ArrayList<>();
	}
	
	public void addTask(String title, Priority priority) {
	    tasks.add(new Task(nextId++, title, priority));
	}
	
	public void addTask(String title, Priority priority, LocalDateTime deadline) {
		tasks.add(new Task(nextId++, title, priority, deadline)); // overload
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
	        	String deadlineStr = (t.getDeadline() == null) ? "" : t.getDeadline().toString(); // different kind of "if" statement, means: if condition is true, use this; otherwise, use that. condition ? valueIfTrue : valueIfFals
	        	
	            writer.println(
	                t.getId() + ";" +
	                t.getTitle() + ";" +
	                t.getStatus() + ";" +
	                t.getPriority() + ";" +
	                deadlineStr
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
	            
	            LocalDateTime deadline = null;
	            if (parts.length >= 5 && !parts[4].isBlank()) {
	            	deadline = LocalDateTime.parse(parts[4]);
	            }


	            Task task = new Task(id, title, priority, deadline);
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
