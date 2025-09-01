package com.projects.personal.projectManagementSystem.dto;

import com.projects.personal.projectManagementSystem.enums.TaskPriority;
import com.projects.personal.projectManagementSystem.enums.TaskStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TaskResponseDTO {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private Double estimatedHours;
    private LocalDateTime createdAt;
    private Long projectId;
    private String projectName;
    private Long assigneeId;
    private String assigneeName;
}
