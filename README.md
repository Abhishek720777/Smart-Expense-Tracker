# Smart Expense Tracker API

A REST API built with Java 21 and Spring Boot 3.4.3 for managing personal expenses.

## What I Built
- Add an expense with title, amount, category, and date. The server generates the id.
- View all expenses.
- Filter expenses by category.
- Calculate total expenses overall or by category.
- Delete an expense by id.
- Return consistent JSON error responses for validation failures, bad UUIDs, bad date formats, missing resources, unknown routes, and unsupported HTTP methods.

## Optional Bonus Chosen
- OpenAPI/Swagger documentation with interactive UI at `/swagger-ui/index.html`.

## Prerequisites
- Java 21 or higher.
- No separate Maven installation is required because the Maven Wrapper is included.

## Install Dependencies

Dependencies are installed automatically by the Maven Wrapper when you run the project or tests.

```bash
# Linux / macOS
./mvnw test

# Windows PowerShell
.\mvnw.cmd test
```

## Run the Server

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows PowerShell
.\mvnw.cmd spring-boot:run
```

The server starts at `http://localhost:8080`.

Swagger UI is available at `http://localhost:8080/swagger-ui/index.html`.

## Run Tests

```bash
# Linux / macOS
./mvnw clean test

# Windows PowerShell
.\mvnw.cmd clean test
```

Expected output includes: `Tests run: 20, Failures: 0, Errors: 0, Skipped: 0`.

The Maven test suite lives in `src/test/java`. A top-level `tests/` directory is also included for assignment structure compatibility.

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/expenses` | Add a new expense |
| `GET` | `/api/expenses` | Get all expenses |
| `GET` | `/api/expenses?category={category}` | Filter expenses by category |
| `GET` | `/api/expenses/total` | Get overall total |
| `GET` | `/api/expenses/total?category={category}` | Get total by category |
| `DELETE` | `/api/expenses/{id}` | Delete an expense by id |

## Example Request

```bash
curl -X POST http://localhost:8080/api/expenses \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Groceries",
    "amount": 45.50,
    "category": "Food",
    "date": "2026-08-01"
  }'
```

Example validation error response:

```json
{
  "status": 400,
  "error": "Validation Failed",
  "message": "title: Title is required",
  "timestamp": "2026-08-01T14:52:31.123"
}
```
