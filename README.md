# ProjectHub

A platform where people can post project ideas and find collaborators to build them together. The goal is a real, usable product: users create accounts, publish projects they want help with, and connect with people who have the skills and interest to join them.

Planned features include project ownership and management, joining or expressing interest in projects, user profiles with skills and tags, project filtering/search, and in-app messaging between collaborators.

## Tech Stack

- **Java 21**
- **Spring Boot 4** (Web MVC)
- **Thymeleaf** + Layout Dialect (server-side templating)
- **PostgreSQL** (local server, accessed through Spring Data JPA and Spring JDBC)
- **Tailwind CSS 4** (built with npm)
- **Maven** (wrapper included)
- **Lombok**

### Prerequisites

- JDK 21 (set `JAVA_HOME` to the JDK installation and add its `bin` directory to `PATH`)
- PostgreSQL server and the `psql` command-line client (no Docker required)
- A running local PostgreSQL service listening on port 5432
- Node.js LTS and npm for editing templates or CSS
- IntelliJ IDEA or VS Code

Maven is provided through the wrapper; a separate Maven installation is not required. Run all commands below from the project root. On Windows PowerShell, use `.\mvnw.cmd`; on macOS/Linux, use `./mvnw`.

## Database setup

Before starting the application, connect to the existing `postgres` database using your PostgreSQL administrator account (commonly `postgres`):

```sh
psql -h localhost -p 5432 -U postgres -d postgres
```

Alternatively, open a query console connected to the `postgres` database in pgAdmin or your IDE. Create the application role and database once:

```sql
CREATE ROLE projecthub WITH LOGIN PASSWORD 'replace-with-a-strong-local-password';
CREATE DATABASE projecthub OWNER projecthub;
```

If the `projecthub` role already exists, skip `CREATE ROLE` and use its existing password. If the database already exists, skip `CREATE DATABASE`.

The application reads these environment variables from its run configuration or terminal:

| Variable | Default | Usage |
| --- | --- | --- |
| `DB_URL` | `jdbc:postgresql://localhost:5432/projecthub` | Change if your database host, port, or name differs. |
| `DB_USERNAME` | `projecthub` | PostgreSQL login role. |
| `DB_PASSWORD` | Empty | Set to the password for that role. |

Do not put passwords in `application.properties` or commit them to Git. A `.env` file is not automatically loaded by this project.

Schema changes are managed by Flyway migrations in `src/main/resources/db/migration` (`V2__description.sql`, etc.), applied on startup. Hibernate runs with `ddl-auto=validate`, so entities must match the migrated schema. **The PostgreSQL database itself must already exist** — Flyway only manages the tables inside it. Tests use H2 with `create-drop` and Flyway disabled.

## IntelliJ IDEA setup

1. Open the project root directory. If prompted, import `pom.xml` as a Maven project and allow Maven dependencies to download.
2. Under **File > Project Structure > Project**, select JDK 21 as the project SDK. Ensure the Maven importer and runner also use JDK 21 in **Settings > Build, Execution, Deployment > Build Tools > Maven**. Use the Maven wrapper when selecting the Maven installation.
3. In the integrated terminal, install the pinned frontend dependencies and build the CSS:
   ```sh
   npm ci
   npm run build:css
   ```
4. Open `src/main/java/com/projecthub/ProjectHubApplication.java` and run its `main` method once to create an Application run configuration. The initial run will fail if the database password has not yet been configured.
5. Open **Run > Edit Configurations**, select that Application configuration, and set:
   - **JRE:** JDK 21
   - **Working directory:** the project root
   - **Environment variables:** `DB_USERNAME=projecthub` and `DB_PASSWORD` set to your local database password. Add `DB_URL` only if overriding the default. Use the environment-variable editor to enter each variable separately.
6. Run `ProjectHubApplication` again and open `http://localhost:8080` once startup completes. A Spring Boot-specific run configuration is optional; a normal Java Application configuration works too.

If the editor reports missing Lombok-generated methods, enable annotation processing under **Settings > Build, Execution, Deployment > Compiler > Annotation Processors** and ensure Lombok support is enabled in your IDE.

## VS Code setup

1. Install **Extension Pack for Java** (Microsoft). **Spring Boot Extension Pack** (VMware) is optional for Spring-specific navigation and the Boot dashboard.
2. Open the project root folder, allow the Java extensions to import the Maven project, and use **Java: Configure Java Runtime** from the Command Palette to select JDK 21.
3. In the integrated terminal, install the pinned frontend dependencies and build the CSS:
   ```sh
   npm ci
   npm run build:css
   ```
4. Set the database credentials and start the application in that same terminal.

   **Windows PowerShell:**
   ```powershell
   $env:DB_USERNAME = 'projecthub'
   $env:DB_PASSWORD = '<your-local-database-password>'
   .\mvnw.cmd spring-boot:run
   ```

   **macOS/Linux (bash/zsh):**
   ```sh
   export DB_USERNAME=projecthub
   export DB_PASSWORD='<your-local-database-password>'
   ./mvnw spring-boot:run
   ```

   Replace the password placeholder with the password you set during database setup. These variables apply to the current terminal session and its child processes; avoid entering real credentials in a shared terminal or committing them in scripts.
5. Open `http://localhost:8080` once startup completes. Stop the application with `Ctrl+C`.

For breakpoints, use the Java extension's **Run and Debug** view to create a Java launch configuration for `com.projecthub.ProjectHubApplication`. Set its working directory to `${workspaceFolder}` and supply the same database environment variables locally. Variables set in an integrated terminal are not automatically inherited by launches started through the editor. Keep credentials out of tracked launch configurations.

## Frontend development

Run the CSS watcher in a separate terminal alongside the Spring Boot application when editing templates or styles:

```sh
npm run watch:css
```

Edit templates under `src/main/resources/views` and styles in `src/main/resources/static/css/main.css` or `tailwind.css`. Use complete Tailwind class names in templates so the scanner can detect them. Do not edit `src/main/resources/static/css/app.css` directly; it is generated and committed so the application can run without Node.js installed.

Before packaging or committing frontend changes, regenerate the CSS:

```sh
npm run build:css
```

Maven does not run the CSS build automatically.

## Troubleshooting

- **`FATAL: database "projecthub" does not exist`:** Create the database using the steps above, or set `DB_URL` to an existing database. Check that you are connecting to the intended PostgreSQL server and port.
- **Password authentication failed:** Check `DB_USERNAME` and `DB_PASSWORD` in the terminal or IDE run configuration that actually starts the application.
- **Connection refused:** Start the PostgreSQL service and verify that it is listening on the host and port in `DB_URL`.
- **`psql` is not recognized:** Add PostgreSQL's `bin` directory to `PATH`, or use pgAdmin's query tool instead.
- **Wrong Java version:** Check `java -version` and `.\mvnw.cmd -version` (Windows) or `./mvnw -version` (macOS/Linux), then correct `JAVA_HOME` or the IDE's JDK settings.

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
