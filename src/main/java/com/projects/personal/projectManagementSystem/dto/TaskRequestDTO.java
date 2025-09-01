package com.projects.personal.projectManagementSystem.dto;

import com.projects.personal.projectManagementSystem.enums.TaskPriority;
import com.projects.personal.projectManagementSystem.enums.TaskStatus;
import lombok.Data;

@Data
public class TaskRequestDTO {
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private Double estimatedHours;
    private Long projectId;   // Foreign key
    private Long assigneeId;  // Foreign key
}
