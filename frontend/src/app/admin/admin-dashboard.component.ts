import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../core/api.service';

@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="card">
      <h2>Restaurant Admin Console</h2>
      <p>Create restaurants and menu items.</p>
    </div>

    <div class="card">
      <h3>Create Restaurant</h3>
      <label>Name</label>
      <input [(ngModel)]="restaurantName" />
      <label>Cuisine</label>
      <input [(ngModel)]="cuisine" />
      <button class="btn btn-primary" (click)="createRestaurant()">Create</button>
    </div>

    <div class="card">
      <h3>Add Menu Item</h3>
      <label>Restaurant ID</label>
      <input type="number" [(ngModel)]="restaurantId" />
      <label>Name</label>
      <input [(ngModel)]="menuName" />
      <label>Description</label>
      <input [(ngModel)]="description" />
      <label>Price</label>
      <input type="number" [(ngModel)]="price" />
      <button class="btn btn-primary" (click)="addMenuItem()">Add Item</button>
    </div>

    <p>{{ message }}</p>
  `
})
export class AdminDashboardComponent {
  restaurantName = '';
  cuisine = '';
  restaurantId = 1;
  menuName = '';
  description = '';
  price = 100;
  message = '';

  constructor(private api: ApiService) {}

  createRestaurant() {
    this.api.createRestaurant({ name: this.restaurantName, cuisine: this.cuisine }).subscribe({
      next: (res) => this.message = `Restaurant created with id ${res.id}`,
      error: () => this.message = 'Failed to create restaurant'
    });
  }

  addMenuItem() {
    this.api.addMenuItem(this.restaurantId, { name: this.menuName, description: this.description, price: this.price }).subscribe({
      next: () => this.message = 'Menu item added',
      error: () => this.message = 'Failed to add menu item'
    });
  }
}
