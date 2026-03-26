package com.taskflow.controller;

import com.taskflow.model.Project;
import com.taskflow.model.User;
import com.taskflow.service.ProjectService;
import com.taskflow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public String dashboard(Principal principal, Model model) {
        User user = userService.getCurrentUser(principal.getName());
        List<Project> projects = projectService.getProjectsByUser(user);
        model.addAttribute("user", user);
        model.addAttribute("projects", projects);
        return "dashboard/index";
    }

    @PostMapping("/projects/new")
    public String createProject(@RequestParam String name,
                                 @RequestParam(required = false) String description,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {
        User user = userService.getCurrentUser(principal.getName());
        projectService.createProject(name, description != null ? description : "", user);
        redirectAttributes.addFlashAttribute("successMsg", "Proyecto creado correctamente.");
        return "redirect:/dashboard";
    }

    @PostMapping("/projects/{id}/edit")
    public String editProject(@PathVariable Long id,
                               @RequestParam String name,
                               @RequestParam(required = false) String description,
                               Principal principal,
                               RedirectAttributes redirectAttributes) {
        User user = userService.getCurrentUser(principal.getName());
        projectService.updateProject(id, name, description != null ? description : "", user);
        redirectAttributes.addFlashAttribute("successMsg", "Proyecto actualizado.");
        return "redirect:/dashboard";
    }

    @PostMapping("/projects/{id}/delete")
    public String deleteProject(@PathVariable Long id,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {
        User user = userService.getCurrentUser(principal.getName());
        projectService.deleteProject(id, user);
        redirectAttributes.addFlashAttribute("successMsg", "Proyecto eliminado.");
        return "redirect:/dashboard";
    }
}
