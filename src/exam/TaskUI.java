package exam;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;

public class TaskUI {
	
    private TaskManager manager;
    private String currentFile = null;


    public TaskUI(TaskManager manager) {
        this.manager = manager;
    }
    private String buildMenuText() {
        String fileInfo = (currentFile == null)
                ? "Editing file: None\n\n"
                : "Editing file: " + currentFile + "\n\n";

        return fileInfo +
        		"To-Do Manager\n"+
               "1. Add Task\n" +
               "2. List Tasks\n" +
               "3. Edit Task\n" +
               "4. Remove Task\n" +
               "5. Mark Task as Completed\n" +
               "6. Filter Tasks\n" +
               "7. Save File\n" +
               "8. Load/Create File\n" +
               "9. Exit";
    }
    
    public void start() {
    	chooseStartupFile();
    	
        while (true) {
        	String choice = JOptionPane.showInputDialog(buildMenuText());

            if (choice == null || choice.equals("9")) break;

            switch (choice) {
                case "1": addTask(); break;
                case "2": listTasks(); break;
                case "3": editTask(); break;
                case "4": removeTask(); break;
                case "5": markCompleted(); break;
                case "6": filterTasks(); break;
                case "7": saveTasks(); break;
                case "8": loadTasks(); break;

                default:
                    JOptionPane.showMessageDialog(null, "Invalid option!");
            }
        }
    }

    private Task chooseTaskByTitle(String message, ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No tasks available!");
            return null;
        }

        String[] titles = new String[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            titles[i] = tasks.get(i).getId() + ": " + tasks.get(i).getTitle();
        }

        String selected = (String) JOptionPane.showInputDialog(
                null,
                message,
                "Select Task",
                JOptionPane.QUESTION_MESSAGE,
                null,
                titles,
                titles[0]
        );

        if (selected == null) return null;

        int selectedId = Integer.parseInt(selected.split(":")[0]);

        for (Task t : tasks) {
            if (t.getId() == selectedId) {
                return t;
            }
        }
        return null;
    }


    private void addTask() {
        String title = JOptionPane.showInputDialog("Enter task title:");
        if (title == null || title.isBlank()) {
            JOptionPane.showMessageDialog(null, "Title cannot be empty!");
            return;
        }

        String[] priorities = {"LOW", "MEDIUM", "HIGH"};
        String priorityStr = (String) JOptionPane.showInputDialog(
                null,
                "Select priority:",
                "Priority",
                JOptionPane.QUESTION_MESSAGE,
                null,
                priorities,
                priorities[0]
        );

        if (priorityStr == null) return;

        manager.addTask(title, Priority.valueOf(priorityStr));
        autoSave();
        JOptionPane.showMessageDialog(null, "Task added!");
       
        
    }

    private void listTasks() {
        ArrayList<Task> tasks = manager.getAllTasks();

        if (tasks.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No tasks available!");
            return;
        }

        StringBuilder sb = new StringBuilder("All Tasks:\n\n");
        for (Task t : tasks) {
            sb.append(t).append("\n");
        }

        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private void editTask() {
        Task task = chooseTaskByTitle("Select task to edit:", manager.getAllTasks());
        if (task == null) return;

        String newTitle = JOptionPane.showInputDialog("New title:", task.getTitle());
        if (newTitle != null && !newTitle.isBlank()) {
            task.setTitle(newTitle);
        }

        String[] priorities = {"LOW", "MEDIUM", "HIGH"};
        String newPriority = (String) JOptionPane.showInputDialog(
                null,
                "Select new priority:",
                "Priority",
                JOptionPane.QUESTION_MESSAGE,
                null,
                priorities,
                task.getPriority().name()
        );

        if (newPriority != null) {
            task.setPriority(Priority.valueOf(newPriority));
            autoSave();
        }

        JOptionPane.showMessageDialog(null, "Task updated!");
    }

    private void removeTask() {
        Task task = chooseTaskByTitle("Select task to remove:", manager.getAllTasks());
        if (task == null) return;

        manager.removeTaskById(task.getId());
        autoSave();
        JOptionPane.showMessageDialog(null, "Task removed!");
    }

    private void markCompleted() {
        ArrayList<Task> activeTasks = manager.filterByStatus(TaskStatus.ACTIVE);
        Task task = chooseTaskByTitle("Select task to mark as completed:", activeTasks);
        if (task == null) return;

        task.markCompleted();
        autoSave();
        JOptionPane.showMessageDialog(null, "Task marked as completed!");
    }

    private void filterTasks() {
        String[] options = {"ACTIVE", "COMPLETED"};
        String statusStr = (String) JOptionPane.showInputDialog(
                null,
                "Select status:",
                "Filter Tasks",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (statusStr == null) return;

        TaskStatus status = TaskStatus.valueOf(statusStr);
        ArrayList<Task> filtered = manager.filterByStatus(status);

        if (filtered.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No " + status + " tasks found.");
            return;
        }

        StringBuilder sb = new StringBuilder(status + " Tasks:\n\n");
        for (Task t : filtered) {
            sb.append(t).append("\n");
        }

        JOptionPane.showMessageDialog(null, sb.toString());
    }
    private void saveTasks() {
        String filename = JOptionPane.showInputDialog(
                "Enter filename to save:",
                currentFile == null ? "" : currentFile
        );

        if (filename == null || filename.isBlank()) return;
        
        if (!filename.toLowerCase().endsWith(".txt")) {
            filename += ".txt";
        }
        
        manager.saveToFile(filename);
        currentFile = filename;   

        JOptionPane.showMessageDialog(null, "Tasks saved successfully!");
    }
    
    private void loadTasks() {
        String filename = JOptionPane.showInputDialog("Enter filename to load or create:");
        if (filename == null || filename.isBlank()) return;

     
        if (!filename.toLowerCase().endsWith(".txt")) {
            filename += ".txt";
        }

        File file = new File(filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
                JOptionPane.showMessageDialog(null, "File did not exist, created new file: " + filename);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error creating new file: " + filename);
                return;
            }
        }

        manager.loadFromFile(filename);
        currentFile = filename;

        JOptionPane.showMessageDialog(null, "File loaded successfully!");
    }


    private void chooseStartupFile() {
        String filename = JOptionPane.showInputDialog(
                "Enter file name to load or create:"
        );

        if (filename == null || filename.isBlank()) {
            JOptionPane.showMessageDialog(null, "No file selected. Application will exit.");
            System.exit(0);
        }


        if (!filename.toLowerCase().endsWith(".txt")) {
            filename += ".txt";
        }

        File file = new File(filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
                JOptionPane.showMessageDialog(null, "Created new file: " + filename);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Failed to create file.");
                System.exit(0);
            }
        }

        manager.loadFromFile(filename);
        currentFile = filename;
    }

    private void autoSave() {
        if (currentFile != null) {
            manager.saveToFile(currentFile);
        }
    }	

}
