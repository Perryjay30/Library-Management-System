# Library Management System

## Project Description
The Library Management System is an API built using Spring Boot that allows librarians to manage books, patrons, and borrowing records. The system provides RESTful endpoints for managing these entities, including adding, updating, retrieving, and deleting records.

## Requirements

### Entities
1. **Book**:
    - Attributes: ID, title, author, publication year, ISBN, etc.
2. **Patron**:
    - Attributes: ID, firstname, lastname, password, home address, phone number, email address, etc.
3. **Borrowing Record**:
    - Tracks the association between books and patrons, including borrowing and return dates.

### API Endpoints

#### Book Management Endpoints
- **GET /api/books/getAllBooks**: Retrieve a list of all books.
- **GET /api/books/getBookById/{id}**: Retrieve details of a specific book by ID.
- **POST /api/books/addBooksToLibrary**: Add a new book to the library.
- **PUT /api/books/editBookDetails/{id}**: Update an existing book's information.
- **DELETE /api/books/removeBook/{id}**: Remove a book from the library.

#### Patron Management Endpoints
- **GET /api/patrons/getAllPatrons**: Retrieve a list of all patrons.
- **GET /api/patrons/getExistingPatron/{emailAddress}**: Retrieve details of a specific patron by ID.
- **POST /api/patrons/registration**: Add a new patron to the system.
- **PUT /api/patrons/editPatronProfile/{emailAddress}**: Update an existing patron's information.
- **DELETE /api/patrons/deletePatron/{emailAddress}**: Remove a patron from the system.

#### Borrowing Endpoints
- **POST /api/borrowingRecord/borrowBook/{bookId}/patron/{emailAddress}**: Allow a patron to borrow a book.
- **PUT /api/borrowingRecord/returnBook/{bookId}/{emailAddress}**: Record the return of a borrowed book by a patron.

### Data Storage
- MySQL database was used to persist book, patron, and borrowing record details.
- Proper relationships was set up between entities (e.g., one-to-many between books and borrowing records).

### Validation and Error Handling
- Implemented input validation for API requests (e.g., validating required fields, data formats, etc.).
- Handled exceptions gracefully and return appropriate HTTP status codes and error messages.

### Optional Features (Extra Credit)

#### Security
- Implemented basic authentication or JWT-based authorization to protect the API endpoints.

#### Aspects
- Implemented logging using Aspect-Oriented Programming (AOP) to log method calls, exceptions, and performance metrics of certain operations like book additions, updates, and patron transactions.

#### Caching
- Utilized Spring's caching mechanisms to cache frequently accessed data, such as book details or patron information, to improve system performance.

### Transaction Management
- Implemented declarative transaction management using Spring's `@Transactional` annotation to ensure data integrity during critical operations.

### Testing
- Wrote unit tests to validate the functionality of API endpoints.
- Used testing frameworks like JUnit, Mockito, or SpringBootTest for testing.

### Evaluation Criteria
- **Functionality**: CRUD operations for books, patrons, and borrowing records are working correctly.
- **Code Quality**: The code has been evaluated for readability, maintainability, and adherence to best practices.
- **Error Handling**: Proper handling of edge cases and validation errors.

## How to Run the Application

1. **Clone the Repository**:
    ```bash
    git clone https://github.com/Perryjay30/Library-Management-System.git
    cd library-management-system
    ```

2. **Set Up the Database**:
    - The application is configured to use MySQL database.
    - If you want to use MySQL or PostgreSQL, update the `application.yml` file with your database configurations.

3. **Build and Run the Application**:
    ```bash
    ./mvnw clean install
    ./mvnw spring-boot:run
    ```

4. **Access the API**:
    - The API will be available at `http://localhost:2205`.

## API Endpoints

### Book Management
- **GET /api/books/getAllBooks**: Retrieve a list of all books.
- **GET /api/books/getBookById/{id}**: Retrieve details of a specific book by ID.
- **POST /api/books/addBooksToLibrary**: Add a new book to the library.
    - Example Request Body:
      ```json
      {
          "title": "The Great Gatsby",
          "author": "F. Scott Fitzgerald",
          "publicationYear": "1925",
          "isbn": "9780743273565"
      }
      ```
