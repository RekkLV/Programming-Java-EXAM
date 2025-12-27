package exam;

public class Task {
	private int id;
	private String title;
	private TaskStatus status;
	private Priority priority;
	
	public Task(int id, String title, Priority priority) {
		this.id = id;
		this.title = title;
		this.priority = priority;
		this.status = TaskStatus.ACTIVE;
	}
	
	@Override
	public String toString() {
		return "Task " + id + ": " + title + " | Status: " + status + " | Priority: " + priority;
	}
	
	//getter
	public int getId() {
		return id;
	}
	
	public void markCompleted() {
		status = TaskStatus.COMPLETED;
	}
	
	public void setTitle(String newTitle) {
		if (newTitle != null && !newTitle.isBlank()) {
			title = newTitle;
		}
	}
	
	public void setPriority(Priority newPriority) {
		if (newPriority != null) {
			priority = newPriority;
		}
	}
	
	public TaskStatus getStatus() {
		return status;
	}
}
