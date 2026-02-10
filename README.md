# E-Commerce RESTful API 

A production-style **e-commerce REST API** built with **Java and Spring Boot**, simulating a complete online shopping experience.  
The application supports secure user authentication, cart and order management, Stripe-based checkout, and webhook-driven payment confirmation.  
All endpoints are protected with strong security rules and data is persisted using a **MySQL relational database**.

The service is deployed on **Railway** and exposes **Swagger/OpenAPI documentation** for easy API exploration and testing.

---

## Features

### Core Functionality
- User registration and login with **JWT-based authentication**
- Secure, role-protected REST endpoints
- Product catalog with categories
- Shopping cart creation and item management
- Order creation and checkout flow
- Stripe payment integration with webhook handling
- Automatic order status updates (`PENDING → PAID`)
- **OpenAPI / Swagger documentation** for all endpoints

### Security
- **Spring Security** with JWT access tokens
- Stateless authentication
- Endpoint-level authorization rules
- Requests without valid tokens are rejected
- Secure password storage using BCrypt
- Swagger UI configured to support JWT authorization

### Payments
- Stripe **Payment Intent**–based checkout
- Redirect flow to Stripe-hosted payment page
- Stripe **webhook verification** using `stripe-signature`
- Webhook event parsing and validation
- Order status updated upon successful payment confirmation

### Observability & Deployment
- Application logging using **Logback**
- Environment-based configuration
- Deployed on **Railway**

---

## Tech Stack

- **Java**, **Spring Boot**
- Spring Web (REST)
- **Spring Security**, JWT
- **Spring Data JPA (Hibernate)**
- **MySQL**
- Stripe Payments API
- Logback logging
- Maven
- Hosting: **Railway**

---

## Database Model

The application uses a relational MySQL schema with the following core tables:

- `users`
- `profile`
- `address`
- `product`
- `category`
- `cart`
- `order`

Relationships are managed using **JPA/Hibernate**, ensuring transactional integrity and consistent data modeling.

---

## High-Level Application Flow

### Authentication
1. User registers with name, email, and password
2. User logs in
3. API returns a **JWT access token**
4. Token must be included in all secured requests:
```http
    Authorization: Bearer <access_token>
```

### Shopping & Checkout
1. Authenticated user creates a cart
2. Products are added to the cart
3. User initiates checkout
4. Order is created with status `PENDING`
5. User is redirected to **Stripe Checkout**

### Payment Confirmation
1. Stripe processes the payment
2. Stripe sends a **webhook request** to the application
- Includes `stripe-signature` header
3. Application:
- Verifies webhook signature
- Parses the Stripe event
- Confirms `payment_intent.succeeded` OR `payment_intent.payment_failed` 
4. Order status is updated to `PAID` or `FAILED`

---

## API Documentation (Swagger / OpenAPI)

The application exposes an **OpenAPI-compliant Swagger UI** that documents all REST endpoints, request/response schemas, and authentication requirements.

### Swagger UI
```bash
http://localhost:8080/swagger-ui/index.html
```

### OpenAPI Specification
```bash
http://localhost:8080/v3/api-docs
```

All secured endpoints require a valid **JWT access token**, which can be supplied via the **Authorize** button in Swagger UI.

---

## Getting Started (Local Development)

### Prerequisites
- Java 17+
- Maven
- MySQL 8+
- Stripe account (test keys)

---

### Clone the Repository
```bash
git clone https://github.com/ShervinAbd92/SpringRestAPI-app.git
cd SpringRestAPI-app
```
### Database Setup

The application uses **MySQL** with **Flyway** for database migrations.

For **local development**, ensure you have MySQL running and a valid user account.  
The Flyway Maven plugin is configured to automatically create the database if it does not exist.

> ⚠️ Credentials shown below are for **local development only**.  
> Do **not** hardcode production credentials in `pom.xml`.

### Flyway Maven Plugin (example)

```xml
<plugin>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-maven-plugin</artifactId>
    <version>10.15.0</version>
    <configuration>
        <url>jdbc:mysql://localhost:3306/store?createDatabaseIfNotExist=true</url>
        <user>${DB_USERNAME}</user>
        <password>${DB_PASSWORD}</password>
        <cleanDisabled>false</cleanDisabled>
    </configuration>
</plugin>
```

Database migrations will run automatically on application startup.

### Environment Variables 

This project uses the **spring-dotenv** library  
([paulschwarz/spring-dotenv](https://github.com/paulschwarz/spring-dotenv)) to load environment variables from a `.env` file at runtime. This approach keeps sensitive configuration values outside of source control.

## Setup Steps

1. Create the following files in the project root directory:
   - `.env`
   - `.env.example`

2. Add your **actual secret values** to the `.env` file:
   ```env
   JWT_SECRET=your_jwt_secret
   STRIPE_SECRET_KEY=sk_test_...
   STRIPE_WEBHOOK_SECRET_KEY=whsec_...

3. Add only the variable names (without values) to the .env.example file:
    JWT_SECRET=
    STRIPE_SECRET_KEY=
    STRIPE_WEBHOOK_SECRET_KEY=

4. Ensure .env is listed in .gitignore to prevent secrets from being committed to the repository.

### Run the Application 
```bash
  mvn spring-boot:run
```

the API will be available at 
```arduino
  http://localhost:8080
```
    
