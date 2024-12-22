import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import categoriesData from '../fashion.json';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-fashion-type',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './fashion-type.component.html',
  styleUrl: './fashion-type.component.css'
})
export class FashionTypeComponent implements OnInit {

    category:any;
    constructor(private http:HttpClient){}

    ngOnInit(): void {
      this.category = categoriesData;
      console.log(this.category);
      this.selectedType("womens");
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
        break;

        case "electronics" :
        break;

        default:
        break;
      }
    }
} 
