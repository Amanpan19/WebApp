import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import categoriesData from '../fashion_for.json';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-fashion-for',
  standalone: true,
  imports: [CommonModule,RouterModule],
  templateUrl: './fashion-for.component.html',
  styleUrl: './fashion-for.component.css'
})
export class FashionForComponent {


    type:any;
    constructor(private http:HttpClient){}

    ngOnInit(): void {
      this.type = categoriesData.fashion_for.type;
    }
}
