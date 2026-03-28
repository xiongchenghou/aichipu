import { Injectable } from '@nestjs/common';
import { HttpService } from '@nestjs/axios';
import { AxiosRequestConfig } from 'axios';
import { firstValueFrom } from 'rxjs';

export interface CustomerPayload {
  id?: string;
  name: string;
  kanaName?: string;
  contactPerson?: string;
  phone?: string;
  email?: string;
  address?: string;
  taxId?: string;
  notes?: string;
}

export interface ProductPayload {
  id?: string;
  name: string;
  kanaName?: string;
  category?: string;
  unit?: string;
  standardCost?: number;
  sellingPrice?: number;
  hazardous?: boolean;
  notes?: string;
}

export interface ReceiptPayload {
  productId: string;
  quantity: number;
  unitCost: number;
  supplierName?: string;
  referenceNumber?: string;
  notes?: string;
}

export interface ShipmentPayload {
  productId: string;
  quantity: number;
  unitPrice?: number;
  customerId?: string;
  referenceNumber?: string;
  notes?: string;
}

export interface FinancialPayload {
  type: 'REVENUE' | 'EXPENSE' | 'ADJUSTMENT';
  amount: number;
  currency?: string;
  referenceNumber?: string;
  relatedCustomerId?: string;
  description?: string;
}

@Injectable()
export class MetalRecycleService {
  private readonly baseUrl = process.env.BACKEND_URL || 'http://localhost:8080/api/metal-recycle';

  constructor(private readonly http: HttpService) {}

  private async request<T>(method: string, path: string, data?: unknown): Promise<T> {
    const config: AxiosRequestConfig = {
      method,
      url: `${this.baseUrl}${path}`,
      data,
    };

    const response = await firstValueFrom(this.http.request<T>(config));
    return response.data;
  }

  listCustomers() {
    return this.request<CustomerPayload[]>('GET', '/customers');
  }

  createCustomer(payload: CustomerPayload) {
    return this.request<CustomerPayload>('POST', '/customers', payload);
  }

  updateCustomer(id: string, payload: CustomerPayload) {
    return this.request<CustomerPayload>('PUT', `/customers/${id}`, payload);
  }

  listProducts() {
    return this.request<ProductPayload[]>('GET', '/products');
  }

  createProduct(payload: ProductPayload) {
    return this.request<ProductPayload>('POST', '/products', payload);
  }

  updateProduct(id: string, payload: ProductPayload) {
    return this.request<ProductPayload>('PUT', `/products/${id}`, payload);
  }

  inventorySnapshot() {
    return this.request('GET', '/inventory');
  }

  recordReceipt(payload: ReceiptPayload) {
    return this.request('POST', '/receipts', payload);
  }

  recordShipment(payload: ShipmentPayload) {
    return this.request('POST', '/shipments', payload);
  }

  transactionHistory() {
    return this.request('GET', '/transactions');
  }

  recordFinancial(payload: FinancialPayload) {
    return this.request('POST', '/finance/transactions', payload);
  }

  financialTransactions() {
    return this.request('GET', '/finance/transactions');
  }

  financialSummary() {
    return this.request('GET', '/finance/summary');
  }
}
