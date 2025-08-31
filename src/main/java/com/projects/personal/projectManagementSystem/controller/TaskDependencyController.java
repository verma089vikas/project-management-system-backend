package com.projects.personal.projectManagementSystem.controller;

import com.projects.personal.projectManagementSystem.entity.TaskDependency;
import com.projects.personal.projectManagementSystem.service.TaskDependencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/task-dependencies")
@RequiredArgsConstructor
public class TaskDependencyController {

    private final TaskDependencyService taskDependencyService;

    // Create dependency
    @PostMapping
    public ResponseEntity<TaskDependency> createDependency(
            @RequestParam Long taskId,
            @RequestParam Long dependsOnTaskId) {
        TaskDependency dependency = taskDependencyService.createDependency(taskId, dependsOnTaskId);
        return ResponseEntity.ok(dependency);
    }

    // Get dependencies of a task
    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<TaskDependency>> getDependencies(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskDependencyService.getDependenciesByTask(taskId));
    }

    // Get tasks that depend on a given task
    @GetMapping("/depends-on/{taskId}")
    public ResponseEntity<List<TaskDependency>> getDependents(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskDependencyService.getDependents(taskId));
    }

    // Delete dependency
    @DeleteMapping("/{dependencyId}")
    public ResponseEntity<Void> deleteDependency(@PathVariable Long dependencyId) {
        taskDependencyService.deleteDependency(dependencyId);
        return ResponseEntity.noContent().build();
    }
}
