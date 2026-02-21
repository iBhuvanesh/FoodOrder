# FoodOrder Backend (Microservices)

Online Food Ordering backend using **Java 17**, **Spring Boot 3.5.11**, **MySQL**, and Spring Cloud microservices.

## Spring Initializr dependencies

### Common
- Spring Boot DevTools
- Lombok
- Validation
- Spring Boot Actuator
- Spring Boot Starter Test

### Per module
- **discovery-server**: Eureka Server
- **api-gateway**: Gateway, Eureka Client, OAuth2 Resource Server, Actuator
- **auth-service**: Web, Security, Data JPA, MySQL Driver, Eureka Client, Validation, Actuator
- **restaurant-service**: Web, Data JPA, MySQL Driver, Eureka Client, OpenFeign, Validation, Actuator, Springdoc OpenAPI
- **order-service**: Web, Data JPA, MySQL Driver, Eureka Client, OpenFeign, Validation, Actuator, Springdoc OpenAPI
- **notification-service**: Web, Eureka Client, Validation, Actuator
- **payment-service**: Web, Data JPA, MySQL Driver, Eureka Client, Validation, Actuator

## Implemented backend modules

### 1) Auth module (`auth-service`)
- User registration and login
- Roles: `USER`, `RESTAURANT_ADMIN`
- JWT issuance and validation in auth service
- Protected `/api/auth/me`

Endpoints:
- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/auth/me`

### 2) Restaurant module (`restaurant-service`)
- Restaurant CRUD (create + list currently)
- Menu management per restaurant
- Menu existence endpoint for order integration
- Role guard for management APIs (`RESTAURANT_ADMIN`)

Endpoints:
- `POST /api/restaurants`
- `GET /api/restaurants`
- `POST /api/restaurants/{restaurantId}/menu`
- `GET /api/restaurants/{restaurantId}/menu`
- `GET /api/menu/{menuItemId}/exists`

### 3) Cart + Order module (`order-service`)
- Cart management
- Direct order placement
- Place order from cart
- Order status tracking

Cart endpoints:
- `POST /api/cart/items`
- `GET /api/cart/{userId}`
- `DELETE /api/cart/{userId}/items/{menuItemId}`
- `DELETE /api/cart/{userId}`

Order endpoints:
- `POST /api/orders`
- `POST /api/orders/from-cart/{userId}`
- `PATCH /api/orders/{orderId}/status?status=PREPARING`
- `GET /api/orders/{orderId}`
- `GET /api/orders/user/{userId}`

### 4) Gateway + Discovery
- Eureka discovery server for service registration
- API gateway route forwarding for auth, restaurant, order, cart, notifications, and payments APIs
- Gateway JWT filter and role checks

## Local setup
1. Start MySQL
```bash
docker compose up -d
```

2. Create databases
```sql
CREATE DATABASE foodorder_auth;
CREATE DATABASE foodorder_restaurant;
CREATE DATABASE foodorder_order;
CREATE DATABASE foodorder_payment;
```

3. Start services
- `discovery-server`
- `auth-service`, `restaurant-service`, `order-service`, `notification-service`, `payment-service`
- `api-gateway`


### 5) Notification module (`notification-service`)
- Internal notification endpoint used by order-service
- Sends order placed / status update notifications (stub implementation with logs)

Endpoint:
- `POST /api/notifications`


### 6) Payment module (`payment-service`)
- Processes payments for orders (simulation with success/failure status)
- Exposes payment lookup by order

Endpoints:
- `POST /api/payments`
- `GET /api/payments/order/{orderId}`


## Frontend (Angular)
- Angular app added under `frontend/` with routes for login, restaurants, cart, orders, and payment lookup.
- Uses API Gateway base URL `http://localhost:8080` from environment config.
- Start with:
  ```bash
  cd frontend
  npm install
  npm start
  ```
