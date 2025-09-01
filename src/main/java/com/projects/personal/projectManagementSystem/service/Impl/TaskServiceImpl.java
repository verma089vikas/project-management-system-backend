package com.projects.personal.projectManagementSystem.service.Impl;

import com.projects.personal.projectManagementSystem.dto.TaskRequestDTO;
import com.projects.personal.projectManagementSystem.dto.TaskResponseDTO;
import com.projects.personal.projectManagementSystem.entity.Project;
import com.projects.personal.projectManagementSystem.entity.Task;
import com.projects.personal.projectManagementSystem.entity.TaskDependency;
import com.projects.personal.projectManagementSystem.entity.User;
import com.projects.personal.projectManagementSystem.enums.TaskStatus;
import com.projects.personal.projectManagementSystem.exception.ResourceNotFoundException;
import com.projects.personal.projectManagementSystem.repository.ProjectRepository;
import com.projects.personal.projectManagementSystem.repository.TaskRepository;
import com.projects.personal.projectManagementSystem.repository.TaskDependencyRepository;
import com.projects.personal.projectManagementSystem.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

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
    public List<TaskResponseDTO> getTasksByProjectId(Long projectId) {
        List<Task> tasks = taskRepository.findByProjectId(projectId);
        if (tasks.isEmpty()) {
            throw new EntityNotFoundException("No tasks found for project ID: " + projectId);
        }
         return tasks.stream()
                .map(this::mapToResponse)
                .toList();

    }



    @Override
    @Transactional
    public TaskResponseDTO createTask(TaskRequestDTO request) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + request.getProjectId()));

        User assignee = userRepository.findById(request.getAssigneeId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + request.getAssigneeId()));

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .priority(request.getPriority())
                .estimatedHours(request.getEstimatedHours())
                .project(project)
                .assignee(assignee)
                .build();

        Task savedTask = taskRepository.save(task);
        projectService.updateProjectCompletion(project.getId());
        return mapToResponse(savedTask);
    }

    @Override
    @Transactional
    public TaskResponseDTO updateTask(Long taskId, TaskRequestDTO request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with ID: " + taskId));

        if (request.getProjectId() != null) {
            Project project = projectRepository.findById(request.getProjectId())
                    .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + request.getProjectId()));
            task.setProject(project);
        }

        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + request.getAssigneeId()));
            task.setAssignee(assignee);
        }

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setEstimatedHours(request.getEstimatedHours());

        Task updatedTask = taskRepository.save(task);
        return mapToResponse(updatedTask);
    }

    private TaskResponseDTO mapToResponse(Task task) {
        return TaskResponseDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .estimatedHours(task.getEstimatedHours())
                .createdAt(task.getCreatedAt())
                .projectId(task.getProject().getId())
                .projectName(task.getProject().getName())
                .assigneeId(task.getAssignee().getId())
                .assigneeName(task.getAssignee().getName())
                .build();
    }



}
