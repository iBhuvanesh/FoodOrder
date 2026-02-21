import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login.component';
import { RestaurantListComponent } from './restaurants/restaurant-list.component';
import { CartComponent } from './cart/cart.component';
import { OrdersComponent } from './orders/orders.component';
import { PaymentLookupComponent } from './payment/payment-lookup.component';
import { AdminDashboardComponent } from './admin/admin-dashboard.component';
import { authGuard } from './core/auth.guard';
import { roleGuard } from './core/role.guard';

export const appRoutes: Routes = [
  { path: '', redirectTo: 'restaurants', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'restaurants', component: RestaurantListComponent },
  { path: 'cart', component: CartComponent, canActivate: [authGuard] },
  { path: 'orders', component: OrdersComponent, canActivate: [authGuard] },
  { path: 'payments', component: PaymentLookupComponent, canActivate: [authGuard] },
  { path: 'admin', component: AdminDashboardComponent, canActivate: [authGuard, roleGuard], data: { roles: ['RESTAURANT_ADMIN'] } }
];
