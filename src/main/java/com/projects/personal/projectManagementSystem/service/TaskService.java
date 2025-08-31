package com.projects.personal.projectManagementSystem.service;

import com.projects.personal.projectManagementSystem.entity.Task;
import com.projects.personal.projectManagementSystem.entity.TaskDependency;
import java.util.List;

public interface TaskService {
    Task createTask(Task task, List<Long> dependencyIds);
    List<Task> getAllTasks();
    Task getTaskById(Long id);
    Task updateTask(Long id, Task taskDetails);
    void deleteTask(Long id);
    List<TaskDependency> getDependencies(Long taskId);
}
