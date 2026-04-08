package com.TaskFlow.controller;

import com.TaskFlow.model.*;
import com.TaskFlow.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired private UserService userService;
    @Autowired private ProjectService projectService;
    @Autowired private TaskService taskService;
    @Autowired private MessageSource messageSource;

    @Autowired private com.TaskFlow.repository.TaskRepository taskRepository;
    @Autowired private com.TaskFlow.repository.ProjectRepository projectRepository;

    @GetMapping
    public String dashboard(Principal principal, Model model) {
        try {
            User currentUser = userService.getCurrentUser(principal.getName());
            Locale locale = LocaleContextHolder.getLocale();
            
            // --- 1. DATOS REALMENTE GLOBALES (Toda la BD) ---
            List<Task> systemAllTasks = taskRepository.findAll();
            List<User> systemAllUsers = userService.getAllUsers();
            
            Map<String, Long> globalRankingMap = new HashMap<>();
            // Inicializar a todos los usuarios con 0 tareas completadas
            for (User u : systemAllUsers) {
                globalRankingMap.put(u.getDisplayName(), 0L);
            }

            // Contar tareas DONE globales
            for (Task t : systemAllTasks) {
                if (t.getStatus() == TaskStatus.DONE) {
                    User performer = t.getAssignedTo();
                    if (performer == null) {
                        // Si no hay asignado, se le cuenta al dueño del proyecto
                        performer = t.getProject().getOwner();
                    }
                    if (performer != null) {
                        String name = performer.getDisplayName();
                        globalRankingMap.put(name, globalRankingMap.getOrDefault(name, 0L) + 1);
                    }
                }
            }

            // Ordenar ranking y tomar los top 5 (incluyendo los de 0 tareas)
            List<Map.Entry<String, Long>> sortedRanking = globalRankingMap.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed()
                            .thenComparing(Map.Entry.comparingByKey())) // Para desempates alfabéticamente
                    .limit(5)
                    .collect(Collectors.toList());

            // --- 2. DATOS PERSONALES DEL USUARIO ---
            List<Project> myProjects = projectService.getProjectsByUser(currentUser);
            List<Task> myTasks = systemAllTasks.stream()
                .filter(t -> (t.getAssignedTo() != null && t.getAssignedTo().getId().equals(currentUser.getId())) || 
                             (t.getAssignedTo() == null && t.getProject().getOwner().getId().equals(currentUser.getId())))
                .collect(Collectors.toList());

            // --- 3. PREPARAR MODELO ---
            model.addAttribute("user", currentUser);
            model.addAttribute("projects", myProjects); 
            model.addAttribute("allTasks", myTasks); 
            model.addAttribute("ranking", sortedRanking);
            
            // Strings para Gráficos Globales (Sistema completo)
            String rankingNamesStr = sortedRanking.stream().map(Map.Entry::getKey).collect(Collectors.joining(","));
            String rankingValuesStr = sortedRanking.stream().map(e -> e.getValue().toString()).collect(Collectors.joining(","));
            model.addAttribute("rankingNamesStr", rankingNamesStr);
            model.addAttribute("rankingValuesStr", rankingValuesStr);
            
            // Donut Global (Sistema completo)
            long globalTodo = systemAllTasks.stream().filter(t -> t.getStatus() == TaskStatus.TODO).count();
            long globalDoing = systemAllTasks.stream().filter(t -> t.getStatus() == TaskStatus.IN_PROGRESS).count();
            long globalDone = systemAllTasks.stream().filter(t -> t.getStatus() == TaskStatus.DONE).count();
            model.addAttribute("globalTodo", globalTodo);
            model.addAttribute("globalDoing", globalDoing);
            model.addAttribute("globalDone", globalDone);

            // Donut Personal
            long myTodo = myTasks.stream().filter(t -> t.getStatus() == TaskStatus.TODO).count();
            long myDoing = myTasks.stream().filter(t -> t.getStatus() == TaskStatus.IN_PROGRESS).count();
            long myDone = myTasks.stream().filter(t -> t.getStatus() == TaskStatus.DONE).count();
            model.addAttribute("myTodo", myTodo);
            model.addAttribute("myDoing", myDoing);
            model.addAttribute("myDone", myDone);

            // Etiquetas
            String todoLabel = messageSource.getMessage("task.status.todo", null, locale);
            String doingLabel = messageSource.getMessage("task.status.in_progress", null, locale);
            String doneLabel = messageSource.getMessage("task.status.done", null, locale);
            model.addAttribute("statusLabelsStr", todoLabel + "," + doingLabel + "," + doneLabel);

            model.addAttribute("statuses", TaskStatus.values());
            model.addAttribute("priorities", TaskPriority.values());
            
            // --- DEBUGGING ---
            System.out.println("--- Dashboard Data ---");
            System.out.println("Current User: " + currentUser.getDisplayName());
            System.out.println("Global Ranking Map: " + globalRankingMap);
            System.out.println("Sorted Ranking (Top 5): " + sortedRanking);
            System.out.println("Ranking Names String: " + rankingNamesStr);
            System.out.println("Ranking Values String: " + rankingValuesStr);
            System.out.println("Global Todo: " + globalTodo + ", Doing: " + globalDoing + ", Done: " + globalDone);
            System.out.println("My Todo: " + myTodo + ", Doing: " + myDoing + ", Done: " + myDone);
            System.out.println("----------------------");

            return "dashboard/index";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/tasks/add")
    public String addTask(@RequestParam Long projectId, @RequestParam String title, 
                          @RequestParam TaskStatus status, @RequestParam TaskPriority priority,
                          Principal principal) {
        User user = userService.getCurrentUser(principal.getName());
        Project project = projectService.getProjectByIdAndUser(projectId, user);
        taskService.createTask(title, "", status, priority, project);
        return "redirect:/dashboard";
    }
    
    @PostMapping("/projects/new")
    public String createProject(@RequestParam String name, Principal principal) {
        User user = userService.getCurrentUser(principal.getName());
        projectService.createProject(name, "", user);
        return "redirect:/dashboard";
    }
}
