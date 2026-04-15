import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { TopbarComponent } from './topbar/topbar.component';
import { SidebarComponent } from './sidebar/sidebar.component';

@Component({
  selector: 'app-student',
  standalone: true,
  imports: [
    CommonModule, 
    RouterModule, 
    TopbarComponent, 
    SidebarComponent
  ],
  templateUrl: './student.component.html',
  styleUrls: ['./student.component.css']
})
export class StudentComponent {
  menuItems = [
    { path: 'home', icon: 'grid_view', label: 'Dashboard' },
    { path: 'event-feed', icon: 'explore', label: 'Event Feed' },
    { path: 'clubs', icon: 'groups', label: 'Clubs' },
    { path: 'leaderboard', icon: 'emoji_events', label: 'Leaderboard' },
    { path: 'profile', icon: 'person', label: 'Profile' }
  ];
}