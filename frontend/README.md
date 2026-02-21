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


Notes:
- Login/Register stores JWT, role, and userId in local storage for cart/order APIs.
