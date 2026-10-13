# CampusConnect – Spring Boot Backend REST API ⚙️

Enterprise backend REST API for **CampusConnect – Smart Student Skill & Opportunity Platform** built with **Java 21**, **Spring Boot 3.3.4**, **Spring Data JPA**, **MySQL 8.0**, and **JDBC**.

---

## 🚀 Features
- **Layered Architecture:** Controller ➔ Service ➔ Repository ➔ Entity / DTOs.
- **Jakarta Bean Validation:** `@Valid`, `@NotBlank`, `@Email`, `@DecimalMin`, `@DecimalMax`.
- **Global Exception Handling:** `@RestControllerAdvice` handling validation errors and custom exceptions.
- **Smart Matching Service:** Stream and Lambda algorithms calculating candidate match scores.
- **Direct JDBC Architecture:** `JdbcDemoServiceImpl` demonstrating raw `Connection`, `PreparedStatement`, `ResultSet`, and `DatabaseMetaData`.
- **CORS Config:** Configured for `http://localhost:5173` and `http://localhost:3000`.

---

## 🛠️ Setup & Running

```bash
# Run Spring Boot backend
mvn spring-boot:run
```

API runs on: **`http://localhost:8080/api`**
MySQL Database: **`campusconnect_db` on port 3306**
