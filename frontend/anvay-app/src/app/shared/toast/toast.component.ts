import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationService, Toast } from '../../services/notification.service';

@Component({
  selector: 'app-toast',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './toast.component.html',
  styleUrls: ['./toast.component.css']
})
export class ToastComponent {
  protected ns = inject(NotificationService);

  dismiss(id: number): void {
    this.ns.dismiss(id);
  }

  trackById(_: number, t: Toast): number {
    return t.id;
  }
}
