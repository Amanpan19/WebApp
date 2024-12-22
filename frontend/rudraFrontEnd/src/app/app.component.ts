import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterModule, RouterOutlet} from '@angular/router';
import { NavBarComponent } from "./nav-bar/nav-bar.component";
import { FooterComponent } from './footer/footer.component';
import { MainFrontComponent } from "./main-front/main-front.component";
import { LoginComponent } from "./login/login.component";
import {HttpClientModule} from "@angular/common/http";
import { SignUpComponent } from './sign-up/sign-up.component';
import { FormsModule,ReactiveFormsModule } from '@angular/forms';
import { AddProductComponent } from "./add-product/add-product.component";
import { SupplierDashboardComponent } from "./supplier-dashboard/supplier-dashboard.component";
import { AdminDashboardComponent } from "./admin-dashboard/admin-dashboard.component";
import { ProductComponent } from "./product/product.component";
import { MatBadgeModule } from '@angular/material/badge';
import { CartInfoComponent } from './cart-info/cart-info.component';
import {MatExpansionModule} from '@angular/material/expansion';
import { SearchComponent } from './search/search.component';
import { LoaderComponent } from './loader/loader.component';




@Component({
    selector: 'app-root',
    standalone: true,
    templateUrl: './app.component.html',
    styleUrl: './app.component.css',
    imports: [
        CommonModule,
        RouterOutlet,
        NavBarComponent,
        SignUpComponent,
        FooterComponent,
        MainFrontComponent,
        LoginComponent,FormsModule,
        LoaderComponent,
        ReactiveFormsModule,
        AddProductComponent,
        SupplierDashboardComponent,
        AdminDashboardComponent,
        ProductComponent,
        MatBadgeModule,
        CartInfoComponent,RouterModule,
        MatExpansionModule,
        SearchComponent
    ]
})
export class AppComponent implements OnInit {
  constructor(private router:Router){}
  ngOnInit(): void {
    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        window.scrollTo(0, 0); // Scroll to the top
      }
    });
  }
  title = 'CloudCart';
}
