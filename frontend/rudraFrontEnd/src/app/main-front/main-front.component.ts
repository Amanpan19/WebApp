import { AfterViewInit, Component } from '@angular/core';
import { ProductDashComponent } from '../product-dash/product-dash.component';
import { FashionTypeComponent } from '../fashion-type/fashion-type.component';
import { FashionForComponent } from '../fashion-for/fashion-for.component';
declare var bootstrap: any;

@Component({
  selector: 'app-main-front',
  standalone: true,
  imports: [ProductDashComponent,FashionTypeComponent,FashionForComponent],
  templateUrl: './main-front.component.html',
  styleUrl: './main-front.component.css'
})
export class MainFrontComponent implements AfterViewInit{

  ngAfterViewInit(): void {
    const carouselElement = document.querySelector('#carouselExampleIndicators');
    if (carouselElement) {
      new bootstrap.Carousel(carouselElement, {
        interval: 5000, // Set interval for autoplay
        ride: 'carousel'
      });
    }
  }
}
