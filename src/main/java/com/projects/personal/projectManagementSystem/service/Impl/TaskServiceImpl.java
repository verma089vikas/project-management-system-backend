package com.projects.personal.projectManagementSystem.service.Impl;

import com.projects.personal.projectManagementSystem.entity.Task;
import com.projects.personal.projectManagementSystem.entity.TaskDependency;
import com.projects.personal.projectManagementSystem.enums.TaskStatus;
import com.projects.personal.projectManagementSystem.exception.ResourceNotFoundException;
import com.projects.personal.projectManagementSystem.repository.TaskRepository;
import com.projects.personal.projectManagementSystem.repository.TaskDependencyRepository;
import com.projects.personal.projectManagementSystem.service.TaskService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskDependencyRepository taskDependencyRepository;
    private final ProjectServiceImpl projectService;

    @Override
    @Transactional
    public Task createTask(Task task, List<Long> dependencyIds) {
        Task savedTask = taskRepository.save(task);

        if (dependencyIds != null && !dependencyIds.isEmpty()) {
            for (Long depId : dependencyIds) {
                Task dependsOnTask = taskRepository.findById(depId)
                        .orElseThrow(() -> new EntityNotFoundException("Dependency Task not found with ID: " + depId));

                TaskDependency dependency = TaskDependency.builder()
                        .task(savedTask)
                        .dependsOnTask(dependsOnTask)
                        .build();
                taskDependencyRepository.save(dependency);
            }
        }

        projectService.updateProjectCompletion(task.getProject().getId());

        return savedTask;
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with ID: " + id));
    }

    @Override
    @Transactional
    public Task updateTask(Long id, Task updatedTask) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with ID: " + id));

        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setStatus(updatedTask.getStatus());
        existingTask.setPriority(updatedTask.getPriority());
        existingTask.setEstimatedHours(updatedTask.getEstimatedHours());

        Task savedTask = taskRepository.save(existingTask);

        // Recalculate completion percentage after status update
        projectService.updateProjectCompletion(savedTask.getProject().getId());

        return savedTask;
    }


    @Override
    @Transactional
    public void deleteTask(Long id) {
        Task task = getTaskById(id);
        Long projectId = task.getProject().getId(); // Get project ID before deleting

        taskDependencyRepository.deleteAll(taskDependencyRepository.findByTaskId(id));
        taskDependencyRepository.deleteAll(taskDependencyRepository.findByDependsOnTaskId(id));
        taskRepository.delete(task);
        projectService.updateProjectCompletion(projectId);
    }


    @Override
    public List<TaskDependency> getDependencies(Long taskId) {
        return taskDependencyRepository.findByTaskId(taskId);
    }

    @Override
    @Transactional
    public Task updateTaskStatus(Long taskId, TaskStatus newStatus) {
        // Fetch task by ID
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));

        // Update status
        task.setStatus(newStatus);
        Task updatedTask = taskRepository.save(task);

        // Recalculate completion percentage if task is linked to a project
        if (task.getProject() != null) {
            projectService.updateProjectCompletion(task.getProject().getId());
        }

        return updatedTask;
    }

    @Override
    public List<Task> getTasksByProjectId(Long projectId) {
        List<Task> tasks = taskRepository.findByProjectId(projectId);
        if (tasks.isEmpty()) {
            throw new EntityNotFoundException("No tasks found for project ID: " + projectId);
        }
        return tasks;
    }


}
