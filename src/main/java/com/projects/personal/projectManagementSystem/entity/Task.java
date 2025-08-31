package com.projects.personal.projectManagementSystem.entity;

import com.projects.personal.projectManagementSystem.entity.User;
import com.projects.personal.projectManagementSystem.enums.TaskPriority;
import com.projects.personal.projectManagementSystem.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    private Double estimatedHours;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
