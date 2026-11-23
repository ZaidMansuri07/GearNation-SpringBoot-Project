# GearNation - High-Performance Car Showcase

GearNation is a Spring Boot web application designed to showcase elite automotive engineering. It features a curated list of high-performance cars (The Elite Eight), user authentication, and an administrative dashboard for member management.

## 🚀 Features
- **User Module**: Secure Signup, Login, and Profile Management.
- **Forgot Password**: Fully functional password recovery flow via email lookup.
- **Elite Eight Showcase**: Dedicated high-fidelity pages for brands like BMW, Bugatti, and McLaren.
- **Admin Dashboard**: Full CRUD (Create, Read, Update, Delete) operations for managing user records.
- **Responsive Design**: Custom CSS styling with a dark-themed "Carbon Fiber" aesthetic.

## 🛠️ Tech Stack
- **Backend**: Java 21, Spring Boot 4
- **Frontend**: Thymeleaf, HTML5, CSS3, JavaScript
- **Database**: MySQL (via XAMPP)
- **Dependencies**: Spring Data JPA, Spring Web MVC, Validation API, Lombok

## ⚙️ Setup Instructions

**Prerequisites:** JDK 21 and MySQL running on port 3306 (XAMPP works).

1. **Clone the repo**:
   ```bash
   git clone https://github.com/ZaidMansuri07/GearNation-SpringBoot-Project.git
   cd GearNation-SpringBoot-Project
   ```

2. **Create the database** (tables are created automatically on first run):
   ```sql
   CREATE DATABASE carnation;
   ```

3. **Check the database settings** in `src/main/resources/application.properties`. The defaults are user `root` with an empty password. Change them to match your MySQL setup.

4. **Run the app**:
   ```bash
   ./mvnw spring-boot:run
   ```
   On Windows use `mvnw.cmd spring-boot:run`.

5. **Open the site** at http://localhost:3031
