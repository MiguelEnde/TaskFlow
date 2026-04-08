package com.TaskFlow.service;

import com.TaskFlow.model.*;
import com.TaskFlow.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public List<Project> getProjectsByUser(User user) {
        return projectRepository.findByUser(user);
    }

    public Project getProjectByIdAndUser(Long id, User user) {
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
        
        boolean isOwner = project.getOwner().getId().equals(user.getId());
        boolean isMember = project.getMembers().stream().anyMatch(m -> m.getId().equals(user.getId()));
        
        if (!isOwner && !isMember) {
            throw new RuntimeException("No tienes acceso a este proyecto");
        }
        return project;
    }

    public Project createProject(String name, String description, User owner) {
        Project project = new Project(name, description, owner);
        return projectRepository.save(project);
    }

    public void inviteMember(Long projectId, String username) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
        User member = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        project.addMember(member);
        projectRepository.save(project);
    }

    public void saveChatMessage(String content, User sender, Project project) {
        ChatMessage message = new ChatMessage(content, sender, project);
        chatMessageRepository.save(message);
    }

    public List<ChatMessage> getChatMessages(Project project) {
        return chatMessageRepository.findByProjectOrderByTimestampAsc(project);
    }

    public List<User> getPotentialMembers(Project project) {
        List<User> allUsers = userRepository.findAll();
        Long ownerId = project.getOwner().getId();
        
        // Devolvemos todos los usuarios excepto el dueño
        // Si quieres filtrar también a los que YA son miembros, descomenta la línea de abajo
        return allUsers.stream()
            .filter(u -> !u.getId().equals(ownerId))
            // .filter(u -> project.getMembers().stream().noneMatch(m -> m.getId().equals(u.getId())))
            .collect(Collectors.toList());
    }
}
