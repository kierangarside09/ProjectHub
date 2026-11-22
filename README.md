# ProjectHub

A platform where people can post projects and find collaborators to join and build them together.

## Tech Stack

- **Java 21**
- **Spring Boot 4** (Web MVC)
- **Thymeleaf** + Layout Dialect (server-side templating)
- **PostgreSQL** (local server, queried through Spring JDBC)
- **Maven** (wrapper included)
- **Lombok**

### Prerequisites

- JDK 21+
- PostgreSQL server and the `psql` command-line client (no Docker required)
- A running local PostgreSQL service listening on port 5432

## Project Structure

```
src/main/java/com/projecthub/       # Java source
  controllers/                      # MVC controllers
src/main/resources/
  views/                            # Thymeleaf templates
  static/                           # CSS and other static assets
  application.properties            # App configuration
```

## Testing

```bash
./mvnw test
```

## Notes

- `target/` is build output and is gitignored — don't commit it.
- Database passwords are supplied through `DB_PASSWORD`, not stored in the repository.
