```text
████████╗██╗███╗   ██╗         ██╗ █████╗ ██╗   ██╗ █████╗
╚══██╔══╝██║████╗  ██║         ██║██╔══██╗██║   ██║██╔══██╗
   ██║   ██║██╔██╗ ██║         ██║███████║██║   ██║███████║
   ██║   ██║██║╚██╗██║    ██   ██║██╔══██║╚██╗ ██╔╝██╔══██║
   ██║   ██║██║ ╚████║    ╚█████╔╝██║  ██║ ╚████╔╝ ██║  ██║
   ╚═╝   ╚═╝╚═╝  ╚═══╝     ╚════╝ ╚═╝  ╚═╝  ╚═══╝  ╚═╝  ╚═╝

                TIN JAVA
```

## Spring Boot Demo (Backend)

Project demo sử dụng Spring Boot + PostgreSQL + Docker để quản lý User.

Technical Stack: Java 17, Spring Boot, Spring Data JPA, PostgreSQL, Maven, Docker, Docker Compose.

# Project Structure

```
src/main/java/com.ndt.ktpm.Demo
│
├── Controller
│   └── UserController
│
├── Domain
│   ├── User
│   └── Response
│
├── Repository
│   └── UserRepository
│
└── DemoApplication
```

# Run Project

## Docker Compose (khuyên dùng)

```bash
docker compose up -d --build
```

API: http://localhost:8080/api/v1/users

```bash
docker compose logs -f app
docker compose down
```

## Build JAR thủ công

```bash
mvn clean package -DskipTests
java -jar target/Demo-0.0.1-SNAPSHOT.jar
```

# Author

Tin Nguyen
