#!/bin/sh

echo "Waiting for MySQL to be ready..."

while ! mysqladmin ping -h"mysql" --silent; do
  sleep 2
done

echo "MySQL is up - starting the app"
exec java -jar app.jar