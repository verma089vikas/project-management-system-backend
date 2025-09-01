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
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found: " + taskId));
        Task dependsOnTask = taskRepository.findById(dependsOnTaskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found: " + dependsOnTaskId));

        // Check for cycle before saving
        if (isCyclePresent(taskId, dependsOnTaskId)) {
            throw new IllegalArgumentException("Circular dependency detected! Cannot create dependency.");
        }

        TaskDependency dependency = TaskDependency.builder()
                .task(task)
                .dependsOnTask(dependsOnTask)
                .build();

        return taskDependencyRepository.save(dependency);
    }

    private boolean isCyclePresent(Long taskId, Long dependsOnTaskId) {
        return dfsCheck(dependsOnTaskId, taskId);
    }

    private boolean dfsCheck(Long currentTaskId, Long targetTaskId) {
        if (currentTaskId.equals(targetTaskId)) {
            return true; // cycle found
        }

        List<Long> dependencies = taskDependencyRepository.findDependenciesByTaskId(currentTaskId);
        for (Long depId : dependencies) {
            if (dfsCheck(depId, targetTaskId)) {
                return true;
            }
        }
        return false;
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
