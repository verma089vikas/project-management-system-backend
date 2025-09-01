package com.projects.personal.projectManagementSystem.service;

import com.projects.personal.projectManagementSystem.dto.TaskRequestDTO;
import com.projects.personal.projectManagementSystem.dto.TaskResponseDTO;
import com.projects.personal.projectManagementSystem.entity.Task;
import com.projects.personal.projectManagementSystem.entity.TaskDependency;
import com.projects.personal.projectManagementSystem.enums.TaskStatus;

import java.util.List;

public interface TaskService {
    TaskResponseDTO createTask(TaskRequestDTO request);
    TaskResponseDTO updateTask(Long taskId, TaskRequestDTO request);
    List<Task> getAllTasks();
    Task getTaskById(Long id);
    void deleteTask(Long id);
    List<TaskDependency> getDependencies(Long taskId);
    TaskResponseDTO updateTaskStatus(Long taskId, TaskStatus newStatus);
    List<TaskResponseDTO> getTasksByProjectId(Long projectId);

}
