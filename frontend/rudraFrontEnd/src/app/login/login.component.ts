import { Component } from '@angular/core';
import { AbstractControl, FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { LoginService } from '../service/login.service';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule,MatDialogModule,ReactiveFormsModule,CommonModule,RouterModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  responseData:any;
  formData: any;

  constructor(
    private fb:FormBuilder,
    private _snackbar:MatSnackBar,
    private logSer:LoginService,
    private routerComp:Router,
    public dialogRef:MatDialogRef<LoginComponent>,
  ){

    this.formData=this.fb.group({
      userEmail : ["",[Validators.required,this.checkEmail]],
      password : ["",[Validators.required,Validators.minLength(7)]]
    }) 

  }
  
   getEmail(){
    return this.formData.get('userEmail');
   }
  
   getPassword(){
    return this.formData.get('password');
   }
  
   checkEmail(data:AbstractControl){
    if(data.value !==''){
      const emailVal = data.value;
      const emailString = emailVal.split(',').map((e:string)=>e.trim());
      const regEx = /^(([^<>()[\]\.,;:\s@\"]+(\.[^<>()[\]\.,;:\s@\"]+)*)|(\".+\"))@(gmail\.com|yahoo\.com|gufum\.com)$/i;
      const emailInvalid = emailString.every((e:string)=>e.match(regEx)!==null);
      if(!emailInvalid){
        return {checkEmail:false}
      }
    }
    return null;
   }
  
   loginCheck(){
    const formInfo = this.formData.value;
    console.log(formInfo);
    this.logSer.login(formInfo).subscribe({
      next:data=>{
        console.log(data);
        this.responseData = data;
  
        localStorage.setItem('Token',this.responseData.Token);
        localStorage.setItem('role',this.responseData.role);
        localStorage.setItem('name',this.responseData.name);
  
        this.dialogRef.close();
        this._snackbar.open('Logged In successfully.....', 'success', {
          duration: 2000,
          panelClass: ['mat-toolbar', 'mat-primary'],
          horizontalPosition: 'right',
          verticalPosition: 'top'  
        });
  
        this.logSer.loginSuccess();
        
        this.routerComp.navigateByUrl("");
      }
    })
    
   }

   clear(){
    this.dialogRef.close();
  }

  openSignUp(){
    this.routerComp.navigateByUrl("/signUp")
    this.dialogRef.close();
  }
   
}
