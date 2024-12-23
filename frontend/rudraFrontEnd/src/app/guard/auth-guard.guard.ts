import { CanActivateFn } from '@angular/router';
import { LoginService } from '../service/login.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { inject } from '@angular/core';

export const authGuardGuard: CanActivateFn = (route, state) => {
  const logService = inject(LoginService);
  const snackBar = inject(MatSnackBar);

  if(logService.getLoginStatus()){
   return true;
  }
  else{
    snackBar.open('You have to Login First', 'Failure', {
      duration: 3000,
      panelClass: ['red-error'],
      horizontalPosition: 'right',
      verticalPosition: 'bottom'  
    });
    return false;
  }
};
