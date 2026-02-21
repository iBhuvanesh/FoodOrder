import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login.component';
import { RestaurantListComponent } from './restaurants/restaurant-list.component';
import { CartComponent } from './cart/cart.component';
import { OrdersComponent } from './orders/orders.component';
import { PaymentLookupComponent } from './payment/payment-lookup.component';

export const appRoutes: Routes = [
  { path: '', redirectTo: 'restaurants', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'restaurants', component: RestaurantListComponent },
  { path: 'cart', component: CartComponent },
  { path: 'orders', component: OrdersComponent },
  { path: 'payments', component: PaymentLookupComponent }
];
