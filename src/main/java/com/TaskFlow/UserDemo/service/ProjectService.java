package com.taskflow.service;

import com.taskflow.model.Project;
import com.taskflow.model.User;
import com.taskflow.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public List<Project> getProjectsByUser(User user) {
        return projectRepository.findByOwnerOrderByCreatedAtDesc(user);
    }

    public Project getProjectByIdAndUser(Long id, User user) {
        return projectRepository.findByIdAndOwnerWithTasks(id, user)
            .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
    }

    public Project createProject(String name, String description, User owner) {
        Project project = new Project(name, description, owner);
        return projectRepository.save(project);
    }

    public Project updateProject(Long id, String name, String description, User owner) {
        Project project = projectRepository.findByIdAndOwner(id, owner)
            .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
        project.setName(name);
        project.setDescription(description);
        return projectRepository.save(project);
    }

    public void deleteProject(Long id, User owner) {
        Project project = projectRepository.findByIdAndOwner(id, owner)
            .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
        projectRepository.delete(project);
    }
}
