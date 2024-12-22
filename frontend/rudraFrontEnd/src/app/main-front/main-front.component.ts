import { Component } from '@angular/core';
import { ProductDashComponent } from '../product-dash/product-dash.component';
import { FashionTypeComponent } from '../fashion-type/fashion-type.component';
import { FashionForComponent } from '../fashion-for/fashion-for.component';

@Component({
  selector: 'app-main-front',
  standalone: true,
  imports: [ProductDashComponent,FashionTypeComponent,FashionForComponent],
  templateUrl: './main-front.component.html',
  styleUrl: './main-front.component.css'
})
export class MainFrontComponent {

}
