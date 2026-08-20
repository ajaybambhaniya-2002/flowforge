import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Header } from '../header/header';
import { SideBar } from '../side-bar/side-bar';

@Component({
  selector: 'app-main-layout',
  imports: [ RouterOutlet,
    Header,
    SideBar],
  templateUrl: './main-layout.html',
  styleUrl: './main-layout.scss',
})
export class MainLayout {

}
