package com.projects.personal.projectManagementSystem.service.Impl;

import com.projects.personal.projectManagementSystem.entity.Task;
import com.projects.personal.projectManagementSystem.entity.TaskDependency;
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
    public Task updateTask(Long id, Task taskDetails) {
        Task existingTask = getTaskById(id);
        existingTask.setTitle(taskDetails.getTitle());
        existingTask.setDescription(taskDetails.getDescription());
        existingTask.setStatus(taskDetails.getStatus());
        existingTask.setPriority(taskDetails.getPriority());
        existingTask.setEstimatedHours(taskDetails.getEstimatedHours());
        existingTask.setAssignee(taskDetails.getAssignee());
        existingTask.setProject(taskDetails.getProject());
        return taskRepository.save(existingTask);
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        Task task = getTaskById(id);
        taskDependencyRepository.deleteAll(taskDependencyRepository.findByTaskId(id));
        taskRepository.delete(task);
    }

    @Override
    public List<TaskDependency> getDependencies(Long taskId) {
        return taskDependencyRepository.findByTaskId(taskId);
    }
}
