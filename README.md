![Logo](assets/logo.png)

*Like Discord, but you can paste and run code in the chat*
---

# About
This is a Discord clone that allows sending code segments in the chat, as well as executing them with the language of your choice.

# Architecture

The project has the following components:
* A **MySQL** database server, running on port `3435` (in Docker) or `3306` (locally).
* A **Spring Boot** backend server written in Kotlin.
* A **NodeJS** server that uses WebSockets to notify users.
* An **Angular** application deployed on Nginx that serves as the main web frontend.
* A  **QT** desktop application written in C++ that is used for administration.

The project runs on 3 servers and uses a MySQL database, as well as a desktop C++ application for administration. The main frontend application is written in Angular and served by an Nginx server deployed in Docker.

![Arch](assets/diagrams/architecture-docker.png)

# Deployment
The project can be deployed to Docker by following the steps [here](docker/README.md).