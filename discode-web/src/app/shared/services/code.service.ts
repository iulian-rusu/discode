import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root',
})
export class CodeService {
  constructor(private readonly httpClient: HttpClient) {}

  private url: string = 'http://localhost:8008/api/code-execution';

  public getLanguages(): Observable<HttpResponse<any>> {
    return this.httpClient.get<HttpResponse<any>>(this.url + '/languages', {
      observe: 'response',
    });
  }

  public runCode(
    messageId: BigInteger,
    language: string
  ): Observable<HttpResponse<any>> {
    const data = { messageId: messageId, language: language };
    return this.httpClient.post<HttpResponse<any>>(this.url, data, {
      observe: 'response',
    });
  }
}
