# Smart Expense Tracker API

A REST API built with Java 21 and Spring Boot 3.4.3 for managing personal expenses.

## Features
- Add an expense (id, title, amount, category, date)
- View all expenses
- Filter expenses by category
- Search expenses by title keyword
- Calculate total expenses (overall and by category)
- Delete an expense
- Consistent JSON error responses for validation, bad UUIDs, bad date formats, and missing resources
- **Bonus**: OpenAPI/Swagger Documentation (interactive UI at `/swagger-ui/index.html`)
- **Bonus**: Search expenses with `GET /api/expenses?search={keyword}`

## Prerequisites
- Java 21 or higher (tested on Java 25)
- No additional installation required - Maven Wrapper is included

## How to Install & Run

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Abhishek720777/Smart-Expense-Tracker.git
   cd Smart-Expense-Tracker
   ```

2. **Run the application**:
   ```bash
   # Linux / macOS
   ./mvnw spring-boot:run

   # Windows (PowerShell)
   .\mvnw.cmd spring-boot:run
   ```

   The server will start on **http://localhost:8080**.

3. **Explore the API (Swagger UI)**:  
   Open http://localhost:8080/swagger-ui/index.html in your browser.

## How to Run Tests

```bash
# Linux / macOS
./mvnw clean test

# Windows (PowerShell)
.\mvnw.cmd clean test
```

Expected output: `Tests run: 15, Failures: 0, Errors: 0, Skipped: 0 - BUILD SUCCESS`

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/expenses` | Add a new expense |
| `GET` | `/api/expenses` | Get all expenses |
| `GET` | `/api/expenses?category={category}` | Filter expenses by category |
| `GET` | `/api/expenses?search={keyword}` | Search expenses by title keyword |
| `GET` | `/api/expenses/total` | Get overall total |
| `GET` | `/api/expenses/total?category={category}` | Get total by category |
| `DELETE` | `/api/expenses/{id}` | Delete an expense by ID |

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
