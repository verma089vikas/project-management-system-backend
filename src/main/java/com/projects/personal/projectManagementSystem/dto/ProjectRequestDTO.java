package com.projects.personal.projectManagementSystem.dto;

import com.projects.personal.projectManagementSystem.enums.ProjectStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequestDTO {

    @NotBlank(message = "Project name is required")
    private String name;

    @Size(max = 1000, message = "Description can't exceed 1000 characters")
    private String description;

    @NotNull(message = "Deadline is required")
    @FutureOrPresent(message = "Deadline must be today or in the future")
    private LocalDate deadline;

    @NotNull(message = "Status is required")
    private ProjectStatus status;

    @NotNull(message = "Owner ID is required")
    private Long ownerId;
}
