package com.taskflow.repository;

import com.taskflow.model.Project;
import com.taskflow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByOwnerOrderByCreatedAtDesc(User owner);

    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.tasks WHERE p.id = :id AND p.owner = :owner")
    Optional<Project> findByIdAndOwnerWithTasks(Long id, User owner);

    Optional<Project> findByIdAndOwner(Long id, User owner);
}
