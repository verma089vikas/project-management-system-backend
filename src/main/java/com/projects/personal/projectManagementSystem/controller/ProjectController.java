package com.projects.personal.projectManagementSystem.controller;

import com.projects.personal.projectManagementSystem.dto.ProjectRequestDTO;
import com.projects.personal.projectManagementSystem.dto.ProjectResponseDTO;
import com.projects.personal.projectManagementSystem.entity.Project;
import com.projects.personal.projectManagementSystem.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponseDTO> create(@RequestBody @Valid  ProjectRequestDTO request) {
        return ResponseEntity.ok(projectService.createProject(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getAll() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> update(@PathVariable Long id, @RequestBody @Valid ProjectRequestDTO request) {
        return ResponseEntity.ok(projectService.updateProject(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Project>> getProjectsByUser(@PathVariable Long userId) {
        List<Project> projects = projectService.getProjectsByUserId(userId);
        return ResponseEntity.ok(projects);
    }
}
