# FinanControl

## 🎯 Project Objective

This project was developed as a **"vibe coding"** experiment in partnership with the AI assistant **Google Antigravity**.

The main technical objective was to explore and validate the integration of the component-based web framework **Apache Wicket** with the **Spring Boot** ecosystem, building a robust application using a **monolithic** architecture.

## 🚀 Features

The system offers a complete platform for personal financial control:

*   **Authentication and Security**:
    *   New user registration.
    *   Secure login.
    *   Password recovery via email token.
    *   Password protection using **Argon2** hashing.
*   **Financial Management**:
    *   **Dashboard**: Overview of financial health.
    *   **Expenses and Incomes**: Registration, editing, and viewing of transactions.
    *   **Categories**: Organization of entries by customizable categories.
    *   **Goals**: Definition and tracking of financial goals.

## 🛠️ Technologies Used

The project uses a modern Java-based stack:

*   **Java 21**: Base language.
*   **Spring Boot 3**: Base framework for configuration and inversion of control.
*   **Apache Wicket 10**: Component-oriented web framework for the User Interface (Frontend/Backend integration).
*   **Spring Data JPA / Hibernate**: Persistence layer and ORM.
*   **Spring Security**: Authentication and authorization management.
*   **PostgreSQL**: Robust relational database.
*   **JavaMailSender**: Email sending service (SMTP).
*   **SLF4J & Logback**: Logging system.

## 🏛️ Patterns and Architecture Adopted

The application follows a classic and well-structured monolithic architecture:

*   **Wicket & Spring Integration**: Use of `@SpringBean` to inject Spring services directly into Wicket visual components, maintaining a clean separation between presentation logic and business logic.
*   **Service Layer Pattern**: Business rules encapsulated in transactional services (`@Service`).
*   **Repository Pattern**: Data access abstraction via `JpaRepository` interfaces.
*   **Component-Based UI**: Construction of reusable and modular pages (e.g., `BasePage`, custom `FeedbackPanel`) leveraging Wicket's inheritance and composition.

## 🏃 How to Run Locally

### Prerequisites

*   **Java 21 JDK** installed.
*   **Maven** installed (or use the `mvnw` wrapper included in the project).
*   **PostgreSQL** installed and running.

### 1. Database Setup

Create a PostgreSQL database (e.g., `financontrol_db`):

```sql
CREATE DATABASE financontrol_db;
```

### 2. Environment Variables

The application relies on environment variables for sensitive configuration. You can set them in your IDE, terminal, or create a configuration file.

Create a `.env` file (or set them in your runner configuration):

```properties
FINANCONTROL_DB_URL=jdbc:postgresql://localhost:5432/financontrol_db
FINANCONTROL_DB_USERNAME=your_db_username
FINANCONTROL_DB_PASSWORD=your_db_password

# Email configuration (e.g., using Mailtrap for testing)
FINANCONTROL_MAIL_HOST=sandbox.smtp.mailtrap.io
FINANCONTROL_MAIL_PORT=2525
FINANCONTROL_MAIL_USERNAME=your_mail_username
FINANCONTROL_MAIL_PASSWORD=your_mail_password
```

### 3. Build and Run

Run using Maven:

```bash
mvn spring-boot:run
```

The application will be available at [http://localhost:8080](http://localhost:8080).
