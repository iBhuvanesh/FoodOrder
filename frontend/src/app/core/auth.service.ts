import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { AuthResponse } from './models';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly base = environment.apiBaseUrl;
  readonly token = signal<string | null>(localStorage.getItem('token'));
  readonly role = signal<string | null>(localStorage.getItem('role'));
  readonly userId = signal<number | null>(Number(localStorage.getItem('userId')) || null);

  constructor(private http: HttpClient) {}

  login(email: string, password: string) {
    return this.http.post<AuthResponse>(`${this.base}/api/auth/login`, { email, password });
  }

  register(email: string, password: string, role: string) {
    return this.http.post<AuthResponse>(`${this.base}/api/auth/register`, { email, password, role });
  }

  saveSession(auth: AuthResponse) {
    localStorage.setItem('token', auth.token);
    localStorage.setItem('role', auth.role);
    localStorage.setItem('userId', String(auth.userId));
    this.token.set(auth.token);
    this.role.set(auth.role);
    this.userId.set(auth.userId);
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('userId');
    this.token.set(null);
    this.role.set(null);
    this.userId.set(null);
  }
}
