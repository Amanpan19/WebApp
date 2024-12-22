import { HttpClient, HttpHeaders } from '@angular/common/http';
import { EventEmitter, Injectable } from '@angular/core';
import { ProductRemoveRequest } from '../model/productRemoveRequest';
import { ProductQtyReduceRequest } from '../model/ProductQtyReduceRequest';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  
  cartUrl:string="http://localhost:9000/v1/api/cart";

  cartCountUpdated = new EventEmitter<number>();

  constructor(private httpClient:HttpClient) { }

  addProductInCart(cartRequest:any){
    let httpHeader = new HttpHeaders({
      "Authorization":"Bearer "+localStorage.getItem('Token')
    });
    let reqOption = {headers:httpHeader}
    return this.httpClient.post(`${this.cartUrl}/addProduct`,cartRequest,reqOption)
  }


  qtyReduced(request:ProductQtyReduceRequest){
    let httpHeader = new HttpHeaders({
      "Authorization":"Bearer "+localStorage.getItem('Token')
    });
    let reqOption = {headers:httpHeader}
    return this.httpClient.put(`${this.cartUrl}/decrease/productQty`,request,reqOption)
  }

  getNoOfProductsInCart(){
    let httpHeader = new HttpHeaders({
      "Authorization":"Bearer "+localStorage.getItem('Token')
    });
    let reqOption = {headers:httpHeader}
    return this.httpClient.get(`${this.cartUrl}/getNoOfProducts`,reqOption)
  }

  getCartDetails(){
    let httpHeader = new HttpHeaders({
      "Authorization":"Bearer "+localStorage.getItem('Token')
    });
    let reqOption = {headers:httpHeader}
    return this.httpClient.get(`${this.cartUrl}/getCart/details`,reqOption)
  }

  removeProductFromCart(productRemoveRequest:ProductRemoveRequest){
    let httpHeader = new HttpHeaders({
      "Authorization":"Bearer "+localStorage.getItem('Token')
    });
    let reqOption = {headers:httpHeader}
    return this.httpClient.post(`${this.cartUrl}/remove/product`,productRemoveRequest,reqOption)
  }

  checkExisting(productId:any){
    let httpHeader = new HttpHeaders({
      "Authorization":"Bearer "+localStorage.getItem('Token')
    });
    let reqOption = {headers:httpHeader}
    return this.httpClient.get(`${this.cartUrl}/check-exist?productId=${productId}`,reqOption)
  }

  notifyCartCountChange(count: number) {
    this.cartCountUpdated.emit(count);
  }
}
