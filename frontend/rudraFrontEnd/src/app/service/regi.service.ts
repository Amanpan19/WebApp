import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class RegiService {
  userUrl:string='http://localhost:9000/api/v1/userService';

  constructor(private httpClient:HttpClient) { }

  registerUser(formData:FormData){
    return this.httpClient.post(`${this.userUrl}/register/user`,formData)
  }
}
