package com.taskflow.service;

import com.taskflow.model.*;
import com.taskflow.repository.TaskRepository;
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

    public Task updateTask(Long taskId, Long projectId, String title, String description, TaskStatus status, TaskPriority priority) {
        Task task = taskRepository.findByIdAndProjectId(taskId, projectId)
            .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        task.setPriority(priority);
        return taskRepository.save(task);
    }

    public void deleteTask(Long taskId, Long projectId) {
        Task task = taskRepository.findByIdAndProjectId(taskId, projectId)
            .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        taskRepository.delete(task);
    }
}
