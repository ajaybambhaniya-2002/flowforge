import { Component } from '@angular/core';
import { ChartConfiguration, ChartType } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';

@Component({
  selector: 'app-dashboard',
  imports: [BaseChartDirective],
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
}
