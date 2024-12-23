import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import categoriesData from '../fashion.json';
import { CommonModule } from '@angular/common';
import { ProductService } from '../service/product.service';

@Component({
  selector: 'app-fashion-type',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './fashion-type.component.html',
  styleUrl: './fashion-type.component.css'
})
export class FashionTypeComponent implements OnInit {

    category:any;
    constructor(private http:HttpClient,private proSer:ProductService){}

    ngOnInit(): void {
      this.category = categoriesData;
      console.log(this.category);
      this.selectedType(this.proSer.fashionType);
    }

    selectedType(fashionType:String){

      const categories = categoriesData.categories;

      switch(fashionType){
        case "womens" : 
          this.category = categories.womens;
        break;

        case "mens" :
          this.category = categories.mens;
        break;

        case "kids" :
          this.category = categories.kids;
        break;

        case "footwear" :
          this.category = categories.footwear;
        break;

        default:
        break;
      }
    }
} 
