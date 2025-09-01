package com.projects.personal.projectManagementSystem.repository;

import com.projects.personal.projectManagementSystem.entity.TaskDependency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskDependencyRepository extends JpaRepository<TaskDependency, Long> {
    List<TaskDependency> findByTaskId(Long taskId);
    List<TaskDependency> findByDependsOnTaskId(Long dependsOnTaskId);

    @Query("SELECT td.dependsOnTask.id FROM TaskDependency td WHERE td.task.id = :taskId")
    List<Long> findDependenciesByTaskId(@Param("taskId") Long taskId);

}
