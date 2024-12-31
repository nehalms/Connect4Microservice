# Connect4Api

Connect4Api is a RESTful API service for implementing the game Connect Four. Built with Java and Spring Boot, this project offers game management, player handling, and real-time game state updates using WebSockets. It is designed for scalability and reliability, with features like exception handling and a modular architecture.

---

## Features

- **Game Management**: Create, join, and manage Connect Four games.
- **Player Handling**: Supports unique player registration and turn-based gameplay.
- **Exception Handling**: Custom exceptions for better error management, including game completion, invalid tokens, and duplicate players.
- **Real-Time Updates**: Implements WebSocket for real-time game status updates.
- **Modular Design**: Separates concerns into models, services, controllers, and configurations.
- **Docker Support**: Includes a `Dockerfile` for containerized deployment.

---

## Project Structure

```
├── mvnw                # Maven wrapper for build automation
├── mvnw.cmd            # Windows Maven wrapper
├── pom.xml             # Maven project configuration
├── Dockerfile          # Dockerfile for containerization
├── src/
│   ├── main/
│   │   ├── resources/                  # Configuration files and static resources
│   │   │   ├── application.properties  # Spring Boot application configuration
│   │   │   └── banner.txt              # Custom banner for the application
│   │   └── java/
│   │       └── com/example/Connect4Api/
│   │           ├── Exception/          # Custom exception classes
│   │           ├── Storage/            # Game state storage
│   │           ├── Model/              # Data models and responses
│   │           ├── Configuration/      # WebSocket and REST template configurations
│   │           ├── Controller/         # REST API controllers
│   │           ├── Service/            # Business logic and services
│   │           └── Connect4ApiApplication.java # Application entry point
└── test/
    └── java/com/example/Connect4Api/    # Unit tests
```

---

## Installation

### Prerequisites

- Java 11 or higher
- Maven 3.6+
- Docker (optional for containerization)

### Build and Run Locally

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/yourusername/Connect4Api.git
   cd Connect4Api
   ```

2. **Build the Project**:
   ```bash
   ./mvnw clean install
   ```

3. **Run the Application**:
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Access the API**:
   The application runs on `http://localhost:8080` by default.

### Run with Docker

1. **Build the Docker Image**:
   ```bash
   docker build -t connect4api .
   ```

2. **Run the Docker Container**:
   ```bash
   docker run -p 8080:8080 connect4api
   ```

---

## API Endpoints

- **`POST /games/start`**: Create a new game.
- **`POST /games/connect`**: Join an existing game.
- **`POST /games/gameplay`**: Make a move in the game.
- **`POST /games/getStatus`**: Get the current status of the game.
- **`POST /games/reset`**: Reset board and start a new game.

---

## Technologies Used

- **Framework**: Spring Boot
- **Build Tool**: Maven
- **WebSocket**: For real-time communication
- **Docker**: For containerized deployments

---

## Future Enhancements

- Add a leaderboard for tracking player performance.
- Extend support for additional board games.
- Implement advanced analytics for gameplay statistics.
