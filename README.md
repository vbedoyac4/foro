# Foro

## Descripción
Plataforma de foro desarrollada con Spring Boot que permite la interacción entre usuarios mediante discusiones y comentarios.

## Tecnologías
- Spring Boot
- Spring Security 
- Spring Data JPA
- MySQL
- Flyway Migration
- Maven

## Requisitos
- Java 17 o superior
- Maven
- MySQL

## Configuración
1. Clonar el repositorio
2. Configurar la base de datos MySQL
3. Ajustar las propiedades de conexión en application.properties
4. Ejecutar las migraciones de Flyway

## Ejecución
Para ejecutar el proyecto localmente:


## Características

- Autenticación y autorización de usuarios
- Creación y gestión de temas
- Sistema de comentarios
- Validaciones de datos
- API RESTful
- Documentación
La documentación de la API está disponible en /swagger-ui.html cuando el proyecto está en ejecución.

```bash
mvn spring-boot:run