import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export type ToastType = 'success' | 'error' | 'warning' | 'info';

export interface Toast {
  id: number;
  message: string;
  type: ToastType;
}

@Injectable({ providedIn: 'root' })
export class NotificationService {
  private _toasts = new BehaviorSubject<Toast[]>([]);
  readonly toasts$ = this._toasts.asObservable();

  private nextId = 0;

  showSuccess(message: string, duration = 4000): void {
    this.add('success', message, duration);
  }

  showError(message: string, duration = 5000): void {
    this.add('error', message, duration);
  }

  showWarning(message: string, duration = 4500): void {
    this.add('warning', message, duration);
  }

  showInfo(message: string, duration = 4000): void {
    this.add('info', message, duration);
  }

  dismiss(id: number): void {
    this._toasts.next(this._toasts.value.filter(t => t.id !== id));
  }

  private add(type: ToastType, message: string, duration: number): void {
    const id = this.nextId++;
    this._toasts.next([...this._toasts.value, { id, type, message }]);
    setTimeout(() => this.dismiss(id), duration);
  }
}
