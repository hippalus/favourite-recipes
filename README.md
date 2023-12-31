# ABN AMRO Favourite Recipes

This project is a standalone Java application that allows users to manage their favorite recipes. Users can add, update,
remove, and fetch recipes through a REST API. Additionally, the application provides filtering capabilities based on
various criteria such as vegetarian dishes, number of servings, specific ingredients, and text search within the
instructions.

## Features

- **Recipe Management**: Users can perform CRUD operations on recipes, including adding, updating, removing, and
  fetching recipes.
- **Filtering**: The API supports filtering based on the following criteria:
  - Vegetarian: Retrieve only vegetarian recipes.
  - Number of Servings: Filter recipes based on the desired number of servings.
  - Specific Ingredients: Find recipes that include or exclude specific ingredients.
  - Text Search: Search for recipes based on keywords within the instructions.

## Architecture and Design Choices

This application was built using several architectural and design principles to ensure maintainability, extensibility,
and testability.

- **Spring Boot**: The application is implemented using Spring Boot, a popular Java framework for building robust and
  scalable applications.
- **Domain-Driven Design (DDD) and Clean Architecture**: The application follows DDD principles to separate concerns and
  create a loosely coupled architecture. The domain interfaces are defined separately to decouple the application from
  specific infrastructure implementations.
- **Separation of Models**: The application separates API models, domain models, and database models. This separation
  allows for flexibility and modularity, as each layer can have its own specific requirements and structures.
- **PostgreSQL**: The application persists data in a PostgreSQL database. The use of a relational database allows for
  structured storage and efficient querying of recipe data.
- **Test Containers**: Integration tests are performed using Test Containers, enabling the application to run a real
  database instance within a Docker container during testing. This approach ensures consistent and reliable test
  results.

If this application requires further scalability, it is possible to enhance the architecture by separating read and
write operations. The suggested approach involves utilizing PostgreSQL for write operations and simple read operations,
while leveraging Elasticsearch for complex search and advanced read operations.

To ensure data consistency between PostgreSQL and Elasticsearch, a Kafka message broker can be employed. One option for
replicating data between the two systems is by implementing a basic producer-consumer setup or by utilizing CDC (Change
Data Capture) functionality provided by tools like Debezium Kafka Connect.

By adopting this architecture, the application can handle a higher volume of read operations efficiently while ensuring
data synchronization between the two databases. The PostgreSQL database serves as the single source of truth for write
operations, while Elasticsearch facilitates fast and powerful search capabilities for complex queries.

It is important to note that implementing this enhanced architecture requires additional configuration and development
effort. Consider the trade-offs, including increased complexity and the need to manage multiple components, as well as
potential performance gains for read-heavy workloads.

## How to Run the Application

To run the application, please follow these steps:

1. Clone the repository and ensure that Docker is installed and running on your machine.
2. Navigate to the project directory by executing the following command in your terminal or command prompt:

```bash
cd favourite-recipes/
```

3. Run the following command to start the application using Docker Compose:

```bash
docker-compose up
```

This command will build and start the necessary containers, including the PostgreSQL database container and
favourite recipes application container, and configure the
datasource for the service connection.

Alternatively, if you prefer to run the application in your IDE:

1. Import the project into your IDE.
2. Configure the maven, JDK 17 and language level in the project structure settings to match your setup.
3. Run the `com.abnamro.recipes.TestFavouriteRecipesApplication.main` class to run the application in a test mode.

* This class internally invokes the main class `com.abnamro.recipes.FavouriteRecipesApplication.main`, which starts up the
  application.
* During the test mode, a PostgreSQL test container is automatically configured for the datasource connection.

## API Documentation

The API is thoroughly documented using OpenAPI. The documentation provides details about each endpoint, including
request and response formats, parameters, and example usage. To access the API documentation, please follow these steps:

1. Start the application by following the instructions mentioned in the previous section.
2. To access Swagger UI, open your web browser and go to [Swagger UI](http://localhost:8080/swagger-ui.html).
3. The API documentation page will be displayed, allowing you to explore the available endpoints and interact with the
   API.

## Testing

The application includes both unit tests and integration tests to ensure the correctness of the implemented
functionality. The tests cover different scenarios and edge cases to validate the behavior of the code.
To run the tests, execute the following command:

```bash
./mvnw clean verify
  ```
