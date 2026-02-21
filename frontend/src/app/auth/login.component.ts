import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../core/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="card">
      <h2>Login / Register</h2>
      <label>Email</label>
      <input [(ngModel)]="email" placeholder="user@example.com" />
      <label>Password</label>
      <input [(ngModel)]="password" type="password" placeholder="******" />
      <label>Role</label>
      <select [(ngModel)]="role">
        <option value="USER">USER</option>
        <option value="RESTAURANT_ADMIN">RESTAURANT_ADMIN</option>
      </select>
      <div style="display:flex; gap:8px; margin-top:8px;">
        <button class="btn btn-primary" (click)="onLogin()">Login</button>
        <button class="btn btn-muted" (click)="onRegister()">Register</button>
      </div>
      <p *ngIf="message">{{ message }}</p>
    </div>
  `
})
export class LoginComponent {
  email = '';
  password = '';
  role = 'USER';
  message = '';

  constructor(private auth: AuthService) {}

  onLogin() {
    this.auth.login(this.email, this.password).subscribe({
      next: res => {
        this.auth.saveSession(res);
        this.message = 'Login successful';
      },
      error: () => this.message = 'Login failed'
    });
  }

  onRegister() {
    this.auth.register(this.email, this.password, this.role).subscribe({
      next: res => {
        this.auth.saveSession(res);
        this.message = 'Registration successful';
      },
      error: () => this.message = 'Registration failed'
    });
  }
}
