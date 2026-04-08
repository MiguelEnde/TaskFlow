package com.TaskFlow.repository;

import com.TaskFlow.model.ProjectInvitation;
import com.TaskFlow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectInvitationRepository extends JpaRepository<ProjectInvitation, Long> {
    List<ProjectInvitation> findByRecipientAndStatus(User recipient, String status);
    boolean existsByProjectAndRecipientAndStatus(com.TaskFlow.model.Project project, User recipient, String status);
}
