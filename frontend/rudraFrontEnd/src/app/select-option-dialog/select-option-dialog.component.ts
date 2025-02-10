import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { FavouriteService } from '../service/favourite.service';
import { cartRequest } from '../model/cartRequest';
import { CartService } from '../service/cart.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ProductService } from '../service/product.service';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { Router } from '@angular/router';

@Component({
  selector: 'app-select-option-dialog',
  standalone: true,
  imports: [MatDialogModule, CommonModule],
  templateUrl: './select-option-dialog.component.html',
  styleUrl: './select-option-dialog.component.css'
})
export class SelectOptionDialogComponent implements OnInit {
  
  sizeAvail:string[]=[]; 
  selectedSize: string = '';
  selectedColor: string = '';
  proImg: SafeUrl | null = null;
  productName:string='';
  colorCodes: string[] = [];
  isSelectedSize: boolean = false;
  isSelectedColor: boolean = false;
  proPrice:number=0.00;
  originalPrice:number=0.00;
  previousSize:string='S';

  constructor(
    private favSer:FavouriteService,
    private proSer:ProductService,
    private cartSer: CartService,
    private _snackBar: MatSnackBar,
    private sanitizer: DomSanitizer,
    private router:Router,
    public dialogRef:MatDialogRef<SelectOptionDialogComponent>,
  ){}

  ngOnInit(): void {
    this.getProductdata();
  }

  getProductdata(){
    this.proSer.getProductById(this.proSer.productId).subscribe((data:any)=>{
      this.sizeAvail=data.data.productDetails.availableSize;
      this.colorCodes=data.data.productDetails.colorsAvail;
      this.originalPrice = data.data.productPrice;

      const imageData = data.data.imageUrl;
      this.proImg = this.decodeImage(imageData, 'image/png');

      this.productName=data.data.productName
      this.proPrice=data.data.productPrice;
    });
  }



  decodeImage(base64Data: string, mimeType: string): SafeUrl {
    const binaryData = atob(base64Data); // Decode Base64 string
    const uint8Array = new Uint8Array(binaryData.length);

    for (let i = 0; i < binaryData.length; i++) {
      uint8Array[i] = binaryData.charCodeAt(i);
    }

    // Create a blob and generate a URL
    const blob = new Blob([uint8Array], { type: mimeType });
    return this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(blob));
  }


  moveToCart(){

        const request: cartRequest = {
          productId: this.proSer.productId,
          proQty: 1,
          productColor: this.selectedColor,
          productSize: this.selectedSize,
          productPrice : this.proPrice
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

            this.clear();
          },
          error: (err) => {
            this._snackBar.open('Failed to add product', 'error', {
              duration: 2000,
              panelClass: ['mat-toolbar', 'mat-warn']
            });
            console.error('Error adding product to cart:', err);
          }
         })

         this.favSer.removeProductFromFav(request.productId).subscribe({
          next:(data)=>{
            this.favSer.getNoOfProductsInFav().subscribe({
              next: (data: any) => {
                const updatedCount = data.data; // API returns updated count
                this.favSer.notifyFavCountChange(updatedCount);
              }
            });
          }
        })

        this.router.navigateByUrl('/cart')
      }

  clear(){
    this.dialogRef.close();
  }

  onSizeSelected(size: string) {
     this.selectedSize = size;
     this.isSelectedSize=true;
     this.updateProductPrice(size);
  }

  onColorSelected(color: string) {
     this.selectedColor = color;
     this.isSelectedColor=true;
  }

  updateProductPrice(size:string){
    

    if(size===this.previousSize){
      return
    }

    const basePrice = this.originalPrice;
    let priceMultiplier = 1;
    
    switch(size){
      case 'L':
        priceMultiplier = 1.02;
        break;  
      case 'XL':
        priceMultiplier = 1.1;
        break;
      case 'XXL':
        priceMultiplier = 1.16;
        break;
      case 'XXXL':
        priceMultiplier = 1.18;
        break;
      default:
        priceMultiplier = 1;
        break;  
    }

    this.proPrice = Math.round(basePrice * priceMultiplier);
    this.previousSize = size;

  }

}
