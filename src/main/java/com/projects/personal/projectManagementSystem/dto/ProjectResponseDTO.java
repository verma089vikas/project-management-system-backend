package com.projects.personal.projectManagementSystem.dto;

import com.projects.personal.projectManagementSystem.enums.ProjectStatus;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectResponseDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate deadline;
    private ProjectStatus status;
    private Long ownerId;
    private String ownerName;
    private Double completionPercentage; // Added field
}
