# FoodOrder Angular Frontend

## Run

```bash
cd frontend
npm install
npm start
```

App runs on `http://localhost:4200` and calls API Gateway at `http://localhost:8080`.

## Implemented pages
- Login/Register
- Restaurant browsing + menu listing + add to cart
- Cart management + place order from cart
- User orders list
- Payment lookup by order id
- Admin Console (restaurant/menu management for RESTAURANT_ADMIN)


Notes:
- Login/Register stores JWT, role, and userId in local storage for cart/order APIs.

- Route guards enabled for authenticated routes and role-based admin route.


## Docker
- Frontend image is built via `frontend/Dockerfile` and served by Nginx on port 80 inside container.
- In full compose profile, frontend is available at `http://localhost:4200`.
