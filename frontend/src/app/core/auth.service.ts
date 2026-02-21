import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { AuthResponse } from './models';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly base = environment.apiBaseUrl;
  readonly token = signal<string | null>(localStorage.getItem('token'));
  readonly role = signal<string | null>(localStorage.getItem('role'));

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
    this.token.set(auth.token);
    this.role.set(auth.role);
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    this.token.set(null);
    this.role.set(null);
  }
}
