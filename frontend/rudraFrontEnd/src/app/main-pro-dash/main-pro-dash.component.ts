import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ProductService } from '../service/product.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-main-pro-dash',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './main-pro-dash.component.html',
  styleUrl: './main-pro-dash.component.css'
})
export class MainProDashComponent implements OnInit {

  mensProduct:any[]=[];
  menProLoading:boolean=false;

  womensProduct:any[]=[];
  womenProLoading:boolean=false;

  seasonSpecificProduct:any[]=[];
  seasonLoading:boolean=false;

  constructor(private proSer:ProductService, private router: Router){}

  ngOnInit(): void {
    this.loadProductMens("mens");
    this.loadProductWomens("womens");
    this.getProductByTag("hoodie");
  }

  //Mens Product

  loadProductMens(category:string){
    this.proSer.getProductDataByCategory(category).subscribe({
      next:(data:any)=>{
        this.mensProduct = data.data;
        this.proSer.convertImageData(data.data);
        console.log(this.mensProduct);
        this.menProLoading=true;
      }
    })
  }


  //womens product

  loadProductWomens(category:string){
    this.proSer.getProductDataByCategory(category).subscribe({
      next:(data:any)=>{
        this.womensProduct = data.data;
        this.proSer.convertImageData(data.data);
        this.womenProLoading=true;
      }
    })
  }

  scroll(containerClass: string, scrollAmount: number) {
    const container = document.querySelector(`.${containerClass}`) as HTMLElement;
    if (container) {
      container.scrollBy({ left: scrollAmount, behavior: 'smooth' });
    }
  }
  
  getProduct(proId:number){
    this.proSer.productId=proId;
    this.router.navigateByUrl("/product");
 }

 // Seasonal Products

 getProductByTag(proTag:string){
  this.proSer.getProductByTag(proTag,0,10).subscribe({
    next:(data:any)=>{
      this.seasonSpecificProduct=data.data.data;
      this.proSer.convertImageData(data.data.data);
      this.seasonLoading=true;
    }
  })
 }

}
