import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { MainFrontComponent } from './main-front/main-front.component';
import { SignUpComponent } from './sign-up/sign-up.component';
import { ProfileComponent } from './profile/profile.component';
import { AddProductComponent } from './add-product/add-product.component';
import { authGuardGuard } from './guard/auth-guard.guard';
import { SupplierDashboardComponent } from './supplier-dashboard/supplier-dashboard.component';
import { SupplierVerificationComponent } from './supplier-verification/supplier-verification.component';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { SupplierWaitingComponent } from './supplier-waiting/supplier-waiting.component';
import { ProductComponent } from './product/product.component';
import { FavouriteComponent } from './favourite/favourite.component';
import { ProductByCatComponent } from './product-by-cat/product-by-cat.component';
import { CartInfoComponent } from './cart-info/cart-info.component';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { AboutUsComponent } from './about-us/about-us.component';
import { ContactComponent } from './contact/contact.component';


export const routes: Routes = [
    {
       path:"",
       component:MainFrontComponent, 
       title:"Rivaana | Home"
    },{
      path:"login",
      component:LoginComponent,
      title:"Login"
    },
    {
        path:"signUp",
        component:SignUpComponent,
        title:"Sign Up"
    },
    {
        path:"profile",
        component:ProfileComponent,
        title:"User | Profile",
        canActivate:[authGuardGuard]
    },
    {
        path:"addProduct",
        component:AddProductComponent,
        title:"Add | Product"
    },
    {
        path:"supplierDash",
        component:SupplierDashboardComponent,
        title:"Supplier | Dashboard",
        canActivate:[authGuardGuard]
    },
    {
        path:"suppVerification",
        component:SupplierVerificationComponent,
        title:"Supplier | Verification",
        canActivate:[authGuardGuard]
    },
    {
        path:"adminDash",
        component:AdminDashboardComponent,
        title:"Admin Dashboard"
    },
    {
        path:"supplierWait",
        component:SupplierWaitingComponent,
        title:"Approval"
    },
    {
        path:"product",
        component:ProductComponent,
        title:"Product | Info"
    },
    {
        path:"fav",
        component:FavouriteComponent,
        title:"Favourite | Dash"
    },
    {
        path:"proCat/:category",
        component:ProductByCatComponent,
        title:"Product | Category"
    },
    {
        path:"cart",
        component:CartInfoComponent,
        title:"Cart"
    },
    {
        path:"forgotPass",
        component:ForgotPasswordComponent,
        title:"Forgot | Password"
    },
    {
        path:"about",
        component:AboutUsComponent,
        title:"About Us"
    },
    {
        path:"contact",
        component:ContactComponent,
        title:"Contact Us"
    }
];
