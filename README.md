# CRUD Employee

![Homepage](https://user-images.githubusercontent.com/67349235/175102934-8f73ce8b-6d32-4953-bf93-b0ea9aa97f38.png)

![UML Diagram](https://user-images.githubusercontent.com/67349235/175102037-241c1dc7-1538-4743-a2fc-4b5c2ea21b55.png)

This is a full-stack project that combines Spring Boot for the back-end and React.js for the front-end. The application utilizes a MySQL database.

## Project Description

The CRUD Employee app provides a user-friendly interface for managing employee data. The homepage displays a list of all employees, and users can perform operations such as creating new employees, updating existing employee information, and deleting employees.

## Technologies Used

### Back-end
- JDK 11
- Spring Boot
- Maven
- Embedded Tomcat
- MySQL
- Postman

### Front-end
- HTML
- CSS
- JavaScript
- Yarn
- Node.js
- React.js
- React Hooks
- Bootstrap 5
- Axios
- VS Code

## Getting Started

To run the project locally, follow these steps:

1. Clone the repository: `git clone https://github.com/jonasmachados/crud-employee.git`
2. Set up the back-end dependencies and run the Spring Boot application.
3. Set up the front-end dependencies and start the React development server.
4. Access the application in your browser at `http://localhost:3000`.

For more detailed instructions, please refer to the project documentation in the repository.

## Repository

The project repository can be found on GitHub: [https://github.com/jonasmachados/crud-employee](https://github.com/jonasmachados/crud-employee)

## AI-SDLC Orchestrator

A new standalone orchestrator module now lives in the repository to keep AI-SDLC concerns separate from the employee backend.

### Structure

- springboot-backend: existing Spring Boot API
- react-frontend: existing React UI
- ai-sdlc-orchestrator: standalone Node.js service for story-driven workflows
- stories: example stories consumed by the orchestrator
- docs: architecture and workflow documentation

### Run locally

```bash
cd ai-sdlc-orchestrator
npm start
```

### Run tests

```bash
cd ai-sdlc-orchestrator
npm test
```
