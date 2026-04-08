package com.TaskFlow.repository;

import com.TaskFlow.model.Task;
import com.TaskFlow.model.TaskStatus;
import com.TaskFlow.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectAndStatusOrderByCreatedAtDesc(Project project, TaskStatus status);
    Optional<Task> findByIdAndProjectId(Long id, Long projectId);
}
