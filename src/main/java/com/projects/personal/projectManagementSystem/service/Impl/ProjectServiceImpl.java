package com.projects.personal.projectManagementSystem.service.Impl;

import com.projects.personal.projectManagementSystem.dto.ProjectRequestDTO;
import com.projects.personal.projectManagementSystem.dto.ProjectResponseDTO;
import com.projects.personal.projectManagementSystem.entity.Project;
import com.projects.personal.projectManagementSystem.entity.User;
import com.projects.personal.projectManagementSystem.exception.ResourceNotFoundException;
import com.projects.personal.projectManagementSystem.repository.ProjectRepository;
import com.projects.personal.projectManagementSystem.repository.UserRepository;
import com.projects.personal.projectManagementSystem.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;


    @Override
    public ProjectResponseDTO createProject(ProjectRequestDTO request) {
        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .deadline(request.getDeadline())
                .status(request.getStatus())
                .owner(owner)
                .build();

        return mapToResponse(projectRepository.save(project));
    }

    @Override
    public ProjectResponseDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        return mapToResponse(project);
    }

    @Override
    public List<ProjectResponseDTO> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectResponseDTO updateProject(Long id, ProjectRequestDTO request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setDeadline(request.getDeadline());
        project.setStatus(request.getStatus());
        project.setOwner(owner);

        return mapToResponse(projectRepository.save(project));
    }

    @Override
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        projectRepository.delete(project);
    }


    @Override
    public List<Project> getProjectsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User with ID " + userId + " not found");
        }
        return projectRepository.findByOwnerId(userId);
    }


    private ProjectResponseDTO mapToResponse(Project project) {
        return ProjectResponseDTO.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .deadline(project.getDeadline())
                .status(project.getStatus())
                .ownerId(project.getOwner().getId())
                .ownerName(project.getOwner().getName())
                .build();
    }
}
