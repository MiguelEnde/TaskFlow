package com.taskflow.config;

import com.taskflow.model.*;
import com.taskflow.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData(UserRepository userRepo,
                                     ProjectRepository projectRepo,
                                     TaskRepository taskRepo) {
        return args -> {
            // Demo user
            User juan = new User("juan", passwordEncoder.encode("password123"), "Juan García", "juan@example.com");
            userRepo.save(juan);

            // Proyecto A
            Project proyectoA = new Project("Proyecto A", "Primer proyecto de prueba", juan);
            projectRepo.save(proyectoA);

            taskRepo.save(new Task("Tarea 1", "Descripción de la tarea 1", TaskStatus.TODO, TaskPriority.MEDIUM, proyectoA));
            taskRepo.save(new Task("Tarea 2", "Descripción de la tarea 2", TaskStatus.TODO, TaskPriority.LOW, proyectoA));
            taskRepo.save(new Task("Tarea 3", "Análisis de requisitos", TaskStatus.IN_PROGRESS, TaskPriority.HIGH, proyectoA));
            taskRepo.save(new Task("Tarea 4", "Revisar diseño", TaskStatus.IN_PROGRESS, TaskPriority.MEDIUM, proyectoA));
            taskRepo.save(new Task("Tarea 5", "Informe final", TaskStatus.DONE, TaskPriority.HIGH, proyectoA));
            taskRepo.save(new Task("Tarea 6", "Presentación", TaskStatus.DONE, TaskPriority.MEDIUM, proyectoA));

            // Proyecto B
            Project proyectoB = new Project("Proyecto B", "Segundo proyecto", juan);
            projectRepo.save(proyectoB);
            taskRepo.save(new Task("Diseño UI", "Crear mockups", TaskStatus.DONE, TaskPriority.HIGH, proyectoB));
            taskRepo.save(new Task("Backend API", "Desarrollar endpoints", TaskStatus.IN_PROGRESS, TaskPriority.HIGH, proyectoB));
            taskRepo.save(new Task("Testing", "Pruebas unitarias", TaskStatus.TODO, TaskPriority.MEDIUM, proyectoB));

            // Marketing Plan
            Project marketing = new Project("Marketing Plan", "Estrategia de marketing Q1", juan);
            projectRepo.save(marketing);
            for (int i = 1; i <= 5; i++) {
                taskRepo.save(new Task("Tarea marketing " + i, "", TaskStatus.IN_PROGRESS, TaskPriority.MEDIUM, marketing));
            }
            for (int i = 6; i <= 15; i++) {
                taskRepo.save(new Task("Tarea marketing " + i, "", TaskStatus.TODO, TaskPriority.LOW, marketing));
            }

            // Desarrollo App
            Project devApp = new Project("Desarrollo App", "App móvil iOS/Android", juan);
            projectRepo.save(devApp);
            for (int i = 1; i <= 4; i++) {
                taskRepo.save(new Task("Sprint " + i + " backlog", "", TaskStatus.TODO, TaskPriority.MEDIUM, devApp));
            }
            for (int i = 1; i <= 4; i++) {
                taskRepo.save(new Task("Feature " + i, "", TaskStatus.IN_PROGRESS, TaskPriority.HIGH, devApp));
            }
            for (int i = 1; i <= 4; i++) {
                taskRepo.save(new Task("Release " + i, "", TaskStatus.DONE, TaskPriority.HIGH, devApp));
            }

            System.out.println("✅ Datos de prueba cargados. Usuario: juan / password123");
        };
    }
}
