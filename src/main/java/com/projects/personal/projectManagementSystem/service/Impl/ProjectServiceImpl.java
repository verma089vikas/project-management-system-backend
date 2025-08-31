package com.projects.personal.projectManagementSystem.service.Impl;

import com.projects.personal.projectManagementSystem.dto.ProjectRequestDTO;
import com.projects.personal.projectManagementSystem.dto.ProjectResponseDTO;
import com.projects.personal.projectManagementSystem.entity.Project;
import com.projects.personal.projectManagementSystem.entity.Task;
import com.projects.personal.projectManagementSystem.entity.User;
import com.projects.personal.projectManagementSystem.enums.TaskStatus;
import com.projects.personal.projectManagementSystem.exception.ResourceNotFoundException;
import com.projects.personal.projectManagementSystem.repository.ProjectRepository;
import com.projects.personal.projectManagementSystem.repository.TaskRepository;
import com.projects.personal.projectManagementSystem.repository.UserRepository;
import com.projects.personal.projectManagementSystem.service.ProjectService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;



    public Project getProjectEntity(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + projectId));
    }

    @Transactional
    public void updateProjectCompletion(Long projectId) {
        Project project = getProjectEntity(projectId);

        List<Task> tasks = taskRepository.findByProjectId(projectId);
        if (tasks.isEmpty()) {
            project.setCompletionPercentage(0.0);
        } else {
            long completed = tasks.stream()
                    .filter(t -> t.getStatus() == TaskStatus.DONE)
                    .count();
            double percentage = ((double) completed / tasks.size()) * 100;
            project.setCompletionPercentage(percentage);
        }
        projectRepository.save(project);
    }


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
                .completionPercentage(project.getCompletionPercentage())
                .build();
    }

}
