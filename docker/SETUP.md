# Deploying in Docker

To deploy the backend server, the chat server and the database server in Docker containers:

1. Go to `discode-backend/` and follow the steps in `README.md`.
2. Make sure port `3306` is not occupied by MySQL: `sudo systemctl status mysql`.
3. Go to the project root directory and run `docker-compose up -d`.