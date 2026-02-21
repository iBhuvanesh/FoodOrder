import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../core/api.service';
import { AuthService } from '../core/auth.service';
import { MenuItem, Restaurant } from '../core/models';

@Component({
  selector: 'app-restaurant-list',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="card">
      <h2>Restaurants & Menu</h2>
      <div class="grid">
        <div class="card" *ngFor="let r of restaurants">
          <h3>{{ r.name }}</h3>
          <p>{{ r.cuisine }}</p>
          <button class="btn btn-primary" (click)="loadMenu(r.id)">View Menu</button>
        </div>
      </div>
    </div>

    <div class="card" *ngIf="selectedRestaurantId">
      <h3>Menu Items</h3>
      <div class="card" *ngFor="let item of menuItems">
        <strong>{{ item.name }}</strong> - ₹{{ item.price }}
        <p>{{ item.description }}</p>
        <button class="btn btn-primary" (click)="add(item)">Add to Cart</button>
      </div>
      <p>{{ info }}</p>
    </div>
  `
})
export class RestaurantListComponent implements OnInit {
  restaurants: Restaurant[] = [];
  menuItems: MenuItem[] = [];
  selectedRestaurantId: number | null = null;
  info = '';

  constructor(private api: ApiService, private auth: AuthService) {}

  ngOnInit(): void {
    this.api.getRestaurants().subscribe(res => this.restaurants = res);
  }

  loadMenu(restaurantId: number) {
    this.selectedRestaurantId = restaurantId;
    this.api.getMenu(restaurantId).subscribe(res => this.menuItems = res);
  }

  add(item: MenuItem) {
    const userId = this.auth.userId() ?? 1;
    if (!this.selectedRestaurantId) return;
    this.api.addToCart({
      userId,
      restaurantId: this.selectedRestaurantId,
      menuItemId: item.id,
      quantity: 1,
      unitPrice: item.price
    }).subscribe({
      next: () => this.info = 'Added to cart',
      error: () => this.info = 'Failed to add item'
    });
  }
}
