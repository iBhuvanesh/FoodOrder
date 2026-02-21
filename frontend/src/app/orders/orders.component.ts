import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../core/api.service';
import { FoodOrder } from '../core/models';

@Component({
  selector: 'app-orders',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="card">
      <h2>My Orders</h2>
      <label>User ID</label>
      <input type="number" [(ngModel)]="userId" />
      <button class="btn btn-primary" (click)="load()">Load Orders</button>
    </div>

    <div class="card" *ngFor="let order of orders">
      <p><strong>Order #{{ order.id }}</strong></p>
      <p>Status: <span class="badge">{{ order.status }}</span></p>
      <p>Total: ₹{{ order.totalAmount }}</p>
    </div>
  `
})
export class OrdersComponent {
  userId = 1;
  orders: FoodOrder[] = [];

  constructor(private api: ApiService) {}

  load() {
    this.api.getUserOrders(this.userId).subscribe(res => this.orders = res);
  }
}
