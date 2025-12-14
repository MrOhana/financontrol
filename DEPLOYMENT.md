# Deployment Guide

## Docker (Local)
To build and run the application locally using Docker:

```bash
# Build the image
docker build -t financontrol .

# Run the container (with env vars)
docker run -p 8080:8080 \
  -e FINANCONTROL_DB_URL="jdbc:postgresql://host.docker.internal:5432/financontrol" \
  -e FINANCONTROL_DB_USERNAME="postgres" \
  -e FINANCONTROL_DB_PASSWORD="yourpassword" \
  financontrol
```

## Google Cloud Run

### 1. Environment Variables
You need to provide the following variables securely. In Cloud Run, you can set them via the `--set-env-vars` flag or a `.env` file (if using updated CLI), or preferably via **Secret Manager**.

**Required Variables:**
- `FINANCONTROL_DB_URL`: JDBC URL (e.g., `jdbc:postgresql://34.12.34.56:5432/financontrol`)
- `FINANCONTROL_DB_USERNAME`: Database user
- `FINANCONTROL_DB_PASSWORD`: Database password
- `FINANCONTROL_MAIL_HOST`: SMTP Host (e.g., `smtp.gmail.com`)
- `FINANCONTROL_MAIL_PORT`: SMTP Port (e.g., `587`)
- `FINANCONTROL_MAIL_USERNAME`: SMTP User
- `FINANCONTROL_MAIL_PASSWORD`: SMTP Password
- `FINANCONTROL_BASE_URL`: Public URL of your app (e.g., `https://financontrol-xyz.a.run.app`)

### 2. Deploy Command
Using the `gcloud` CLI:

```bash
gcloud run deploy financontrol \
  --source . \
  --region us-central1 \
  --allow-unauthenticated \
  --set-env-vars="FINANCONTROL_DB_URL=jdbc:postgresql://IP:PORT/DB,FINANCONTROL_DB_USERNAME=user,FINANCONTROL_DB_PASSWORD=pass"
```

*Note: For sensitive data like passwords, relying on Secret Manager (`--set-secrets`) is recommended for production.*
