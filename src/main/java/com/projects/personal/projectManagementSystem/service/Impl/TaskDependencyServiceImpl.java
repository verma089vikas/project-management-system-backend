package com.projects.personal.projectManagementSystem.service.Impl;

import com.projects.personal.projectManagementSystem.entity.Task;
import com.projects.personal.projectManagementSystem.entity.TaskDependency;
import com.projects.personal.projectManagementSystem.repository.TaskDependencyRepository;
import com.projects.personal.projectManagementSystem.repository.TaskRepository;
import com.projects.personal.projectManagementSystem.service.TaskDependencyService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskDependencyServiceImpl implements TaskDependencyService {

    private final TaskDependencyRepository taskDependencyRepository;
    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public TaskDependency createDependency(Long taskId, Long dependsOnTaskId) {
        if (taskId.equals(dependsOnTaskId)) {
            throw new IllegalArgumentException("A task cannot depend on itself.");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with ID: " + taskId));

        Task dependsOnTask = taskRepository.findById(dependsOnTaskId)
                .orElseThrow(() -> new EntityNotFoundException("Dependent Task not found with ID: " + dependsOnTaskId));

        TaskDependency dependency = TaskDependency.builder()
                .task(task)
                .dependsOnTask(dependsOnTask)
                .build();

        return taskDependencyRepository.save(dependency);
    }

    @Override
    public List<TaskDependency> getDependenciesByTask(Long taskId) {
        return taskDependencyRepository.findByTaskId(taskId);
    }

    @Override
    public List<TaskDependency> getDependents(Long dependsOnTaskId) {
        return taskDependencyRepository.findByDependsOnTaskId(dependsOnTaskId);
    }

    @Override
    @Transactional
    public void deleteDependency(Long dependencyId) {
        if (!taskDependencyRepository.existsById(dependencyId)) {
            throw new EntityNotFoundException("Task Dependency not found with ID: " + dependencyId);
        }
        taskDependencyRepository.deleteById(dependencyId);
    }
}
