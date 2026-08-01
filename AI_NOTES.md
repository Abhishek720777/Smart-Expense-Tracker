# AI Collaboration Notes

## 1. Which Parts Were AI-Generated vs. Written by Me

### AI-Generated Scaffolding
- I used AI assistance to bootstrap the Spring Boot project structure and initial package layout.
- The first drafts of the `Expense` model, repository interface, in-memory repository, service layer, controller, and starter tests were AI-assisted.
- AI also suggested improvements such as Swagger annotations, a global exception handler, and extra repository/controller tests.

### Written or Heavily Modified by Me
- `Expense.java`: The AI initially suggested Lombok. I removed Lombok and wrote constructors, getters, setters, and a small builder manually after validating that Lombok caused compatibility problems in my Java 25 environment.
- `pom.xml`: I corrected dependencies and build settings, including Spring Boot 3.4.3, Java 21 bytecode output, Maven compiler plugin configuration, Spring validation, Springdoc OpenAPI, and test runtime settings.
- `InMemoryExpenseRepository.java`: I changed the backing storage to `ConcurrentHashMap` for thread-safety and later made filtering null-safe.
- `ExpenseController.java`: I reviewed and adjusted the REST contract, including server-generated IDs, category filtering, totals, delete behavior, and Swagger annotations.
- `GlobalExceptionHandler.java`: I refined the AI-suggested exception handling so clients receive consistent JSON responses without leaking internal exception messages.
- Tests: I expanded the AI-generated tests into controller, service, repository, and application-context coverage.

## 2. What I Validated, Tested, or Changed and Why

- Removed Lombok because it caused JVM/tooling issues on the local Java 25 setup. The project now has no Lombok dependency and compiles to Java 21 bytecode.
- Rejected client-provided IDs on create requests so a caller cannot overwrite an existing in-memory expense by posting a chosen UUID.
- Added bean validation for required fields and positive amounts, then verified invalid title, amount, category, and date inputs return HTTP 400.
- Added a global error response DTO with `status`, `error`, `message`, and `timestamp` so malformed UUIDs, bad JSON/date input, validation failures, missing resources, unknown routes, and unsupported methods return structured JSON.
- Changed the generic 500 handler to log the internal exception and return a safe public message instead of exposing implementation details to API clients.
- Added OpenAPI/Swagger documentation as the one optional bonus chosen for the assignment.
- Verified the full test suite with `./mvnw clean test` / `.\mvnw.cmd clean test`.
- Current test coverage: 20 tests across controller, service, repository, and Spring application context tests.

## 3. AI Suggestions I Decided Not to Use and Why

- Lombok: I decided not to use it because it created compatibility problems in my environment and the explicit Java code is simple enough for this assignment.
- JSON file persistence: I did not use it because the assignment allows in-memory storage and file I/O would add extra path, parsing, and concurrency concerns without improving the core API.
- Database persistence: I did not add H2/PostgreSQL because no database is required and an in-memory repository keeps the project easy to run during automated review.
- Docker support: I did not add Docker because the assignment says to pick at most one bonus, and I chose OpenAPI/Swagger documentation.
- Monthly summary endpoint: I skipped it to keep the API focused on the required functionality plus one clearly documented bonus.
