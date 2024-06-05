# Product Cart Service
Basic service to manage products and carts

## Pre-Requisites
* Java 21
* Docker

## To Run Locally
Run the following commands from the root of the project

`./gradlew clean bootJar` - cleans the project and builds the application jar artifact

`docker-compose build` - builds the docker image

`docker-compose up` - runs the container with the Product Cart Service

The application can now be accessed on localhost:8080, for example http://localhost:8080/cart will create a cart. For more details on usage see the OpenAPI schemas located in _**src/main/resources/schemas**_

In addition, you can find a basic postman collection with the various endpoints in the **_postman_** folder

To run the tests you can use the command `./gradlew clean test`

## Next Steps
* Move transformation logic in the facade layer into its own layer, alternatively we could use a mapping library such as **ModelMapper** or **Orika**
* Add additional endpoint to get all products to assist with adding products to the cart
* Add validations and size and format constraints on the data
* Secure the DB connection details, so they are retrieved from a secure parameter store
* Add logging for easier support and maintenance