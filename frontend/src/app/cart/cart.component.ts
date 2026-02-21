import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../core/api.service';
import { AuthService } from '../core/auth.service';
import { Cart } from '../core/models';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="card">
      <h2>Cart</h2>
      <label>User ID</label>
      <input type="number" [(ngModel)]="userId" />
      <div style="display:flex; gap:8px; margin-top:8px;">
        <button class="btn btn-primary" (click)="loadCart()">Load Cart</button>
        <button class="btn btn-primary" (click)="placeOrder()">Place Order from Cart</button>
      </div>
      <p>{{ info }}</p>
    </div>

    <div class="card" *ngIf="cart">
      <h3>Items</h3>
      <div class="card" *ngFor="let item of cart.items">
        <p>Menu Item: {{ item.menuItemId }} | Qty: {{ item.quantity }} | ₹{{ item.unitPrice }}</p>
        <button class="btn btn-danger" (click)="remove(item.menuItemId)">Remove</button>
      </div>
    </div>
  `
})
export class CartComponent {
  userId = Number(localStorage.getItem('userId') || 1);
  cart: Cart | null = null;
  info = '';

  constructor(private api: ApiService, private auth: AuthService) {
    this.userId = this.auth.userId() ?? this.userId;
  }

  loadCart() {
    this.api.getCart(this.userId).subscribe({
      next: res => this.cart = res,
      error: () => this.info = 'Unable to load cart'
    });
  }

  remove(menuItemId: number) {
    this.api.removeCartItem(this.userId, menuItemId).subscribe({
      next: () => {
        this.info = 'Item removed';
        this.loadCart();
      },
      error: () => this.info = 'Remove failed'
    });
  }

  placeOrder() {
    this.api.placeOrderFromCart(this.userId).subscribe({
      next: order => {
        this.info = `Order placed: #${order.id}`;
        this.loadCart();
      },
      error: () => this.info = 'Order placement failed'
    });
  }
}
