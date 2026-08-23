import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { AuthStateService } from '../../services/auth-state-service';
import { TokenService } from '../../services/token-service';
import { Router } from '@angular/router';
import { Auth } from '../../services/auth';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialog } from '../../components/confirm-dialog/confirm-dialog';
@Component({
  selector: 'app-header',
  imports: [MatButtonModule,MatIcon],
  templateUrl: './header.html',
  styleUrl: './header.scss',
})
export class Header {
  constructor(private authStateService:AuthStateService,private tokenService:TokenService
    , private router:Router,private authService:Auth
  ){}
  private dialog = inject(MatDialog);
logout():void{
  const dialogRef = this.dialog.open(ConfirmDialog, {
    width: '400px',
    data: {
      title: 'Logout',
      message: 'Are you sure you want to logout?',
      confirmText: 'Logout',
      cancelText: 'Cancel'
    }
  });
  dialogRef.afterClosed().subscribe((confirmed: boolean) => {

    if (confirmed) {
      this.authService.logout().subscribe((res:any)=>{
        console.log(res);
        this.authStateService.setUnauthenticated();
        this.tokenService.clearAccessToken();
        this.router.navigate(['/auth/login']);
      })
    }

  });
  // we need to clean up at time of logout
}
TestApiConcurrent(){
this.authService.testApi().subscribe((res:any)=>{
  console.log(res);
})
}
}
