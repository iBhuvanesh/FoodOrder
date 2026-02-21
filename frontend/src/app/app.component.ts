import { Component, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterOutlet } from '@angular/router';
import { AuthService } from './core/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink],
  template: `
    <nav class="nav">
      <strong>FoodOrder</strong>
      <a routerLink="/restaurants">Restaurants</a>
      <a routerLink="/cart">Cart</a>
      <a routerLink="/orders">Orders</a>
      <a routerLink="/payments">Payments</a>
      <a routerLink="/login">Login</a>
      <span class="badge" *ngIf="role()">Role: {{ role() }}</span>
      <button class="btn btn-muted" *ngIf="role()" (click)="logout()">Logout</button>
    </nav>
    <div class="container">
      <router-outlet></router-outlet>
    </div>
  `
})
export class AppComponent {
  role = computed(() => this.authService.role());

  constructor(private authService: AuthService) {}

  logout() {
    this.authService.logout();
  }
}
