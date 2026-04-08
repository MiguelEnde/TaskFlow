package com.TaskFlow.repository;

import com.TaskFlow.model.ChatMessage;
import com.TaskFlow.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByProjectOrderByTimestampAsc(Project project);
}
