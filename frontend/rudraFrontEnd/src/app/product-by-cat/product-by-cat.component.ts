import { Component } from '@angular/core';
import { ProductService } from '../service/product.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import {MatSliderModule} from '@angular/material/slider';
import { FormsModule } from '@angular/forms';
import { CartService } from '../service/cart.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FavouriteService } from '../service/favourite.service';
import { cartRequest } from '../model/cartRequest';
import { FashionTypeComponent } from '../fashion-type/fashion-type.component';

@Component({
  selector: 'app-product-by-cat',
  standalone: true,
  imports: [CommonModule,MatSliderModule,FormsModule,FashionTypeComponent],
  templateUrl: './product-by-cat.component.html',
  styleUrl: './product-by-cat.component.css'
})
export class ProductByCatComponent {
  productsData:any=[];
  productsLoaded:boolean=false;
  category:string='women';
  productId?:number;
  viewMode: string = 'grid';
  minPrice: number = 2500; 
  maxPrice: number = 7500; 
  priceGap: number = 1000; 
  minRange: number = 0; 
  maxRange: number =10000;

  constructor(
     private productService:ProductService, 
     private route:ActivatedRoute,
     private cartSer:CartService,
     private _snackBar:MatSnackBar,
     private favSer:FavouriteService,
     private router:Router ){}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.category = params['category'];
      this.fetchProducts();
    })
    
  }

  fetchProducts(){
    this.productService.getProductByCategory(this.category).subscribe({
      next:data=>{
       this.productsData = data;
       this.productService.convertImageData(this.productsData);
       this.productsLoaded=true;
      }
    })
  }

  getProduct(proId:number){
     this.productId=proId;
     console.log(this.productId);
     this.productService.productId=proId;
     this.router.navigateByUrl("/product");
  }

  setView(mode: string) {
    this.viewMode = mode;
  }

  limitWords(productName: string): string {
    const words = productName.split(' ');
    return words.length > 3 ? words.slice(0, 3).join(' ') + '...' : productName;
  }
  
  onMinInputChange(value: number): void 
  { 
    if ((this.maxPrice - value) >= this.priceGap) 
      { this.minPrice = value; } 
  }
  onMaxInputChange(value: number): void { if ((value - this.minPrice) >= this.priceGap) { this.maxPrice = value; } } 
  onRangeMinInputChange(value: number): void { if ((this.maxPrice - value) >= this.priceGap) { this.minPrice = value; } } 
  onRangeMaxInputChange(value: number): void { if ((value - this.minPrice) >= this.priceGap) { this.maxPrice = value; } }  


  addInFavSection(productId: number) {
    this.favSer.addProductInFav(productId).subscribe({
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

  addToCart(productId:number){
    const request: cartRequest = {
      productId: productId,
      proQty: 1
    };

     this.cartSer.addProductInCart(request).subscribe({
      next:data=>{
        this.cartSer.getNoOfProductsInCart().subscribe({
          next: (data: any) => {
            const updatedCount = data.data;
            this.cartSer.notifyCartCountChange(updatedCount);
          }
        })
        this._snackBar.open('Product added to Cart', 'success', {
          duration: 2000,
          panelClass: ['mat-toolbar', 'mat-primary'],
          horizontalPosition: 'left',
          verticalPosition: 'top' 
        });
      }
     })
  }
}
