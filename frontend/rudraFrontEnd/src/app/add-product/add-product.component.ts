import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProductService } from '../service/product.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-add-product',
  standalone: true,
  imports: [FormsModule,ReactiveFormsModule,CommonModule],
  templateUrl: './add-product.component.html',
  styleUrl: './add-product.component.css'
})
export class AddProductComponent implements OnInit{

  userEmail:any;
  productForm:any;
  selectedImage: any = File;

  constructor(
        private fb:FormBuilder,
        private productService:ProductService,
        private _snackBar:MatSnackBar,
        private userSer:UserService
        ){}

  ngOnInit(): void {
    this.userSer.getUserData().subscribe({
      next:(data:any)=>{
        this.userEmail=data.userEmail;//getting
      }
    })

    this.productForm = this.fb.group({
      category:["",Validators.required],
      productName:["",Validators.required],
      productPrice:["",Validators.required],
      quantity:["",Validators.required],
      productRating:["",Validators.required],
      description:["",Validators.required],
    })
  }

  


  getProductId(){
    return this.productForm.get('productId')?.value;
  }

  onFileSelect(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedImage = input.files[0];
    }
  }

  onSubmit(){
    const productData = this.productForm.value;
    const formData = new FormData();

    formData.append('productData', JSON.stringify(productData));
    formData.append('file', this.selectedImage);
    

    this.productService.addProduct(formData).subscribe({ 
      next:data =>{
        this._snackBar.open('Product added successfully.....', 'success', {
          duration: 2000,
          panelClass: ['mat-toolbar', 'mat-primary']
        });
      },
      error:err=>{
        this._snackBar.open('Product is not added.....', 'Failure', {
          duration: 2000,
          panelClass: ['mat-toolbar', 'mat-primary']
        });
      }
    })
  }
}
