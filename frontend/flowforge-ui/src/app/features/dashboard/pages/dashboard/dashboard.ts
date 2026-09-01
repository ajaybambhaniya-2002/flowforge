import { Component } from '@angular/core';
import { ChartConfiguration, ChartType } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
@Component({
  selector: 'app-dashboard',
  imports: [BaseChartDirective,MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    RouterLink,
    ReactiveFormsModule,
    MatIconModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class Dashboard {
  constructor(private route:Router) {
    
  }
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
  public projectStatusChartType: 'doughnut' = 'doughnut';

public projectStatusChartData: ChartConfiguration<'doughnut'>['data'] = {
  labels: ['Active', 'Completed', 'Planning'],
  datasets: [
    {
      data: [8, 3, 1]
    }
  ]
};

public projectStatusChartOptions: ChartConfiguration<'doughnut'>['options'] = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      position: 'bottom'
    }
  }
};
public projectProgressChartType: 'bar' = 'bar';

public projectProgressChartData: ChartConfiguration<'bar'>['data'] = {
  labels: [
    'FlowForge',
    'Customer Portal',
    'Payment Service',
    'Notification System'
  ],
  datasets: [
    {
      label: 'Completed',
      data: [18, 12, 15, 3]
    },
    {
      label: 'Remaining',
      data: [6, 8, 0, 15]
    }
  ]
};

public projectProgressChartOptions: ChartConfiguration<'bar'>['options'] = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      position: 'bottom'
    }
  },
  scales: {
    x: {
      stacked: true
    },
    y: {
      stacked: true,
      beginAtZero: true,
      ticks: {
        stepSize: 5
      }
    }
  }
};
onClickRoute(){
  this.route.navigate(['/projectList'])
}
}
