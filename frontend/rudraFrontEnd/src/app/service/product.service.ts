import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { DomSanitizer,SafeUrl } from '@angular/platform-browser';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  public productId:number=0;
  public fashionType:string='';

  productUrl:string='http://localhost:9000/api/v1/productService';

  constructor(private httpClient:HttpClient,private sanitizer:DomSanitizer ) { }



  addProduct(formData:FormData){
    let httpHeader = new HttpHeaders({
      "Authorization":"Bearer "+localStorage.getItem('Token')
    });
    let reqOption = {headers:httpHeader}
    console.log(reqOption);
    
    return this.httpClient.post(`${this.productUrl}/addNewProduct`,formData,reqOption)
  }

  getAllProduct(){

    return this.httpClient.get(`${this.productUrl}/getAllProducts`)

  }

  getProduct():Observable<any>{
    return this.httpClient.get(`${this.productUrl}/getProducts?pageNum=0&pageSize=20`)
  }

  getTrendingProducts():Observable<any>{
    return this.httpClient.get(`${this.productUrl}/getProducts/trending?pageNum=0&pageSize=10`)
  }
    
  getProductById(productId:number){

    return this.httpClient.get(`${this.productUrl}/getById/${productId}`)

  }

  getProductByCategory(category:string){
    console.log(category);
    
    return this.httpClient.get(`${this.productUrl}/getByCategory/${category}`)
  }

  convertImageData(products: any[]): void {
    products.forEach((product) => {
      if (product.imageUrl) {
        const binaryData = atob(product.imageUrl);
        const arrayBuffer = new ArrayBuffer(binaryData.length);
        const uint8Array = new Uint8Array(arrayBuffer);
        for (let i = 0; i < binaryData.length; i++) {
          uint8Array[i] = binaryData.charCodeAt(i);
        }
        const blob = new Blob([uint8Array], { type: 'image/png' });
        product.imageUrl = this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(blob));
      }
    });
  }

  convertSingleProductImage(product: any): void {
    if (product && product.imageUrl) {
      const binaryData = atob(product.imageUrl);
      const arrayBuffer = new ArrayBuffer(binaryData.length);
      const uint8Array = new Uint8Array(arrayBuffer);
      for (let i = 0; i < binaryData.length; i++) {
        uint8Array[i] = binaryData.charCodeAt(i);
      }
      const blob = new Blob([uint8Array], { type: 'image/png' });
      product.imageUrl = this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(blob));
    }
  }

  viewProduct(productId: number) {
    return this.httpClient.post(`${this.productUrl}/product/view?productId=${productId}`, {});
  }
}
