# URL Shortener API

A high-performance URL shortener built with Spring Boot, featuring monitoring with Prometheus and Grafana.

## Features

- ✅ **React Frontend**: Modern, responsive UI built with Vite & React
- ✅ **URL Shortening**: Convert long URLs to short codes using Base62 encoding
- ✅ **Fast Redirects**: Cached lookups for optimal performance
- ✅ **H2 Database**: In-memory database for quick development
- ✅ **Swagger UI**: Interactive API documentation
- ✅ **Monitoring**: Prometheus metrics + Grafana dashboards
- ✅ **Docker Support**: Fully containerized application
- ✅ **Unit Tests**: Comprehensive test coverage with JUnit & Mockito

## Quick Start

### Prerequisites

- Docker & Docker Compose
- Java 17+ (for local development)
- Maven 3.8+ (for local development)

### Running with Docker Compose

1. **Build and start all services:**
```bash
docker-compose up --build
```

2. **Access the services:**
   - **Frontend**: http://localhost
   - **API**: http://localhost:8080
   - **Swagger UI**: http://localhost:8080/swagger-ui.html
   - **H2 Console**: http://localhost:8080/h2-console
   - **Prometheus**: http://localhost:9090
   - **Grafana**: http://localhost:3000 (admin/admin)

3. **Stop all services:**
```bash
docker-compose down
```

### Running Locally (without Docker)

1. **Build the project:**
```bash
mvn clean package
```

2. **Run the application:**
```bash
mvn spring-boot:run
```

### Running Frontend Locally

1. **Navigate to the frontend directory:**
```bash
cd frontend
```

2. **Install dependencies:**
```bash
npm install
```

3. **Run the development server:**
```bash
npm run dev
```

## API Endpoints

### Shorten URL
```bash
POST /api/shorten
Content-Type: application/json

{
  "url": "https://www.example.com/very/long/url"
}

Response:
{
  "shortCode": "Ab3Xy",
  "shortUrl": "http://localhost:8080/Ab3Xy"
}
```

### Redirect to Original URL
```bash
GET /{shortCode}

Example: http://localhost:8080/Ab3Xy
→ Redirects to original URL
```

## Monitoring Setup

### Grafana Dashboard Configuration

1. **Login to Grafana** at http://localhost:3000 (admin/admin)

2. **Add Prometheus as Data Source:**
   - Go to Configuration → Data Sources
   - Click "Add data source"
   - Select "Prometheus"
   - URL: `http://prometheus:9090`
   - Click "Save & Test"

3. **Import Dashboard:**
   - Go to Dashboards → Import
   - Use Dashboard ID: `11378` (JVM Micrometer)
   - Select Prometheus data source
   - Click "Import"

### Key Metrics Available

- **HTTP Request Rate**: `rate(http_server_requests_seconds_count[1m])`
- **Response Times**: `http_server_requests_seconds`
- **JVM Memory**: `jvm_memory_used_bytes`
- **Cache Hit Rate**: `cache_gets_total`

## Architecture

```
┌─────────────┐
│   Browser   │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│  Frontend   │
│  (React)    │
└──────┬──────┘
       │
       ▼
┌─────────────────────────────┐
│  URL Shortener Controller   │
└──────────┬──────────────────┘
           │
           ▼
┌─────────────────────────────┐
│  URL Shortener Service      │
│  - Base62 Encoding          │
│  - Caffeine Cache           │
└──────────┬──────────────────┘
           │
           ▼
┌─────────────────────────────┐
│  H2 Database (In-Memory)    │
└─────────────────────────────┘
```

## Testing

Run all tests:
```bash
mvn test
```

Run with coverage:
```bash
mvn test jacoco:report
```

## Configuration

Key configuration in `application.properties`:

- **Database**: H2 in-memory
- **Cache**: Caffeine (500 entries, 10min TTL)
- **Actuator**: Health, Info, Prometheus endpoints
- **Logging**: DEBUG level for application

## Docker Images

- **App**: Multi-stage build (Maven + OpenJDK 17)
- **Prometheus**: `prom/prometheus:latest`
- **Grafana**: `grafana/grafana:latest`

## License

MIT License
