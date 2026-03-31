import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router'; // Must import this!

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet], // Must include RouterOutlet here
  template: `
    <router-outlet></router-outlet>
  `,
 // styleUrls: ['./app.component.css'] // Or styles: [...]
})
export class AppComponent {
  title = 'anvay-app';
}