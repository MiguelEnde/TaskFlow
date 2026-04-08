package com.TaskFlow.repository;

import com.TaskFlow.model.Project;
import com.TaskFlow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByOwnerOrderByCreatedAtDesc(User owner);

    // Modificado para cargar las tareas junto con los proyectos
    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN FETCH p.members m LEFT JOIN FETCH p.tasks t WHERE p.owner = :user OR m = :user ORDER BY p.createdAt DESC")
    List<Project> findByUser(@Param("user") User user);

    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.tasks WHERE p.id = :id AND p.owner = :owner")
    Optional<Project> findByIdAndOwnerWithTasks(Long id, User owner);

    Optional<Project> findByIdAndOwner(Long id, User owner);
}
