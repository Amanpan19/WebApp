import { ChangeDetectionStrategy,ChangeDetectorRef, Component, signal } from '@angular/core';
import { cartRequest } from '../model/cartRequest';
import { ProductService } from '../service/product.service';
import { CartService } from '../service/cart.service';
import { UserService } from '../service/user.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CommonModule } from '@angular/common';
import { MatExpansionModule } from '@angular/material/expansion';
import { Router } from '@angular/router';
import { FavouriteService } from '../service/favourite.service';


@Component({
  selector: 'app-product',
  standalone: true,
  imports: [CommonModule,MatExpansionModule],
  templateUrl: './product.component.html',
  styleUrl: './product.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ProductComponent {
  readonly panelOpenState = signal(false);
  productData:any;
  proCategory:string='';
  proSuggested:any=[];
  proId:number=0;
  prod?:boolean;
  previousSize:string='S';
  proPrice?:number;
  userData:any;
  productExistInCart:any=true;
  loading:boolean=false;
  productAdded:boolean=true;

  constructor(private proSer:ProductService,
     private cartSer:CartService, 
    private _snackBar:MatSnackBar,
     private userSer:UserService,
     private cdr: ChangeDetectorRef,
     private favSer:FavouriteService,
     private route:Router
    ){}

  ngOnInit(): void {
    this.proId=this.proSer.productId;
    console.log(this.proId);
    this.loading=true;
    
    this.proSer.getProductById(this.proId).subscribe({
      next:data=>{
        this.productData=data;
        console.log(this.productData);
        
        this.proSer.convertSingleProductImage(this.productData.data);
        this.proCategory=this.productData.data.category;
        this.proPrice = this.productData.data.productPrice;
        console.log("this is proCat : "+this.proCategory);
        
        console.log(this.productData);
        this.getProductByCat();
      },
      complete:()=>{
        this.loading=false;
        window.scrollTo({ top: 0, behavior: 'smooth' }); 
      }
    })

    this.cartSer.checkExisting(this.proId).subscribe({
      next:data=>{
        
        this.productExistInCart=data;
        console.log(this.productExistInCart.data);
      }
          
    })

    this.favSer.checkExisting(this.proId).subscribe({
      next:data=>{

      }
    })

    this.proSer.viewProduct(this.proId).subscribe({
      next: () => {
        console.log('Product view recorded successfully');
      },
      error: err => {
        console.error('Error recording product view:', err);
      }
    })
    
  }
  getStarsArray(): number[] {
    const rating = this.productData.data.productRating || 0;
    const starsArray: number[] = [];
  
    for (let i = 0; i < rating; i++) {
      starsArray.push(i);
    }
  
    return starsArray;
  }

  getProductByCat(){
   
    this.proSer.getProductByCategory(this.proCategory).subscribe({
     next:(data)=>{
      if(this.proSuggested){
        this.proSuggested=data;
        this.proSer.convertImageData(this.proSuggested);
        this.cdr.detectChanges();
        console.log(this.proSuggested);
      }
     }
    })
    
  }

  addProductToCart() {
    this.userSer.getUserData().subscribe({
      next: (data) => {
        this.userData = data;
  

        const request: cartRequest = {
          productId: this.proId,
          proQty: 1
        };
  
        // Call the service to add the product to the cart
        this.cartSer.addProductInCart(request).subscribe({
          next: (data:any) => {
            this.cartSer.getNoOfProductsInCart().subscribe({
              next: (data: any) => {
                const updatedCount = data.data; // API returns updated count
                this.cartSer.notifyCartCountChange(updatedCount);
              }
            });
            this._snackBar.open('Product added successfully.....', 'success', {
              duration: 2000,
              panelClass: ['mat-toolbar', 'mat-primary'],
              horizontalPosition: 'left',
              verticalPosition: 'top'
            });
            this.ngOnInit();
          },
          error: (err) => {
            this._snackBar.open('Failed to add product', 'error', {
              duration: 2000,
              panelClass: ['mat-toolbar', 'mat-warn']
            });
            console.error('Error adding product to cart:', err);
          }
        });
      },
      error: (err) => {
        this._snackBar.open('Failed to fetch user data', 'error', {
          duration: 2000,
          panelClass: ['mat-toolbar', 'mat-warn']
        });
        console.error('Error fetching user data:', err);
      }
    });
  }

  limitWords(productName: string): string {
    const words = productName.split(' ');
    return words.length > 3 ? words.slice(0, 3).join(' ') + '...' : productName;
  }
  

  updatePrice(size:string){

    if (size === this.previousSize) {
      return; // Do nothing if the same size is clicked again
  }

    let priceMultiplier = 1; // Default multiplier for size S
    const basePrice = this.productData.productPrice;

        switch(size) {
            case 'M':
                priceMultiplier = 1.3;
                break;
            case 'L':
                priceMultiplier = 1.5;
                break;
            case 'XL':
                priceMultiplier = 1.7;
                break;
            case 'XXL':
                priceMultiplier = 1.9;
                break;
    }
    this.proPrice = Math.round(basePrice * priceMultiplier);
    this.previousSize = size;
  }

  buyProduct(){}

  getProduct(productId:number){
    this.proSer.productId=productId;
    console.log(this.proSer.productId);
    this.ngOnInit();
   }

  shareOn(platform: string) {
    const url = window.location.href; // Or the specific URL for the product
    const text = encodeURIComponent("Check out this product!");

    switch (platform) {
      case 'whatsapp':
        window.open(`https://wa.me/?text=${text} ${url}`, '_blank');
        break;
      case 'twitter':
        window.open(`https://twitter.com/share?url=${url}&text=${text}`, '_blank');
        break;
      case 'instagram':
        window.open(`https://instagram.com/`, '_blank');
        break;
      default:
        break;
    }
  }


  toggleFavourite(proId: number) {

    this.favSer.checkExisting(proId).subscribe({
      next:(data:any)=> {
        if (data.data) {
          this.productAdded=false;
          this.removeFromFavourites(proId);
        } else {
          this.addToFavourites(proId);
        }
      },
      error: err => {
        this._snackBar.open('Product is already Present in fav.', 'error', {
          duration: 2000,
          panelClass: ['mat-toolbar', 'mat-warn'],
          horizontalPosition: 'left',
          verticalPosition: 'top'
        });
      }
    });
  }


  addToFavourites(proId:number){
    this.favSer.addProductInFav(proId).subscribe({
      next: data => {
    
          this.favSer.getNoOfProductsInFav().subscribe({
            next: (data: any) => {
              const updatedCount = data.data;
              this.favSer.notifyFavCountChange(updatedCount);
            }
          })
        this._snackBar.open('Product added to favourites successfully.', 'success', {
          duration: 2000,
          panelClass: ['mat-toolbar', 'mat-primary'],
          horizontalPosition: 'left',
          verticalPosition: 'top' 
        });
      },
      error: err => {
        this._snackBar.open('Failed to add product to favourites. Please try again.', 'error', {
          duration: 2000,
          panelClass: ['mat-toolbar', 'mat-warn'],
          horizontalPosition: 'left',
          verticalPosition: 'top' 
        });
      }
    });
  }

  removeFromFavourites(proId: number) {
    this.favSer.removeProductFromFav(proId).subscribe({
      next: data => {
        
        this.favSer.getNoOfProductsInFav().subscribe({
          next: (data: any) => {
            const updatedCount = data.data;
            this.favSer.notifyFavCountChange(updatedCount);
          }
        });
        this._snackBar.open('Product removed from favourites successfully.', 'success', {
          duration: 2000,
          panelClass: ['mat-toolbar', 'mat-primary'],
          horizontalPosition: 'left',
          verticalPosition: 'top'
        });
      },
      error: err => {
        this._snackBar.open('Failed to remove product from favourites. Please try again.', 'error', {
          duration: 2000,
          panelClass: ['mat-toolbar', 'mat-warn'],
          horizontalPosition: 'left',
          verticalPosition: 'top'
        });
      }
    });
  }
  
  
}
