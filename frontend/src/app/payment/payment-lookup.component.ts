import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService } from '../core/api.service';
import { PaymentResponse } from '../core/models';

@Component({
  selector: 'app-payment-lookup',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="card">
      <h2>Payment Lookup</h2>
      <label>Order ID</label>
      <input type="number" [(ngModel)]="orderId" />
      <button class="btn btn-primary" (click)="lookup()">Check Payment</button>
      <p *ngIf="error" style="color:#dc2626">{{ error }}</p>
    </div>

    <div class="card" *ngIf="payment">
      <p>Payment ID: {{ payment.paymentId }}</p>
      <p>Order ID: {{ payment.orderId }}</p>
      <p>Status: <span class="badge">{{ payment.status }}</span></p>
      <p>{{ payment.message }}</p>
    </div>
  `
})
export class PaymentLookupComponent {
  orderId = 1;
  payment: PaymentResponse | null = null;
  error = '';

  constructor(private api: ApiService) {}

  lookup() {
    this.error = '';
    this.api.getPaymentByOrder(this.orderId).subscribe({
      next: res => this.payment = res,
      error: () => {
        this.payment = null;
        this.error = 'Payment not found';
      }
    });
  }
}
