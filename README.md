# Sensor Management

## Architecture

The application is built using:

- **Java 26**
- **Spring Boot** (spring-boot-starter-integration, integration-ip, spring-boot-starter-amqp)
- **com.fasterxml.jackson** 
- **RabbitMQ** (running in Docker container)

---

## Requirements

Before running the application, start the required Docker containers:

```bash
docker compose up
```
http://localhost:15672/ is the RabbitMQ Management UI

---

## Architectural Decisions

### Sensor Type and Timestamp

I introduced a `SensorType` enum to make sensor identification more explicit and type-safe.

Additionally, I added a `timestamp` field to record the exact date and time when the sensor data is received.

---

### Constants

A dedicated constants class was created to centralize reusable values and string literals. This helps:

- Avoid code duplication
- Improve maintainability
- Keep the codebase more consistent

---

### Data Format

Based on my understanding, the incoming sensor data is **not** received in JSON format. Instead, it follows a format similar to:

```text
sensor_id=h1; value=40
```

To support both the current and future data formats, I implemented two mapping methods:

#### `mapperReceivedDataToJsonSensorData`

Converts the incoming plain-text sensor data into a JSON representation before publishing it to RabbitMQ.

Using JSON as the messaging format makes the system more extensible, allowing additional fields to be added in the future without requiring changes to the communication protocol.

#### `mapperReceivedDataJsonToSensorData`

Maps the JSON payload to the `SensorData` domain object.

This approach prepares the application for future integrations with third-party APIs or services that already communicate using JSON, minimizing future code changes.

---

### Helper Components

Helper classes were implemented as Spring `@Component` to improve:

- Testability
- Maintainability
- Dependency injection support

The `SensorThresholdConfig` was also implemented as a Spring `@Component`, since the sensor threshold values are loaded from the application's configuration (`application.properties`).