import { Component, HostListener, Inject } from '@angular/core';
import { LoginService } from '../service/login.service';
import { UserService } from '../service/user.service';
import { CartService } from '../service/cart.service';
import { SupplierService } from '../service/supplier.service';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatBadgeModule } from '@angular/material/badge';
import { SearchComponent } from '../search/search.component';
import { LoginComponent } from '../login/login.component';
import { MatDialog } from '@angular/material/dialog';
import { FavouriteService } from '../service/favourite.service';

@Component({
  selector: 'app-nav-bar',
  standalone: true,
  imports: [RouterModule, CommonModule, MatBadgeModule,SearchComponent],
  templateUrl: './nav-bar.component.html',
  styleUrl: './nav-bar.component.css'
})
export class NavBarComponent {
  userName:any;
  supplierRole:any;
  loginTime:any="Login";
  supplierValid:boolean=false;
  supplierVer:boolean=true;
  role=true;
  loggedIn:boolean=false;
  noOfProductsInCart:number=0;
  noOfProductsInFav:number=0;
  isMobile: boolean = false;
  isMenuOpen:boolean=false;
  
  constructor(private logService:LoginService,
    private userSer:UserService,
    private supService:SupplierService,
    private router :Router,
    private cartSer:CartService,
    private favSer:FavouriteService,
    private dialog:MatDialog
  ){}
    ngOnInit(): void {
      
      this.checkMobileView();
      this.userName ="User" ;
  
      this.logService.userLoggedIn.subscribe(()=>{
  
        this.userName = localStorage.getItem('name');
          this.loginTime="LogOut";
  
          console.log(this.supplierRole=='supplier');
  
          this.supService.getSupplierRole().subscribe({
            next:(data:any)=>{
              console.log(data);
              this.supplierRole = data.suppRole;
              localStorage.setItem('supRole',this.supplierRole)
              if(this.supplierRole=='supplier'){
          
                this.supplierValid=true;
                this.supplierVer=false;
              }  
            } 
          })
         
  
        if(localStorage.getItem('role')==='adminRole'){
          this.role=false;
          this.supplierVer=false;
        }
  
      if(this.logService.isLoggedIn){
        this.loggedIn=true;
  
        this.cartSer.getNoOfProductsInCart().subscribe({
          next:(data:any)=>{
            this.noOfProductsInCart=data.data;
          }
        });

        this.favSer.getNoOfProductsInFav().subscribe({
          next:(data:any)=>{
            this.noOfProductsInFav=data.data;
          }
        })
  
      }
      })

      
      document.addEventListener("DOMContentLoaded",function(){
        window.addEventListener("scroll",function(){
          if(this.scrollY>320){
            document.querySelector('.nav')?.classList.add("sticky");
          }
          else{
            document.querySelector('.nav')?.classList.remove("sticky");
          }
        });
      });

      this.cartSer.cartCountUpdated.subscribe((count: number) => {
        console.log(count);
        
        this.noOfProductsInCart = count;
      });

      this.favSer.favCountUpdated.subscribe((count:number)=>{
        this.noOfProductsInFav=count;
      })
      
    }
  
    clicked(){
      if(this.loginTime==='Login'){
        this.router.navigateByUrl('/login');
      }
      if(this.loginTime === 'LogOut'){
        this.role=true;
        this.supplierVer=true;
        this.loggedIn=false;
        this.logService.loginfailure();
        localStorage.clear();
        this.router.navigateByUrl("/login")
        this.loginTime='Login';
        this.userName ="User" ;
      }
    }
  
    toggleMenu() {
      var menu = document.getElementById("expandableMenu");
      menu?.classList.toggle("active");

      this.isMenuOpen = !this.isMenuOpen;
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: Event) {
    this.checkMobileView();
  }

   checkMobileView() {
    this.isMobile = window.innerWidth < 768; // Set isMobile based on window width
  }

  clearInput(){
    const input = document.getElementsByTagName("input")[0];
    input.value = "";
  }

  login(){
    const dialogRef = this.dialog.open(LoginComponent, {
      width:'auto',
    });

    if(this.loginTime === 'LogOut'){
      this.role=true;
      this.supplierVer=true;
      this.loggedIn=false;
      this.logService.loginfailure();
      localStorage.clear();
      this.loginTime='Login';
      this.userName ="User" ;
    }
  }

  navigateUser(){

    if (this.loggedIn) {
      this.router.navigate(['/profile']);
    } else {
      const dialogRef = this.dialog.open(LoginComponent, {
        width:'auto',
      });
    }

  }
  
}
