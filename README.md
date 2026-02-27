# My WebApp Project

This is a simple Java web application demonstrating a RESTful to-do list backed by **SQLite**.
The app runs on **Tomcat 11** and uses the **Jakarta Servlet API** along with minimal frontend code powered by Bootstrap.

## Features

- **CRUD operations** for tasks via `/api/tasks` endpoint
- SQLite database (file `todo.db` in working directory)
- Responsive frontend with Bootstrap and icons
- Supports JSON and URL-encoded POSTs

## Prerequisites

- Java 11 or newer (JDK 21 tested)
- Apache Maven 3.9+
- Apache Tomcat 11 (or any Jakarta EE 10 compatible container)

## Building

```sh
cd path/to/MyWebAppProject
mvn clean package
```

This will compile the code and produce `target/my-webapp-project.war`.

## Deploying to Tomcat

1. Stop Tomcat if it is running.
2. Remove any existing exploded directory or WAR for the application from `TOMCAT_HOME/webapps`.

   ```sh
   rm -rf $CATALINA_HOME/webapps/my-webapp-project*
   ```

3. Copy the newly built WAR:

   ```sh
   cp target/my-webapp-project.war $CATALINA_HOME/webapps/
   ```

4. Start (or restart) Tomcat.

The app will be available at `http://localhost:8080/my-webapp-project/`.

## Using the API

### List tasks
```
curl http://localhost:8080/my-webapp-project/api/tasks
```

### Create task
```
curl -X POST -H "Content-Type: application/json" \
     -d '{"title":"Buy milk","completed":false}' \
     http://localhost:8080/my-webapp-project/api/tasks
```

Or with form data:
```
curl -X POST -H "Content-Type: application/x-www-form-urlencoded" \
     -d "title=Test+task" \
     http://localhost:8080/my-webapp-project/api/tasks
```

### Update task
```
curl -X PUT "http://localhost:8080/my-webapp-project/api/tasks/1?completed=true"
```

or with JSON body:
```
curl -X PUT -H "Content-Type: application/json" \
     -d '{"title":"Updated","completed":true}' \
     http://localhost:8080/my-webapp-project/api/tasks/1
```

### Delete task
```
curl -X DELETE http://localhost:8080/my-webapp-project/api/tasks/1
```

## Frontend

Open a browser at `http://localhost:8080/my-webapp-project/` to view the professional Bootstrap‑based UI.
All interactions use the REST API above.

## Development

- Source is under `src/main/java/com/mywebapp/todo`.
- Static resources are in `src/main/webapp`.
- To extend the app, modify code and repeat the build & deploy steps.

## Notes

- The SQLite JDBC driver is packaged in `WEB-INF/lib` and loaded explicitly at startup.
- The servlet uses Jakarta (`jakarta.servlet`) packages to be compatible with Tomcat 11.
- Database file `todo.db` will be created in the working directory of Tomcat (usually `TOMCAT_HOME/bin`).

Enjoy! Feel free to fork and enhance the project.
