#!/bin/sh

set -ae
. ./.env
set +a

TARGET_PACKAGE=target/backend-1.0.0.war

if [ "$1" = "--clean" ]; then
  echo "[deploy] - Running mvn clean"
  ./mvnw clean
fi

if [ ! -f $TARGET_PACKAGE ]; then
  echo "[deploy] - Running mvn package"
  ./mvnw package
else
  echo "[deploy] - Found package: $TARGET_PACKAGE"
fi

java -jar $TARGET_PACKAGE
