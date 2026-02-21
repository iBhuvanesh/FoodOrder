export interface AuthResponse {
  userId: number;
  token: string;
  email: string;
  role: string;
}

export interface Restaurant {
  id: number;
  name: string;
  cuisine: string;
  active: boolean;
}

export interface MenuItem {
  id: number;
  name: string;
  description: string;
  price: number;
  available: boolean;
}

export interface Cart {
  id: number;
  userId: number;
  items: CartItem[];
}

export interface CartItem {
  id: number;
  menuItemId: number;
  restaurantId: number;
  quantity: number;
  unitPrice: number;
}

export interface FoodOrder {
  id: number;
  userId: number;
  restaurantId: number;
  status: string;
  totalAmount: number;
}

export interface PaymentResponse {
  paymentId: number;
  orderId: number;
  status: string;
  message: string;
}
