import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LoginService } from '../service/login.service';
import { BrowserModule } from '@angular/platform-browser';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [CommonModule,FormsModule,ReactiveFormsModule],
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.css'
})
export class ForgotPasswordComponent {

  otpSent:boolean=true
  email:string="";

  constructor(
    private loginSer:LoginService
  ){}

  sendOtpToEmailAfterVarification(){
    
    this.loginSer.generateOtpForgotPass(this.email).subscribe({
      next:(data:any)=>{
        if(data.success){
          this.otpSent=false;
        }
      }
    })
  }
 
}
