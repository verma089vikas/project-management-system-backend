package com.projects.personal.projectManagementSystem.controller;

import com.projects.personal.projectManagementSystem.dto.TaskRequestDTO;
import com.projects.personal.projectManagementSystem.dto.TaskResponseDTO;
import com.projects.personal.projectManagementSystem.entity.Task;
import com.projects.personal.projectManagementSystem.entity.TaskDependency;
import com.projects.personal.projectManagementSystem.enums.TaskStatus;
import com.projects.personal.projectManagementSystem.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskRequestDTO request) {
        return ResponseEntity.ok(taskService.createTask(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @RequestBody TaskRequestDTO request) {
        return ResponseEntity.ok(taskService.updateTask(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/dependencies")
    public ResponseEntity<List<TaskDependency>> getDependencies(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getDependencies(id));
    }

    @PutMapping("/{taskId}/status")
    public ResponseEntity<Task> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestParam TaskStatus status
    ) {
        Task updatedTask = taskService.updateTaskStatus(taskId, status);
        return ResponseEntity.ok(updatedTask);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Task>> getTasksByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(taskService.getTasksByProjectId(projectId));
    }
}
