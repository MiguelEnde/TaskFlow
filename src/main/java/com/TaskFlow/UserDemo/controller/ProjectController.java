package com.taskflow.controller;

import com.taskflow.model.*;
import com.taskflow.service.ProjectService;
import com.taskflow.service.TaskService;
import com.taskflow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskService taskService;

    @GetMapping("/{id}")
    public String projectBoard(@PathVariable Long id, Principal principal, Model model) {
        User user = userService.getCurrentUser(principal.getName());
        Project project = projectService.getProjectByIdAndUser(id, user);

        List<Task> todoTasks = taskService.getTasksByProjectAndStatus(project, TaskStatus.TODO);
        List<Task> inProgressTasks = taskService.getTasksByProjectAndStatus(project, TaskStatus.IN_PROGRESS);
        List<Task> doneTasks = taskService.getTasksByProjectAndStatus(project, TaskStatus.DONE);

        model.addAttribute("user", user);
        model.addAttribute("project", project);
        model.addAttribute("todoTasks", todoTasks);
        model.addAttribute("inProgressTasks", inProgressTasks);
        model.addAttribute("doneTasks", doneTasks);
        model.addAttribute("statuses", TaskStatus.values());
        model.addAttribute("priorities", TaskPriority.values());

        return "project/board";
    }

    @PostMapping("/{projectId}/tasks/new")
    public String createTask(@PathVariable Long projectId,
                              @RequestParam String title,
                              @RequestParam(required = false) String description,
                              @RequestParam TaskStatus status,
                              @RequestParam TaskPriority priority,
                              Principal principal,
                              RedirectAttributes redirectAttributes) {
        User user = userService.getCurrentUser(principal.getName());
        Project project = projectService.getProjectByIdAndUser(projectId, user);
        taskService.createTask(title, description != null ? description : "", status, priority, project);
        redirectAttributes.addFlashAttribute("successMsg", "Tarea añadida.");
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{projectId}/tasks/{taskId}/status")
    public String updateTaskStatus(@PathVariable Long projectId,
                                    @PathVariable Long taskId,
                                    @RequestParam TaskStatus status,
                                    Principal principal,
                                    RedirectAttributes redirectAttributes) {
        User user = userService.getCurrentUser(principal.getName());
        // Verify ownership
        projectService.getProjectByIdAndUser(projectId, user);
        taskService.updateTaskStatus(taskId, projectId, status);
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{projectId}/tasks/{taskId}/edit")
    public String editTask(@PathVariable Long projectId,
                            @PathVariable Long taskId,
                            @RequestParam String title,
                            @RequestParam(required = false) String description,
                            @RequestParam TaskStatus status,
                            @RequestParam TaskPriority priority,
                            Principal principal,
                            RedirectAttributes redirectAttributes) {
        User user = userService.getCurrentUser(principal.getName());
        projectService.getProjectByIdAndUser(projectId, user);
        taskService.updateTask(taskId, projectId, title, description != null ? description : "", status, priority);
        redirectAttributes.addFlashAttribute("successMsg", "Tarea actualizada.");
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{projectId}/tasks/{taskId}/delete")
    public String deleteTask(@PathVariable Long projectId,
                              @PathVariable Long taskId,
                              Principal principal,
                              RedirectAttributes redirectAttributes) {
        User user = userService.getCurrentUser(principal.getName());
        projectService.getProjectByIdAndUser(projectId, user);
        taskService.deleteTask(taskId, projectId);
        redirectAttributes.addFlashAttribute("successMsg", "Tarea eliminada.");
        return "redirect:/projects/" + projectId;
    }
}
