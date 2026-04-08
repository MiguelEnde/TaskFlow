package com.TaskFlow.controller;

import com.TaskFlow.model.*;
import com.TaskFlow.service.*;
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

    @Autowired private UserService userService;
    @Autowired private ProjectService projectService;
    @Autowired private TaskService taskService;

    @GetMapping("/{id}")
    public String projectBoard(@PathVariable Long id, Principal principal, Model model) {
        try {
            User user = userService.getCurrentUser(principal.getName());
            Project project = projectService.getProjectByIdAndUser(id, user);

            model.addAttribute("user", user);
            model.addAttribute("project", project);
            model.addAttribute("todoTasks", taskService.getTasksByProjectAndStatus(project, TaskStatus.TODO));
            model.addAttribute("inProgressTasks", taskService.getTasksByProjectAndStatus(project, TaskStatus.IN_PROGRESS));
            model.addAttribute("doneTasks", taskService.getTasksByProjectAndStatus(project, TaskStatus.DONE));
            model.addAttribute("messages", projectService.getChatMessages(project));
            model.addAttribute("potentialMembers", projectService.getPotentialMembers(project));

            return "project/board";
        } catch (Exception e) {
            return "redirect:/dashboard"; 
        }
    }

    @PostMapping("/{id}/invite")
    public String inviteMember(@PathVariable Long id, @RequestParam String username, Principal principal, RedirectAttributes ra) {
        try {
            projectService.inviteMember(id, username);
        } catch (Exception e) {}
        return "redirect:/projects/" + id;
    }

    @PostMapping("/{id}/chat/send")
    public String sendMessage(@PathVariable Long id, @RequestParam String content, Principal principal) {
        User user = userService.getCurrentUser(principal.getName());
        Project project = projectService.getProjectByIdAndUser(id, user);
        if (content != null && !content.trim().isEmpty()) {
            projectService.saveChatMessage(content, user, project);
        }
        return "redirect:/projects/" + id;
    }

    @PostMapping("/{projectId}/tasks/new")
    public String createTask(@PathVariable Long projectId, @RequestParam String title, @RequestParam TaskStatus status, @RequestParam TaskPriority priority, Principal principal) {
        User user = userService.getCurrentUser(principal.getName());
        Project project = projectService.getProjectByIdAndUser(projectId, user);
        taskService.createTask(title, "", status, priority, project);
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/{projectId}/tasks/{taskId}/edit")
    public String editTask(@PathVariable Long projectId, @PathVariable Long taskId, 
                          @RequestParam String title, @RequestParam TaskStatus status, 
                          @RequestParam TaskPriority priority, Principal principal) {
        User user = userService.getCurrentUser(principal.getName());
        projectService.getProjectByIdAndUser(projectId, user);
        // Pasamos el usuario que edita para que se actualice el ranking
        taskService.updateTask(taskId, projectId, title, "", status, priority, user);
        return "redirect:/projects/" + projectId;
    }
}
