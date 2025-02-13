# E-Commerce Product & Inventory Management System


### Overview
This project is a **subsystem of an E-Commerce platform** designed to efficiently manage products and their inventories. Built using **Spring Boot WebFlux**, this microservices-based architecture ensures **reactive, scalable, and high-performance** operations.

The system consists of two core services:

**1. Product Service** – Handles CRUD operations for products, including adding, updating, retrieving, and deleting products.

**2. Product Inventory Service** – Manages inventory levels, stock status, and availability, ensuring accurate product stock tracking.


> ### Tech Stack
> - **Backend:** Java, Spring Boot, WebFlux
> - **Database:** MongoDB
> - **API Communication:** WebClient (Reactive)
> - **Testing:** JUnit, Mockito
> - **Version Control:** Git, GitHub
> - **Cache Mechanism:** Redis
> - **API Documentation:** Swagger
### Features
✅ **Product Management** – Create, Read, Update, and Delete (CRUD) operations for products.  
✅ **Inventory Tracking** – Maintain stock count and status for each product.  
✅ **Microservices Architecture** – Independent, scalable, and loosely coupled services.  
✅ **Reactive Programming** – High-performance, non-blocking request handling using WebFlux.  
✅ **API Integration** – Seamless communication between services using WebClient.  

### API Endpoints
#### Product Service
- POST /api/products – Add a new product
- GET /api/products/{id} – Get product details
- PUT /api/products/{id} – Update product information
- DELETE /api/products/{id} – Remove a product
#### Product Inventory Service
- POST /api/inventory – Add inventory for a product
- GET /api/inventory/{id} – Retrieve inventory details
- PUT /api/inventory/{id} – Update stock count
- DELETE /api/inventory/{id} – Remove inventory record
  
### Prerequisites
- JDK 17 or above
- Gradle
- MongoDB
- Redis 

> ### Installation & Setup
#### 1. Clone the repository:

```sh 
git clone https://github.com/arman-malik9/product-management.git
```
#### 2. Configure MongoDB and Redis in the application
- Update **.properties file.**  
### 3. Run Inventory Service:
```sh
gradlew clean build
gradlew bootRun
```
### 4. Run Product Service:
```sh
gradlew clean build
gradlew bootRun
```

### Contributing
Contributions are welcome! Feel free to open an issue or submit a pull request.
