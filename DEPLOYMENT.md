# ThandaFox Deployment Guide

## 1) What Is Ready
- Frontend is served by Spring Boot static files.
- API endpoints are under `/api/*` and work with same-origin in production.
- App port is environment-aware via `PORT`.
- JWT secret and expiry are environment-configurable.
- PostgreSQL driver is included for production DB usage.

## 2) Required Environment Variables
Set these in your hosting provider:

- `APP_JWT_SECRET` (required): long random value (at least 32+ chars)
- `APP_JWT_EXPIRATION_MS` (optional): default `86400000`
- `SPRING_DATASOURCE_URL` (required in production DB)
- `SPRING_DATASOURCE_USERNAME` (required in production DB)
- `SPRING_DATASOURCE_PASSWORD` (required in production DB)
- `SPRING_DATASOURCE_DRIVER_CLASS_NAME` (recommended): `org.postgresql.Driver`
- `SPRING_JPA_HIBERNATE_DDL_AUTO` (optional): `update` or `validate`
- `SPRING_H2_CONSOLE_ENABLED` (recommended): `false`

If frontend and backend are hosted on different origins, also set:
- `APP_CORS_ALLOWED_ORIGIN_PATTERNS` (comma-separated), for example:
  `https://your-frontend.com,http://localhost:*`

## 3) Deploy on Render (Recommended)
1. Push this project to GitHub.
2. In Render, create a **Web Service** from your repo.
3. Render can auto-detect `render.yaml`, or set manually:
   - Build command: `mvn clean package -DskipTests`
   - Start command: `java -jar target/thanda-fox-0.0.1-SNAPSHOT.jar`
4. Add the environment variables listed above.
5. Deploy.

## 4) Local Production-Like Test
Run this before real deployment:

```bash
mvn clean package -DskipTests
APP_JWT_SECRET="replace-with-long-secret-value" \
SPRING_H2_CONSOLE_ENABLED=false \
java -jar target/thanda-fox-0.0.1-SNAPSHOT.jar
```

Then open `http://localhost:8080` and test signup/login.

## 5) Notes
- Do not use in-memory H2 for real production data.
- Use PostgreSQL so users and orders persist across restarts.
- If you deploy frontend and backend together in this same app, CORS is generally not needed in production.
