# TaskFlow 🗂️

Aplicación de gestión de tareas por proyectos construida con **Spring Boot 3**, **Thymeleaf**, **Spring Security** y **H2** (base de datos en memoria para desarrollo).

---

## 🚀 Cómo ejecutar

### Requisitos previos
- Java 17+
- Maven 3.8+

### Pasos

```bash
# Clonar / descomprimir el proyecto
cd taskflow

# Compilar y ejecutar
mvn spring-boot:run
```

La aplicación estará disponible en: **http://localhost:8080**

---

## 👤 Credenciales de prueba

| Campo    | Valor        |
|----------|--------------|
| Usuario  | `juan`       |
| Contraseña | `password123` |

---

## 📁 Estructura del proyecto

```
taskflow/
├── src/main/java/com/taskflow/
│   ├── TaskFlowApplication.java          # Clase principal
│   ├── config/
│   │   ├── SecurityConfig.java           # Spring Security
│   │   └── DataInitializer.java          # Datos de prueba
│   ├── controller/
│   │   ├── AuthController.java           # Login / Registro
│   │   ├── DashboardController.java      # Dashboard de proyectos
│   │   └── ProjectController.java        # Tablero Kanban
│   ├── model/
│   │   ├── User.java
│   │   ├── Project.java
│   │   ├── Task.java
│   │   ├── TaskStatus.java               # TODO / IN_PROGRESS / DONE
│   │   └── TaskPriority.java             # LOW / MEDIUM / HIGH
│   ├── repository/
│   │   ├── UserRepository.java
│   │   ├── ProjectRepository.java
│   │   └── TaskRepository.java
│   └── service/
│       ├── UserDetailsServiceImpl.java
│       ├── UserService.java
│       ├── ProjectService.java
│       └── TaskService.java
│
├── src/main/resources/
│   ├── application.properties
│   ├── static/
│   │   ├── css/
│   │   │   ├── main.css       # Estilos globales
│   │   │   ├── auth.css       # Login / Registro
│   │   │   ├── dashboard.css  # Dashboard
│   │   │   └── board.css      # Tablero Kanban
│   │   └── js/
│   │       └── app.js         # Modales, dropdowns, JS global
│   └── templates/
│       ├── auth/
│       │   ├── login.html
│       │   └── register.html
│       ├── dashboard/
│       │   └── index.html
│       └── project/
│           └── board.html
│
└── pom.xml
```

---

## ✨ Funcionalidades

- **Autenticación**: Login / Logout / Registro con Spring Security + BCrypt
- **Proyectos**: Crear, editar y eliminar proyectos (aislados por usuario)
- **Tablero Kanban**: Columnas *Por Hacer*, *En Proceso*, *Completado*
- **Tareas**: Crear, editar, eliminar y mover entre columnas
- **Prioridades**: Alta, Media y Baja con badges de color
- **Datos de demo**: Se cargan automáticamente al iniciar

---

## 🛠️ Migrar a PostgreSQL (producción)

1. Añadir dependencia en `pom.xml`:
```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

2. Actualizar `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/taskflow
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
```
