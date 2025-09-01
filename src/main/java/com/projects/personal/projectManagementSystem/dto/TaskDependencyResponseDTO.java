package com.projects.personal.projectManagementSystem.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TaskDependencyResponseDTO {
    private Long id;
    private LocalDateTime createdAt;
    private Long taskId;
    private String taskTitle;
    private Long dependsOnTaskId;
    private String dependsOnTaskTitle;
}
