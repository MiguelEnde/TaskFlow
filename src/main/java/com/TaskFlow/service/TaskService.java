package com.TaskFlow.service;

import com.TaskFlow.model.*;
import com.TaskFlow.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getTasksByProjectAndStatus(Project project, TaskStatus status) {
        return taskRepository.findByProjectAndStatusOrderByCreatedAtDesc(project, status);
    }

    public Task createTask(String title, String description, TaskStatus status, TaskPriority priority, Project project) {
        Task task = new Task(title, description, status, priority, project);
        return taskRepository.save(task);
    }

    public Task updateTaskStatus(Long taskId, Long projectId, TaskStatus newStatus) {
        Task task = taskRepository.findByIdAndProjectId(taskId, projectId)
            .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        task.setStatus(newStatus);
        return taskRepository.save(task);
    }

    public Task updateTask(Long taskId, Long projectId, String title, String description, TaskStatus status, TaskPriority priority, User user) {
        Task task = taskRepository.findByIdAndProjectId(taskId, projectId)
            .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        task.setPriority(priority);
        
        // Si la tarea se marca como DONE, asignamos al usuario que lo hizo para el ranking
        if (status == TaskStatus.DONE) {
            task.setAssignedTo(user);
        }

        return taskRepository.save(task);
    }

    public void deleteTask(Long taskId, Long projectId) {
        Task task = taskRepository.findByIdAndProjectId(taskId, projectId)
            .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        taskRepository.delete(task);
    }
}
