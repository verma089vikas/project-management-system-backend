package com.projects.personal.projectManagementSystem.service;

import com.projects.personal.projectManagementSystem.entity.TaskDependency;
import java.util.List;

public interface TaskDependencyService {
    TaskDependency createDependency(Long taskId, Long dependsOnTaskId);
    List<TaskDependency> getDependenciesByTask(Long taskId);
    List<TaskDependency> getDependents(Long dependsOnTaskId);
    void deleteDependency(Long dependencyId);
}
