# Deploying in Docker

The following services can be deployed in Docker containers:
* The MySQL database server
* The Spring Boot backend server
* The Node.js chat notification server
* The Nginx server for the Angular application

Steps:
1. Go to `discode-backend/` and follow the steps in `README.md`.
2. Go to `discode-web/` and run `ng build --base-href=./`.
3. Go to the project's root directory and run `docker-compose up -d`.