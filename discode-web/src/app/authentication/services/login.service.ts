import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginModel } from '../models/login.model';

@Injectable({
  providedIn: 'root'
})
export class LoginService {
  constructor(private readonly httpClient: HttpClient) {}
  private url: string = 'http://localhost:8008/api/auth'

  public login(data: LoginModel): Observable<HttpResponse<any>> {
    return this.httpClient.post<HttpResponse<any>>(this.url, data, {
      observe: 'response',
    });
  }

}