- **PUT /api/books/editBookDetails/{id}**: Update an existing book's information.
    - Example Request Body:
      ```json
      {
          "title": "The Great Gatsby",
          "author": "F. Scott Fitzgerald",
          "publicationYear": "1925",
          "isbn": "9780743273565"
      }
      ```
- **DELETE /api/books/removeBook/{id}**: Remove a book from the library.

### Patron Management
- **GET /api/patrons/getAllPatrons**: Retrieve a list of all patrons.
- **GET /api/patrons/getExistingPatron/{emailAddress}**: Retrieve details of a specific patron by ID.
- **POST /api/patrons/registration**: Add a new patron to the system.
    - Example Request Body:
      ```json
      {
          "firstName": "Bianca",
          "lastName": "biancajoel@gmail.com",
          "password": "Bianca@345",
          "confirmPassword": "Bianca@345"
      }
      ```
- **PUT /api/patrons/editPatronProfile/{emailAddress}**: Update an existing patron's information.
    - Example Request Body:
      ```json
      {
          "firstName": "Bianca",
          "lastName": "biancajoel@gmail.com",
          "password": "Bianca@345",
          "confirmPassword": "Bianca@345"
      }
      ```
- **DELETE /api/patrons/deletePatron/{emailAddress}**: Remove a patron from the system.

### Borrowing
- **POST /api/borrowingRecord/borrowBook/{bookId}/patron/{emailAddress}**: Allow a patron to borrow a book.
- **PUT /api/borrowingRecord/returnBook/{bookId}/{emailAddress}**: Record the return of a borrowed book by a patron.

## Example of Aspect Logging

The aspect logging will log method calls, exceptions, and performance metrics for the book, patron, and borrowing record services. The logs will be output to the console and a log file based on the logging configuration in `application.yml`.

### Sample Log Output
```
INFO 22480 --- [http-nio-2205-exec-2] c.m.c.l.logging.LoggingAspect            : Method invoked : ClassName = class com.maids.cc.librarymanagementsystem.patron.service.PatronServiceImpl: MethodName = login: methodArguments = [LoginRequest(emailAddress=adebolexsewa@gmail.com, password=Arowolo@97)]
INFO 22480 --- [http-nio-2205-exec-2] c.m.c.l.logging.LoggingAspect            : Method invoked : ClassName = class com.maids.cc.librarymanagementsystem.patron.service.PatronServiceImpl: MethodName = login: methodResponse = LoginResponse(message=Login successful!!, loginToken=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZGVib2xleHNld2FAZ21haWwuY29tIiwiaWF0IjoxNzE2NzM3NjY1LCJleHAiOjE3MTY3Mzk0NjV9.4EkXKeK18plhz8c6fZPlH-GrrrA7_j5mdCllmkZGdV8)
INFO 22480 --- [http-nio-2205-exec-2] c.m.c.l.logging.LoggingAspect            : login executed in 0ms
ERROR: An exception has been thrown: com.maids.cc.librarymanagementsystem.exception.LibraryManagementSystemException: Book not found
```

## Testing

### Running Tests
To run the tests, use the following command:
```bash
./mvnw test
```

### Example Test Cases
- **BookServiceTest**: Tests for caching and CRUD operations on books.
- **PatronServiceTest**: Tests for caching and CRUD operations on patrons.
- **BorrowingRecordServiceTest**: Tests for borrowing and returning books, and transaction management.

### Tools and Frameworks
- **JUnit**: For unit testing.
- **Mockito**: For mocking dependencies.
- **SpringBootTest**: For integration testing.

## Conclusion

The Library Management System provides a comprehensive solution for managing books, patrons, and borrowing records using Spring Boot. The implementation includes validation, error handling, caching, AOP logging, and transaction management to ensure a robust and maintainable system.