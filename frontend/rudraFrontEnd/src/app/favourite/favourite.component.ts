import { Component, OnInit } from '@angular/core';
import { FavouriteService } from '../service/favourite.service';
import { ProductService } from '../service/product.service';
import { CommonModule } from '@angular/common';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { cartRequest } from '../model/cartRequest';
import { CartService } from '../service/cart.service';

@Component({
  selector: 'app-favourite',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './favourite.component.html',
  styleUrl: './favourite.component.css'
})
export class FavouriteComponent implements OnInit {

  productData:any;
  noOfItems:number=0;
  favProductDetails:any[]=[];

  constructor(
    private favSer:FavouriteService,
    private proSer:ProductService,
    private _snackBar:MatSnackBar,
    private router:Router,
    private cartSer:CartService
  ){}

  ngOnInit(): void {
    this.favSer.getFavDetails().subscribe((data:any)=>{
      
      this.productData=data.data;
      this.noOfItems=data.data.noOfItems;
      const productIds:[] = this.productData.favProducts;

      productIds.forEach((productId:number)=>{
        this.proSer.getProductById(productId).subscribe((productDetail: any) => {
          this.proSer.convertSingleProductImage(productDetail.data);
          this.favProductDetails.push(productDetail);
        });
      })
      
      console.log(this.favProductDetails);
        
    })
  }

  getProduct(productId:number){
    this.proSer.productId=productId;
    this.router.navigateByUrl('/product');
   }

  moveToCart(productId:number){
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

  limitWords(productName: string): string {
    const words = productName.split(' ');
    return words.length > 3 ? words.slice(0, 3).join(' ') + '...' : productName;
  }

  removeFromFav(productId:number){
    this.favSer.removeProductFromFav(productId).subscribe({
      next:(data)=>{

        this.favProductDetails = this.favProductDetails.filter(
          (item) => item.data.productId !== productId
        );

        this._snackBar.open('Product removed successfully.', 'success', {
          duration: 2000,
          panelClass: ['mat-toolbar', 'mat-primary'],
          horizontalPosition: 'left',
          verticalPosition: 'top' 
        });
      }
    })
  }
}
