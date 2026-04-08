package com.TaskFlow.config;

import com.TaskFlow.model.*;
import com.TaskFlow.repository.*;
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
            try {
                if (!userRepo.findByUsername("juan").isPresent()) {
                    System.out.println("🌱 Cargando datos iniciales...");
                    
                    User juan = new User("juan", passwordEncoder.encode("password123"), "Juan García", "juan@example.com");
                    userRepo.save(juan);

                    Project proyectoA = new Project("Proyecto A", "Proyecto de ejemplo", juan);
                    projectRepo.save(proyectoA);

                    taskRepo.save(new Task("Tarea Inicial", "Descripción", TaskStatus.TODO, TaskPriority.MEDIUM, proyectoA));
                    
                    System.out.println("✅ Datos de prueba cargados correctamente.");
                }
            } catch (Exception e) {
                System.err.println("⚠️ Error en DataInitializer: " + e.getMessage());
            }
        };
    }
}
