import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private readonly httpClient: HttpClient) {}

  private url: string = 'http://localhost:8008/api/';

  public getUsers(): Observable<HttpResponse<any>> {
    return this.httpClient.get<HttpResponse<any>>(this.url + 'users', {
      observe: 'response',
    });
  }

  public updateUser(userId: string, data: User): Observable<HttpResponse<any>> {
    return this.httpClient.patch<HttpResponse<any>>(
      this.url + 'users/' + userId,
      data,
      {
        observe: 'response',
      }
    );
  }

  public getUser(userId: string): Observable<HttpResponse<any>> {
    return this.httpClient.get<HttpResponse<any>>(
      this.url + 'users/' + userId,
      {
        observe: 'response',
      }
    );
  }

  public getProfileImage(imagePath: string): Observable<any> {
    let httpHeaders = new HttpHeaders().set('Accept', 'image/webp,*/*');
    return this.httpClient.get<Blob>(this.url + 'images/' + imagePath, {
      headers: httpHeaders,
      responseType: 'blob' as 'json',
    });
  }

  public getChats(userId: string): Observable<HttpResponse<any>> {
    return this.httpClient.get<HttpResponse<any>>(
      this.url + 'users/' + userId + '/chats',
      {
        observe: 'response',
      }
    );
  }
}
