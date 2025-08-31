package com.projects.personal.projectManagementSystem.repository;

import com.projects.personal.projectManagementSystem.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByOwnerId(Long ownerId);
}
