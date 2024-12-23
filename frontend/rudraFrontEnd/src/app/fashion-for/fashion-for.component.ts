import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import categoriesData from '../fashion_for.json';
import { RouterModule } from '@angular/router';
import { ProductService } from '../service/product.service';

@Component({
  selector: 'app-fashion-for',
  standalone: true,
  imports: [CommonModule,RouterModule],
  templateUrl: './fashion-for.component.html',
  styleUrl: './fashion-for.component.css'
})
export class FashionForComponent {


    type:any;
    constructor(private http:HttpClient, private productService:ProductService){}

    ngOnInit(): void {
      this.type = categoriesData.fashion_for.type;
    }

    selectedType(fashionType:string){
      this.productService.fashionType=fashionType;
    }
}
