import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private readonly httpClient: HttpClient) {}

  private url: string = 'http://localhost:8008/api/users';

  public getUsers(): Observable<HttpResponse<any>> {
    return this.httpClient.get<HttpResponse<any>>(this.url, {
      observe: 'response',
    });
  }
}
