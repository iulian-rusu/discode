#!/bin/sh

set -ae
. ./.env
set +a

TARGET_PACKAGE=target/backend-1.0.0.war

if [ -f $TARGET_PACKAGE ]; then
  echo "[deploy] - Found package: $TARGET_PACKAGE"
else
  echo "[deploy] - No package found, running mvn package first"
  ./mvnw package
fi

java -jar $TARGET_PACKAGE