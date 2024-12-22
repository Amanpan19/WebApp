import { Component, ElementRef, OnInit, QueryList, Renderer2, ViewChildren } from '@angular/core';
import { ProductService } from '../service/product.service';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { DomSanitizer,SafeUrl } from '@angular/platform-browser';
import { FavouriteService } from '../service/favourite.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CartService } from '../service/cart.service';
import { cartRequest } from '../model/cartRequest';
import { LoaderComponent } from '../loader/loader.component';

@Component({
  selector: 'app-product-dash',
  standalone: true,
  imports: [CommonModule,LoaderComponent],
  templateUrl: './product-dash.component.html',
  styleUrl: './product-dash.component.css'
})
export class ProductDashComponent implements OnInit {


  productsData:any=[];
  productsLoaded:boolean=false;
  isButtonClicked:boolean = false;
  isLoading:boolean=false;

  @ViewChildren('productCard') productCards!: QueryList<ElementRef>;

  constructor(
    private productService:ProductService, 
    private router:Router, 
    private favSer:FavouriteService,
    private _snackBar:MatSnackBar, 
    private cartSer:CartService,
    private sanitizer:DomSanitizer){}

  ngOnInit(): void {
   this.fetchProducts();
  }

  fetchProducts() {
    // this.isLoading=true;
    this.productService.getProduct().subscribe({
      next: data => {
        this.productsData = data;
        this.productService.convertImageData(this.productsData.data.content);
        this.productsLoaded = true;
      }
    });
    this.isButtonClicked = false; 

  }

  getProductTrending(){
    this.isLoading=true;
    this.productService.getTrendingProducts().subscribe({
      next:data => {
        this.productsData = data;
        this.productService.convertImageData(this.productsData.data.content);
        this.isLoading=false;
      }
    })
    this.isButtonClicked = true; 
  }

  getProduct(productId:number){
   this.productService.productId=productId;
   console.log(this.productService.productId);
   this.router.navigateByUrl('/product');
  }

  limitWords(productName: string): string {
    const words = productName.split(' ');
    return words.length > 3 ? words.slice(0, 3).join(' ') + '...' : productName;
  }

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
          panelClass: ['mat-toolbar', 'mat-warn']
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
