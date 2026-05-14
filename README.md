# 📚 Library Management System

A Spring Boot application for managing books and borrowed records in a library.  
This project demonstrates RESTful API design, DTO usage, and controller/service separation.

-------------------------------------------------------------------------

## 🚀 Features
- Register, update, and delete books
- Register, update and delete borrower
- Borrow and return books
- View all borrowed records
- View borrowed records by borrower
- A borrower can borrow multiple books, but only one copy of a specific book id at a time.
- ISBN validation ensures consistency: same ISBN → same title & author. 
- Books with the same ISBN cannot be registered new, it will be registered as a copy book in book record.
- Multiple copies of the book will be recorded in the book record.
- Returning a book frees it for others. 

-------------------------------------------------------------------------

## 🛠 Tech Stack
- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL (or any relational DB)
- Maven

-------------------------------------------------------------------------

## End Point

- POST /api/books → Add book
- PUT /api/books/{bookId} → Update book
- DELETE /api/books/{bookId} → Delete all book
- GET /api/books → List books
- POST /api/borrowers → Add borrower
- GET /api/borrowers → List borrowers
- PUT /api/borrowers/{id} → Update borrowers
- DELETE /api/borrowers/{id} → Delete borrowers
- POST /api/book_record → Add book copies
- GET /api/book_record → List all books include book copies record
- DELETE /api/book_record/{bookRecordId} → Delete book copies (Only delete specific book by book record id)
- GET /api/book_record/isbn/{isbn} → List all book with same isbn number
- POST /api/borrowedRecords/borrow/{borrowerId}/{bookRecordId} → Borrow book
- POST /api/borrowedRecords/return/{borrowedRecordId} → Return book
- GET /api/borrowedRecords → List all borrowed records
- GET /api/borrowedRecords/borrower/{borrowerId} → List a borrower book records 
-------------------------------------------------------------------------

## ⚙️ Setup Instructions

### 1. Clone the repository
git clone https://github.com/husna7930/Library.git

## Run Locally
2. Build the Project
run → mvn clean package
run → java -jar target/library-0.0.1-SNAPSHOT.jar

-------------------------------------------------------------------------

## The application will start on:
http://localhost:8080

-------------------------------------------------------------------------


## Database Configuration (Choosen DB)

Update file path:
src/main/resources/application.properties

1. Run with PostgresSQL

spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

2. Run with H2

spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=create-drop
spring.h2.console.enabled=true

-------------------------------------------------------------------------

## Testing
run → mvn test

## Postman Test
Postman Collection folder path:
https://github.com/husna7930/Library/tree/main/docs/Postman/
File name: Demo Api.postman_collection.json

## Documentation on my API
Full Documentation folder path:
https://github.com/husna7930/Library/tree/main/docs/API%20Documentation
File name: Library System REST API Documentation.pdf

