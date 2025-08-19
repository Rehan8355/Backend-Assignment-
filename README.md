# FreightFox PhantomBuster API

A Spring Boot API that integrates with [PhantomBuster](https://phantombuster.com/) for automated data extraction, built with PostgreSQL as the persistence layer.

---

## 🚀 Features
- RESTful API using Spring Boot 3
- PostgreSQL database integration
- Clean architecture (Controller → Service → Repository)
- DTOs + Validation
- Global error handling
- JUnit 5 + Mockito unit tests
- PhantomBuster API client stub

---

## 📂 Project Structure
src/main/java/com/freightfox

├── controller # REST controllers

├── dto # Data Transfer Objects

├── entity # JPA entities

├── exception # Global exception handling

├── repository # Spring Data JPA repositories

├── service # Business logic

└── client # PhantomBuster API client

