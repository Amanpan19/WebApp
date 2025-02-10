import { Component} from '@angular/core';
import { CartService } from '../service/cart.service';
import { ProductService } from '../service/product.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatIcon } from '@angular/material/icon';
import { cartRequest } from '../model/cartRequest';
import { ProductQtyReduceRequest } from '../model/ProductQtyReduceRequest';
import { MatSnackBar } from '@angular/material/snack-bar';
import { LoginService } from '../service/login.service';
import { Router, RouterModule } from '@angular/router';
import { LoginComponent } from '../login/login.component';
import { MatDialog } from '@angular/material/dialog';
import { FavouriteService } from '../service/favourite.service';
import { ProductQtyIncreaseRequest } from '../model/ProductQtyIncreaseRequest';

@Component({
  selector: 'app-cart-info',
  templateUrl: './cart-info.component.html',
  styleUrls: ['./cart-info.component.css'],
  standalone: true,
  imports: [FormsModule,CommonModule,MatIcon,RouterModule],
})
export class CartInfoComponent {

  cartDetailsLoaded:boolean=false;
  cartProductDetails: any[] = [];
  quantity:number=1;
  userId:string="";
  totalMrp:number=0.00;
  noOfItems:number=0;
  phoneView:boolean=false;
  checkLogin:boolean=false;
  selectedSize: string = '';
  selectedColor: string = '';
  totalMRP: number = 0;
  totalDiscount: number = 0;
  totalPrice: number = 0;

  convenienceFeePerProduct: number = 40;
  totalConvenienceFee: number = 0;
  freeShippingThreshold: number = 599;

  constructor(
    private cartService: CartService,
    private productService: ProductService, 
    private _snackBar:MatSnackBar,
    private logSer:LoginService,
    private dialog:MatDialog,
    private favSer:FavouriteService,
    private proSer:ProductService,
    private router:Router
  ) {}

  ngOnInit(): void {
    this.loadCartProductDetails();
    this.checkLogin = this.logSer.getLoginStatus();
  }

  // loadCartProductDetails() {
    
  //   this.cartService.getCartDetails().subscribe((cartResponse: any) => {
  //     console.log(cartResponse);
  //     this.userId=cartResponse.userId;
  //     const productDetails = cartResponse.data.productDetails;
      
  //     console.log(productDetails);
      
  //     const productIds: number[] = Object.keys(productDetails).map(key => Number(key));
  //     const quantities: number[] =Object.values(productDetails).map(value => Number(value));

  //     if (productIds && Array.isArray(productIds)) { // Check if productIds is a valid array
        
  //       productIds.forEach((productId: number, index: number) => {
  //         this.productService.getProductById(productId).subscribe((productDetail: any) => {
  //           this.productService.convertSingleProductImage(productDetail.data);
  //         productDetail.data.proQty = quantities[index]; // Set the quantity from the map
  //         this.cartProductDetails.push(productDetail);
  //         console.log("end");
  //         });
  //       });
  //     } else {
  //       console.error("Invalid product IDs.");
  //     }
  //   });
  //   this.cartDetailsLoaded=true;
    
  // }

  loadCartProductDetails() {
  this.cartService.getCartDetails().subscribe((cartResponse: any) => {
    console.log(cartResponse);
    this.userId = cartResponse.userId;

    const productDetails = cartResponse.data.productDetails; // Contains { productId: { quantity, price, color } }
    console.log(productDetails);

    const productIds: number[] = Object.keys(productDetails).map(key => Number(key));

    if (productIds && Array.isArray(productIds)) {
      productIds.forEach((productId: number) => {
        const details = productDetails[productId]; // Get the details for the product ID

        this.productService.getProductById(productId).subscribe((productDetail: any) => {
          this.productService.convertSingleProductImage(productDetail.data);

          // Map the additional details (quantity, price, color) to the product details
          productDetail.data.proQty = details.quantity;
          productDetail.data.productPrice = details.price;
          productDetail.data.productColor = details.color;
          productDetail.data.productSize = details.size;

          this.cartProductDetails.push(productDetail);
          
          this.calculateCartSummary();
        });
      });
    } else {
      console.error("Invalid product IDs.");
    }
  });

  this.cartDetailsLoaded = true;
}


