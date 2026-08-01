# Test Suite

This project uses Maven's standard Java test layout. The executable test suite is located in:

- `tests/src/test/java/com/example/smartexpensetracker/controller/ExpenseControllerTest.java`
- `tests/src/test/java/com/example/smartexpensetracker/service/ExpenseServiceTest.java`
- `tests/src/test/java/com/example/smartexpensetracker/repository/InMemoryExpenseRepositoryTest.java`
- `tests/src/test/java/com/example/smartexpensetracker/SmartExpenseTrackerApplicationTests.java`

Run all tests from the repository root with:

```bash
# Linux / macOS
./mvnw clean test

# Windows PowerShell
.\mvnw.cmd clean test
```

Expected result: 20 tests, 0 failures, 0 errors.
