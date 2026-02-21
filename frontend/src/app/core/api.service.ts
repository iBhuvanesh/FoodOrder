import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Cart, FoodOrder, MenuItem, PaymentResponse, Restaurant } from './models';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private readonly base = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  getRestaurants() {
    return this.http.get<Restaurant[]>(`${this.base}/api/restaurants`);
  }

  createRestaurant(payload: { name: string; cuisine: string }) {
    return this.http.post<Restaurant>(`${this.base}/api/restaurants`, payload);
  }

  addMenuItem(restaurantId: number, payload: { name: string; description: string; price: number }) {
    return this.http.post<MenuItem>(`${this.base}/api/restaurants/${restaurantId}/menu`, payload);
  }

  getMenu(restaurantId: number) {
    return this.http.get<MenuItem[]>(`${this.base}/api/restaurants/${restaurantId}/menu`);
  }

  addToCart(payload: { userId: number; restaurantId: number; menuItemId: number; quantity: number; unitPrice: number }) {
    return this.http.post<Cart>(`${this.base}/api/cart/items`, payload);
  }

  getCart(userId: number) {
    return this.http.get<Cart>(`${this.base}/api/cart/${userId}`);
  }

  removeCartItem(userId: number, menuItemId: number) {
    return this.http.delete(`${this.base}/api/cart/${userId}/items/${menuItemId}`);
  }

  placeOrderFromCart(userId: number) {
    return this.http.post<FoodOrder>(`${this.base}/api/orders/from-cart/${userId}`, {});
  }

  getUserOrders(userId: number) {
    return this.http.get<FoodOrder[]>(`${this.base}/api/orders/user/${userId}`);
  }

  getPaymentByOrder(orderId: number) {
    return this.http.get<PaymentResponse>(`${this.base}/api/payments/order/${orderId}`);
  }
}