  getProduct(productId:number){
    this.proSer.productId=productId;
    this.router.navigateByUrl('/product');
   }


  removeProduct(productId: any) {
    const productRemoveRequest = { productId };
  
    this.cartService.removeProductFromCart(productRemoveRequest).subscribe(() => {
      // After removal, reload the cart details
      this.cartProductDetails = this.cartProductDetails.filter(
        (item) => item.data.productId !== productId
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
        horizontalPosition: 'left',
        verticalPosition: 'top' 
      });
    });
  }

  moveToFavSection(productId:number){
    this.favSer.addProductInFav(productId).subscribe({
      next: data => {

        this.favSer.getNoOfProductsInFav().subscribe({
          next: (data: any) => {
            const updatedCount = data.data;
            this.favSer.notifyFavCountChange(updatedCount);
          }
        })
      }
    });

    this.removeProduct(productId);
  }

  buyProduct(product:any){}

  // increaseQuantity(product: any) {

  //   product.data.proQty += 1;

  //   const request: ProductQtyIncreaseRequest = {
  //     productId: product.data.productId,
  //     proQty:product.data.proQty
  //   }

  //   this.cartService.qtyIncrease(request).subscribe({
      
  //   })
  // }
  
  // decreaseQuantity(product: any) {
  //   if (product.data.proQty > 1) {

  //     product.data.proQty -= 1;
  //     const request: ProductQtyReduceRequest = {
  //       productId: product.data.productId,
  //       proQty:product.data.proQty
  //     }
  //     this.cartService.qtyReduced(request).subscribe({

  //     })
  //   }
  // }

  increaseQuantity(product: any): void {
    product.data.proQty += 1;
    this.updateQuantity(product.data.productId, product.data.proQty, 'increase');
  }

  decreaseQuantity(product: any): void {
    if (product.data.proQty > 1) {
      product.data.proQty -= 1;
      this.updateQuantity(product.data.productId, product.data.proQty, 'decrease');
    }
  }

  private updateQuantity(productId: number, quantity: number, action: 'increase' | 'decrease'): void {
    const request = action === 'increase' 
      ? { productId, proQty: quantity } as ProductQtyIncreaseRequest 
      : { productId, proQty: quantity } as ProductQtyReduceRequest;

    const serviceMethod = action === 'increase' ? this.cartService.qtyIncrease(request) : this.cartService.qtyReduced(request);

    serviceMethod.subscribe(() => {
      this.calculateCartSummary();
    });
  }

  navigateUser(){

    if (!this.logSer.getLoginStatus()) {
      const dialogRef = this.dialog.open(LoginComponent, {
        width:'auto',
      });
    }

  }

  limitWords(productName: string): string {
    const words = productName.split(' ');
    return words.length > 3 ? words.slice(0, 3).join(' ') + '...' : productName;
  }

  openDialog(){

  }

  clickToProceed(){

  }

  calculateCartSummary() {
    this.totalMRP = 0.00;
    this.totalDiscount = 0.00;
    this.totalConvenienceFee = 0.00;
  
    this.cartProductDetails.forEach((product: any) => {
      const productMRP = product.data.productPrice*product.data.proQty;
      this.totalMRP += productMRP;
  
      const discount = 15; // Assuming a `discount` property exists
      const productDiscount = (productMRP * discount) / 100;
      this.totalDiscount += productDiscount;
    });

    // Calculate convenience fee based on the total MRP
    if (this.totalMRP > this.freeShippingThreshold) {
      this.totalConvenienceFee = 0; // Free shipping for MRP > ₹599
    } else {
      const totalProducts = this.cartProductDetails.reduce((sum, product) => sum + product.data.proQty, 0);
      this.totalConvenienceFee = totalProducts * this.convenienceFeePerProduct; // ₹40 per product
    }

    this.totalPrice = this.totalMRP - this.totalDiscount + this.totalConvenienceFee;
  }
}
