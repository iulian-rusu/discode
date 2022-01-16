#!/bin/sh

DOCKER_COMPOSE_FLAGS='-d'

if [ "$1" = "--build" ]; then
  echo "[deploy] - Rebuilding all services"
  # Build backend
  cd discode-backend
  scripts/package.sh
  cd ..

  # Building frontend
  cd discode-web
  rm -rf dist
  ng build --base-href=./
  cd ..

  # Telling docker-compose to rebuild images
  DOCKER_COMPOSE_FLAGS="$DOCKER_COMPOSE_FLAGS --build"
fi

docker-compose up $DOCKER_COMPOSE_FLAGS