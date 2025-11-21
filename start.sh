#!/bin/sh
set -e

# Ejecutar migraciones de base de datos
echo "Running database migrations..."
alembic upgrade head

# Iniciar la aplicación
echo "Starting application..."
# Usamos exec para que uvicorn reciba las señales del sistema (como SIGTERM) correctamente
exec uvicorn app.main:app --host 0.0.0.0 --port ${PORT:-8000}
