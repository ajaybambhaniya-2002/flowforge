import { inject, Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root',
})
export class MessageDisplayService {
  private snackBar = inject(MatSnackBar);

  success(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 4500,
      panelClass: ['snackbar-success'],
      horizontalPosition: 'center',
      verticalPosition: 'bottom'
    });
  }

  error(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 4000,
       panelClass: ['snackbar-error'],
      horizontalPosition: 'center',
      verticalPosition: 'bottom'
    });
  }

  warning(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 3500,
      panelClass: ['snackbar-warning'],
      horizontalPosition: 'center',
      verticalPosition: 'bottom'
    });
  }

  info(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
       panelClass: ['snackbar-info'],
      horizontalPosition: 'center',
      verticalPosition: 'bottom'
    });
  }
}
