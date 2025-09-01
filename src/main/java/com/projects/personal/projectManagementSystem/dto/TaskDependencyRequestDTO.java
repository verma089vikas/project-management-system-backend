package com.projects.personal.projectManagementSystem.dto;

import lombok.Data;

@Data
public class TaskDependencyRequestDTO {
    private Long taskId;
    private Long dependsOnTaskId;
}
