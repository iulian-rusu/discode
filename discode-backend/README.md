# Discode Backend

## Running the server
1. Create a new databse in MySQL and connect to it.
2. Run the `sql/create_database.sql` script (using a database user with all privileges).
3. Create a `.env` file in the root folder of the project (`discode-backend/.env`) and add the following lines:
   1. `DISCODE_DB_NAME=<database-name>`
   2. `DISCODE_DB_USER_NAME=<user-name>` (for the user with read/write privileges only)
   3. `DISCODE_DB_USER_PASSWORD=<user-password>`
    * Don't forget to add `.env` to `.gitignore`
4. In IntelliJ, go to `Run Configurations` -> `EnvFile` and add the `.env` file.
5. Run the server.

## Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.5.6/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.5.6/maven-plugin/reference/html/#build-image)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.5.6/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [JDBC API](https://docs.spring.io/spring-boot/docs/2.5.6/reference/htmlsingle/#boot-features-sql)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.5.6/reference/htmlsingle/#boot-features-developing-web-applications)