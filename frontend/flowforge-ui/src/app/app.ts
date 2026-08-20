import { Component, signal } from '@angular/core';
import { environment } from '../environments/environment';
import { RouterOutlet } from '@angular/router';
@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
    constructor() {
    console.log('API URL:', environment.apiUrl);
  }
  protected readonly title = signal('flowforge-ui');
 
}
