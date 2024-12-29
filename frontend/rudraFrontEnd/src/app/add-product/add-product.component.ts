import { CommonModule } from '@angular/common';
import { Component, ElementRef, OnInit, Renderer2 } from '@angular/core';
import { FormArray, FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProductService } from '../service/product.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserService } from '../service/user.service';
import { productInfo } from '../model/productInfo';


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
  productInfo!:productInfo;
  selectedImage: any = File;
  clothTypes = ['Cotton', 'Linen', 'Silk', 'Polyester'];

  constructor(
        private fb:FormBuilder,
        private productService:ProductService,
        private _snackBar:MatSnackBar,
        private userSer:UserService,
        private el: ElementRef,
        private renderer: Renderer2
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
      productDetails: this.fb.group({
        clothType: ['', Validators.required],
        availableSize: this.fb.array([], Validators.required), 
        return14DayAvailability: [false, Validators.required],
        colorsAvail: this.fb.array([], Validators.required),
        fashionType: ['', Validators.required],
      })
    })
  }
  

  get availableSize(): FormArray {
    return this.productForm.get('productDetails.availableSize') as FormArray;
  }

  get colorsAvail(): FormArray {
    return this.productForm.get('productDetails.colorsAvail') as FormArray;
  }

  // Add a new color
  addColor(color: string) {
    this.colorsAvail.push(this.fb.control(color, Validators.required));
  }

  // Remove a color
  removeColor(index: number) {
    this.colorsAvail.removeAt(index);
  }

  onCheckboxChange(event: Event): void {
    const checkbox = event.target as HTMLInputElement;
    if (checkbox.checked) {
      this.availableSize.push(this.fb.control(checkbox.value));
    } else {
      const index = this.availableSize.controls.findIndex(control => control.value === checkbox.value);
      if (index !== -1) {
        this.availableSize.removeAt(index);
      }
    }
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

        this.productForm.reset();
        this.selectedImage = null;
  
        this.availableSize.clear();
        this.colorsAvail.clear();

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
