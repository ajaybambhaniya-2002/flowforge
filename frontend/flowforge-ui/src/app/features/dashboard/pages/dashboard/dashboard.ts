import { Component } from '@angular/core';

@Component({
  selector: 'app-dashboard',
  imports: [],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard {

  dashboardStats = [
    {
      title: 'Total Projects',
      value: 12
    },
    {
      title: 'Active Projects',
      value: 8
    },
    {
      title: 'Total Tasks',
      value: 48
    },
    {
      title: 'Team Members',
      value: 6
    }
  ];
}
