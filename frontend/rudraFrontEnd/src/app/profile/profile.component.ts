import { Component, OnInit } from '@angular/core';
import { DomSanitizer,SafeUrl } from '@angular/platform-browser';
import { UserService } from '../service/user.service';
import { RouterModule } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { UpdateUserComponent } from '../update-user/update-user.component';
import { LoginService } from '../service/login.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit{

  userImg:SafeUrl='';
  userName:any='';
  email:any='';
  phoneNo:any='';
  gender:any='';
  constructor(
    private userSer:UserService,
    private sanitizer:DomSanitizer,
    private dialog:MatDialog, 
    private logSer:LoginService,
    private route:Router,
    private _snackbar:MatSnackBar
  ){}

  ngOnInit(): void {

    // this.userName=localStorage.getItem('name');
    this.userSer.getUserData().subscribe((data:any)=>{
        if(data){
             console.log(data);
             this.email = data.userEmail;
             this.phoneNo = data.phoneNo;
             this.userName = data.userName;
             this.gender = data.gender;
        }
    })


    this.userSer.getUserImage().subscribe((data:any)=>{
      if(data && data.imageData){
        const imageData = data.imageData;
        const binaryData = atob(imageData)
        const arrayBuffer = new ArrayBuffer(binaryData.length); // creates a arrayBuffer of equal Length of binaryData
        const uint8Array = new Uint8Array(arrayBuffer);
        for (let i = 0; i < binaryData.length; i++) {
          uint8Array[i] = binaryData.charCodeAt(i);
        }
        const blob = new Blob([uint8Array], { type: 'image/png' });
        this.userImg = this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(blob));
      }
    });

  }

  logOut(){
    localStorage.removeItem('Token');
    localStorage.removeItem('role');
    localStorage.removeItem('email');
  
    this.logSer.loginfailure();

    this._snackbar.open('Logged Out successfully.....', 'success', {
      duration: 2000,
      panelClass: ['mat-toolbar', 'mat-primary'],
      horizontalPosition: 'right',
      verticalPosition: 'top'  
    });
    this.route.navigateByUrl('/');  
    
    }

  updateUser(){
    const dialogRef = this.dialog.open(UpdateUserComponent, {
      width:'auto',
    });
  }
}
