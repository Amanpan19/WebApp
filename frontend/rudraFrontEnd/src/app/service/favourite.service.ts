import { HttpClient, HttpHeaders } from '@angular/common/http';
import { EventEmitter, Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class FavouriteService {

  favUrl:string = "http://localhost:9000/api/v1/fav"

  favCountUpdated = new EventEmitter<number>();

  constructor(private httpClient:HttpClient) { }

  addProductInFav(productId:any){
    let httpHeader = new HttpHeaders({
      "Authorization":"Bearer "+localStorage.getItem('Token')
    });
    let reqOption = {headers:httpHeader}
    console.log(httpHeader);
    
    return this.httpClient.post(`${this.favUrl}/addProduct?productId=${productId}`,null,reqOption)
  }

  removeProductFromFav(productId:any){
    let httpHeader = new HttpHeaders({
      "Authorization":"Bearer "+localStorage.getItem('Token')
    });
    let reqOption = {headers:httpHeader}
    console.log(httpHeader);
    
    return this.httpClient.post(`${this.favUrl}/remove/product?productId=${productId}`,null,reqOption)
  }

  getFavDetails(){
    let httpHeader = new HttpHeaders({
      "Authorization":"Bearer "+localStorage.getItem('Token')
    });
    let reqOption = {headers:httpHeader}
    return this.httpClient.get(`${this.favUrl}/getFav/details/data`,reqOption)
  }

  getNoOfProductsInFav(){
    let httpHeader = new HttpHeaders({
      "Authorization":"Bearer "+localStorage.getItem('Token')
    });
    let reqOption = {headers:httpHeader}
    return this.httpClient.get(`${this.favUrl}/getNoOfProducts`,reqOption)
  }

  checkExisting(productId:any){
    let httpHeader = new HttpHeaders({
      "Authorization":"Bearer "+localStorage.getItem('Token')
    });
    let reqOption = {headers:httpHeader}
    return this.httpClient.get(`${this.favUrl}/check-exist?productId=${productId}`,reqOption)
  }

  notifyFavCountChange(count: number) {
    this.favCountUpdated.emit(count);
  }
}
