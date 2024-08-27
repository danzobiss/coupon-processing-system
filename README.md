
<h1 align="center">
  <br>
  Coupon Processing System
  <br>
</h1>

<h4 align="center">A Coupon Processing API created for <a href="https://hubbrasil.com.br" target="_blank" style="color: #653AFB">HUB Brasil</a> backend challenge.</h4>

<p align="center">
  <img alt="GitHub language count" src="https://img.shields.io/github/languages/count/danzobiss/inventory-management-system?color=%23b70ac7">
  <img alt="Repository size" src="https://img.shields.io/github/repo-size/danzobiss/coupon-processing-system">
  <img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/danzobiss/coupon-processing-system?color=%23ff1c1c">
  <a href="https://github.com/danzobiss/inventory-management-system/stargazers">
    <img alt="Stargazers" src="https://img.shields.io/github/stars/danzobiss/coupon-processing-system?style=social&color=%23ff1c1c">
  </a>
</p>
<p align="center">
  <img alt="Java badge" src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white">
  <img alt="Spring badge" src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white">
  <img alt="Spring Boot badge" src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img alt="Hibernate badge" src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white">
  <img alt="Apache Maven badge" src="https://img.shields.io/badge/Apache Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white">
  <img alt="Swagger badge" src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=white">
  <img alt="RabbitMQ badge" src="https://img.shields.io/badge/Rabbit MQ-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white">
  <img alt="RabbitMQ badge" src="https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white">
  <img alt="RabbitMQ badge" src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white">
</p>

<p align="center">
  <a href="#-key-features">Key Features</a> •
  <a href="#-how-to-use">How To Use</a> •
  <a href="#-endpoint">Endpoint</a> •
  <a href="#-rabbitmq-queues">RabbitMQ Queues</a> •
  <a href="#-project-structure">Project Structure</a>
</p>

## 🔑 Key Features

* Create coupon
* Sends coupon to RabbitMQ queue
* Consumes buyer data from RabbitMQ queue
* Validates sent request body
* It uses the Adapter design pattern to adapt the entity according to the request body and vice versa
* Error handling for Request Exceptions
* Swagger UI for documentation

## ⚙ How To Use

To clone and run this application, you'll need [Git](https://git-scm.com), [Docker](https://www.docker.com/products/docker-desktop/) and [Apache Maven](https://maven.apache.org/download.cgi) (which comes with [mvn](https://mvnrepository.com)) installed on your computer. From your command line:

```bash
# Clone this repository
$ git clone https://github.com/danzobiss/coupon-processing-system.git

# Go into the repository
$ cd coupon-processing-system

# Install dependencies
$ mvn clean install

# Run the app
$ mvn spring-boot:run

# The application will be started at [http://localhost:8080]
# The Rabbit MQ interface will be started at [http://localhost:15672] with username "guest" and password "guest"

# The PostgreSQL connection URL will be [jdbc:postgresql://localhost:5432/coupon_processing_system]
# with user "postgres" and password "admin"
```

> **Note**
> If you're using Linux Bash for Windows, [see this guide](https://www.howtogeek.com/261575/how-to-run-graphical-linux-desktop-applications-from-windows-10s-bash-shell/) or use `maven` from the command prompt.


## 📡 Endpoint

### Coupons
```http
// Creates coupon
POST /api/coupons
```


## 🐇 RabbitMQ Queues

### buyer-data-sub
This queue is consumed by the application and updates the coupon data with the buyer data.
```json
{
  "couponId": 1,
  "buyerName": "Lucas Souza",
  "buyerBirthDate": "1994-03-29",
  "buyerDocument": "419.438.578.98"
}
```

### coupon-validated-pub
This queue is sent by the application after validating the coupon
```json
{
  "couponId": 1,
  "code44": "75142018464820341008386582266732651087527208",
  "purchaseDate": "2024-08-26",
  "totalValue": 7.5,
  "companyDocument": "48296726000140",
  "state": "SP",
  "products": [
    {
      "productId": 2,
      "name": "Caneta Bic Azul",
      "ean": "1234567890123",
      "unitaryPrice": 1.0,
      "quantity": 3
    },
    {
      "productId": 3,
      "name": "Caderno Espiral 96 folhas",
      "ean": "0987654321098",
      "unitaryPrice": 4.5,
      "quantity": 1
    }
  ],
  "buyer": null
}
```


## 🗂️ Project Structure

The project is structured as follows:

```output
src/
└── main/
    ├── java/
    │   └── com/
    │       └── danzobiss/
    │           ├── client/ # Contains the external API client classes
    │           ├── config/ # Contains the project configuration classes
    │           ├── controller/ # Contains the controller classes (REST endpoints)
    │           ├── dto/ # Contains DTO (Data Transfer Objects) classes for request
    │           ├── entity/ # Contains JPA entities
    │           ├── exception/ # Contains custom exception classes
    │           ├── pubsub/ # Contains the pub/sub queue classes
    │           ├── repository/ # Contains the repository interfaces
    │           └── service/ # Contains the service classes
    └── resources/
        └── application.properties # Application properties file
```
