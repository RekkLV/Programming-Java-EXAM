package exam;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TaskUI {

    private TaskManager manager;
    private String currentFile = null;
    private JFrame frame;

    public TaskUI(TaskManager manager) {
        this.manager = manager;
    }

    public void start() {
        chooseStartupFile();
        SwingUtilities.invokeLater(this::createAndShowUI);
    }

    private void createAndShowUI() {
        frame = new JFrame("To-Do Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 450);
        frame.setLocationRelativeTo(null);
        
        ImageIcon icon = new ImageIcon(getClass().getResource("/icon.png"));
        frame.setIconImage(icon.getImage());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panel.add(createButton("Add Task", e -> addTask()));
        panel.add(createButton("List Tasks", e -> listTasks()));
        panel.add(createButton("Edit Task", e -> editTask()));
        panel.add(createButton("Remove Task", e -> removeTask()));
        panel.add(createButton("Mark Task as Completed", e -> markCompleted()));
        panel.add(createButton("Filter Tasks", e -> filterTasks()));
        panel.add(createButton("Save File", e -> saveTasks()));
        panel.add(createButton("Load/Create File", e -> loadTasks()));
        panel.add(createButton("Exit", e -> exitApp()));

        frame.add(panel);
        updateFrameTitle();
        frame.setVisible(true);
    }

    private JButton createButton(String text, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        return button;
    }


    private Task chooseTaskByTitle(String message, ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No tasks available!");
            return null;
        }

        String[] titles = new String[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            titles[i] = tasks.get(i).getId() + ": " + tasks.get(i).getTitle();
        }

        String selected = (String) JOptionPane.showInputDialog(
                frame,
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
        String title = JOptionPane.showInputDialog(frame, "Enter task title:");
        if (title == null || title.isBlank()) {
            JOptionPane.showMessageDialog(frame, "Title cannot be empty!");
            return;
        }

        String[] priorities = {"LOW", "MEDIUM", "HIGH"};
        String priorityStr = (String) JOptionPane.showInputDialog(
                frame,
                "Select priority:",
                "Priority",
                JOptionPane.QUESTION_MESSAGE,
                null,
                priorities,
                priorities[0]
        );

        if (priorityStr == null) return;

        String deadlineInput = JOptionPane.showInputDialog(frame, "Enter deadline (yyyy-MM-dd HH:mm) or leave empty");
        LocalDateTime deadline = null;

        if (deadlineInput !=null && !deadlineInput.isBlank()) { 
        	try {
        		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        		deadline = LocalDateTime.parse(deadlineInput, format);
        	}catch(Exception e) {
        		JOptionPane.showMessageDialog(null, "Invalid date format!");
        		return;
        	}

        }

        manager.addTask(title, Priority.valueOf(priorityStr), deadline);
        autoSave();
        JOptionPane.showMessageDialog(frame, "Task added!");
    }

    private void listTasks() {
        ArrayList<Task> tasks = manager.getAllTasks();
        
        if (tasks.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No tasks available!");
            return;
        }

        StringBuilder sb = new StringBuilder("All Tasks:\n\n");
        for (Task t : tasks) {
            sb.append(t).append("\n");
        }

        JOptionPane.showMessageDialog(frame, sb.toString());
    }

    private void editTask() {
        Task task = chooseTaskByTitle("Select task to edit:", manager.getAllTasks());
        if (task == null) return;

        String newTitle = JOptionPane.showInputDialog(frame,"New title:", task.getTitle());
        if (newTitle != null && !newTitle.isBlank()) {
            task.setTitle(newTitle);
        }

        String[] priorities = {"LOW", "MEDIUM", "HIGH"};
        String newPriority = (String) JOptionPane.showInputDialog(
                frame,
                "Select new priority:",
                "Priority",
                JOptionPane.QUESTION_MESSAGE,
                null,
                priorities,
                task.getPriority().name()
        );

        
        String currentDeadlineStr = (task.getDeadline() == null) ? "": task.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        
        String newDeadlineInput = JOptionPane.showInputDialog(frame,"New deadline (yyyy-MM-dd HH:mm), leave empty to remove deadline: ", currentDeadlineStr);
        
        if (newDeadlineInput !=null) {
        	if (newDeadlineInput.isBlank()) {
        		task.setDeadline(null);
        	}
        	else {
        		try {
        			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        			LocalDateTime newDeadline = LocalDateTime.parse(newDeadlineInput, format);
        			task.setDeadline(newDeadline);
        		}catch (Exception e) {
        			JOptionPane.showMessageDialog(null, "Invalid date format!");
        			return;
        		}
        	}
        }
        if (newPriority != null) {
            task.setPriority(Priority.valueOf(newPriority));
        }
        
        autoSave();
        JOptionPane.showMessageDialog(frame, "Task updated!");
    }

    private void removeTask() {
        Task task = chooseTaskByTitle("Select task to remove:", manager.getAllTasks());
        if (task == null) return;

        manager.removeTaskById(task.getId());
        autoSave();
        JOptionPane.showMessageDialog(frame, "Task removed!");
    }

    private void markCompleted() {
        ArrayList<Task> activeTasks = manager.filterByStatus(TaskStatus.ACTIVE);
        Task task = chooseTaskByTitle("Select task to mark as completed:", activeTasks);
        if (task == null) return;

        task.markCompleted();
        autoSave();
        JOptionPane.showMessageDialog(frame, "Task marked as completed!");
    }

    private void filterTasks() {
        String[] options = {"ACTIVE", "COMPLETED"};
        String statusStr = (String) JOptionPane.showInputDialog(
                frame,
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
            JOptionPane.showMessageDialog(frame, "No " + status + " tasks found.");
            return;
        }

        StringBuilder sb = new StringBuilder(status + " Tasks:\n\n");
        for (Task t : filtered) {
        	sb.append(t).append("\n");
        }
        JOptionPane.showMessageDialog(frame, sb.toString());
    }


    private void saveTasks() {
        String filename = JOptionPane.showInputDialog(frame,
        		"Enter filename to save:",
        		currentFile == null ? "" : currentFile
        );
        if (filename == null || filename.isBlank()) return;

        if (!filename.toLowerCase().endsWith(".txt")) {
        	filename += ".txt";
        }
        
        manager.saveToFile(filename);
        currentFile = filename;

        JOptionPane.showMessageDialog(frame, "Tasks saved successfully!");
        
        updateFrameTitle();
    }

    private void loadTasks() {
        String filename = JOptionPane.showInputDialog(frame, "Enter filename to load:");
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
                JOptionPane.showMessageDialog(frame, "Error creating new file: " + filename);
                return;
            }
        }
        manager.loadFromFile(filename);
        currentFile = filename;
        JOptionPane.showMessageDialog(frame, "File loaded successfully!");
        
        updateFrameTitle();
    }

    private void chooseStartupFile() {
        String filename = JOptionPane.showInputDialog(
        		"Enter file name to load or create:"
        );
        
        if (filename == null || filename.isBlank()) {
        	JOptionPane.showMessageDialog(frame, "No file selected. Application will exit.");
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
    private void updateFrameTitle() {
        if (frame != null) {
            String fileText = (currentFile == null) ? "No file" : currentFile;
            frame.setTitle("To-Do Manager - " + fileText);
        }
    }

    
    private void exitApp() {
        autoSave();
        frame.dispose();
        System.exit(0);
    }
}
