import { Component, EventEmitter } from '@angular/core';
import { CartService } from '../service/cart.service';
import { ProductService } from '../service/product.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatIcon } from '@angular/material/icon';
import { cartRequest } from '../model/cartRequest';
import { ProductQtyReduceRequest } from '../model/ProductQtyReduceRequest';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-cart-info',
  templateUrl: './cart-info.component.html',
  styleUrls: ['./cart-info.component.css'],
  standalone: true,
  imports: [FormsModule,CommonModule,MatIcon],
})
export class CartInfoComponent {
  cartDetailsLoaded:boolean=false;

  cartProductDetails: any[] = [];
  quantity:number=1;
  userId:string="";

  constructor(private cartService: CartService, private productService: ProductService, private _snackBar:MatSnackBar) {}

  ngOnInit(): void {
    this.loadCartProductDetails();
  }

  loadCartProductDetails() {
    
    this.cartService.getCartDetails().subscribe((cartResponse: any) => {
      console.log(cartResponse);
      this.userId=cartResponse.userId;
      const productDetails = cartResponse.data.productDetails;
      const productIds: number[] = Object.keys(productDetails).map(key => Number(key));
      const quantities: number[] =Object.values(productDetails).map(value => Number(value));

      if (productIds && Array.isArray(productIds)) { // Check if productIds is a valid array
        console.log("enter product get");
        
        productIds.forEach((productId: number, index: number) => {
          this.productService.getProductById(productId).subscribe((productDetail: any) => {
            this.productService.convertSingleProductImage(productDetail.data);
          productDetail.data.proQty = quantities[index]; // Set the quantity from the map
          this.cartProductDetails.push(productDetail);
          console.log("end");
          });
        });
      } else {
        console.error("Invalid product IDs.");
      }
    });
    this.cartDetailsLoaded=true;
    
  }


  removeProduct(product: any) {
    const productRemoveRequest = { productId: product.data.productId };
  
    this.cartService.removeProductFromCart(productRemoveRequest).subscribe(() => {
      // After removal, reload the cart details
      this.cartProductDetails = this.cartProductDetails.filter(
        (item) => item.data.productId !== product.data.productId
      );

      this.cartService.getNoOfProductsInCart().subscribe({
        next: (data: any) => {
          const updatedCount = data.data; // API returns updated count
          this.cartService.notifyCartCountChange(updatedCount);
        }
      });

      this._snackBar.open('Product Removed.....', 'success', {
        duration: 2000,
        panelClass: ['mat-toolbar', 'mat-primary'],
        horizontalPosition: 'right',
        verticalPosition: 'top' 
      });
    });
  }

  buyProduct(product:any){}

  increaseQuantity(product: any) {

    product.data.proQty += 1;

    const request: cartRequest = {
      productId: product.data.productId,
      proQty: product.data.proQty
    };

    this.cartService.addProductInCart(request).subscribe({
      
    })
  }
  
  decreaseQuantity(product: any) {
    if (product.data.proQty > 1) {

      product.data.proQty -= 1;
      const request: ProductQtyReduceRequest = {
        productId: product.data.productId,
        proQty:product.data.proQty
      }
      this.cartService.qtyReduced(request).subscribe({

      })
    }
  }
}
