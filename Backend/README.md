# GutHub-Backend

# Table of Contents

* [Prerequisites](#prerequisites)
* [Start Development](#start-development)
    * [Define Configuration](#define-configuration)
    * [Start the Database](#start-the-database)
    * [Start the Server](#start-the-server)
    * [Stop the Database](#stop-the-database-when-you-are-finished)
* [Run Tests](#run-tests)
* [Problems](#problems)

## Prerequisites

[docker_desktop]: https://www.docker.com/products/docker-desktop/

[java_download]: https://www.oracle.com/java/technologies/downloads/

[maven_download]: https://maven.apache.org/download.cgi

* [Docker Desktop][docker_desktop]
* [Java 23][java_download]
* [Maven 3.9.8][maven_download]

Make sure to update your environment variables or PATH according to the instructions linked above,
to be able to execute all needed commands.

You can use other docker environments besides Docker Desktop.

## Start Development

In this section I will explain how you can set up the development environment and run the application.

### Define Configuration

You need to provide a development configuration (application-dev.properties).

To see what values your configuration needs to define take a look at the sample.

```
/src/main/resources/application-dev.properties.sample
```

If you just want to start developing with the default config rename the sample file to:

```
/src/main/resources/application-dev.properties
```

### Start the Database

Run the following command in the root directory of the project

```
docker compose up -d
```

This will start a postgres db which will listen on port 5432.

### Start the Server

Run the following command in the root directory of the project

```
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Stop the Database (when you are finished)

Run the following command in the root directory of the project

```
docker compose down
```

## Run Tests

### Run Unit Tests

To run the unit tests execute the following command in the root directory of the project

```
mvn test -PUnit
```

### Run Integration Tests

To run the integration tests execute the following command in the root directory of the project

```
mvn test -PIntegration
```

The integration tests use test-containers so make sure to have an active docker environment available.
(e.g. Docker Desktop)

## Problems

No known problems yet.
