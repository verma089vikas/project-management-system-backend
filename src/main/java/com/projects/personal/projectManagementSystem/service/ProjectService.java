package com.projects.personal.projectManagementSystem.service;

import com.projects.personal.projectManagementSystem.dto.ProjectRequestDTO;
import com.projects.personal.projectManagementSystem.dto.ProjectResponseDTO;
import com.projects.personal.projectManagementSystem.entity.Project;

import java.util.List;

public interface ProjectService {

    ProjectResponseDTO createProject(ProjectRequestDTO request);
    ProjectResponseDTO getProjectById(Long id);
    List<ProjectResponseDTO> getAllProjects();
    ProjectResponseDTO updateProject(Long id, ProjectRequestDTO request);
    void deleteProject(Long id);
    List<Project> getProjectsByUserId(Long userId);
}
