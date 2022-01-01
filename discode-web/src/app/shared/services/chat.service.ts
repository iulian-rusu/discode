import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root',
})
export class ChatService {
  constructor(private readonly httpClient: HttpClient) {}

  private url: string = 'http://localhost:8008/api/chats';

  public getChatMembers(chatId: BigInteger): Observable<HttpResponse<any>> {
    return this.httpClient.get<HttpResponse<any>>(
      this.url + "/" + chatId + "/members",
      {
        observe: 'response',
      }
    );
  }
  public createChat(ownerId: string, chatName: string): Observable<HttpResponse<any>> {
    const data = {ownerId: ownerId, chatName: chatName};
    return this.httpClient.post<HttpResponse<any>>(
      this.url, data,
      {
        observe: 'response',
      }
    );
  }
}
