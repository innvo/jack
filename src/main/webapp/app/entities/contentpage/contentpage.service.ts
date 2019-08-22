import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IContentpage } from 'app/shared/model/contentpage.model';

type EntityResponseType = HttpResponse<IContentpage>;
type EntityArrayResponseType = HttpResponse<IContentpage[]>;

@Injectable({ providedIn: 'root' })
export class ContentpageService {
  public resourceUrl = SERVER_API_URL + 'api/contentpages';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/contentpages';

  constructor(protected http: HttpClient) {}

  create(contentpage: IContentpage): Observable<EntityResponseType> {
    return this.http.post<IContentpage>(this.resourceUrl, contentpage, { observe: 'response' });
  }

  update(contentpage: IContentpage): Observable<EntityResponseType> {
    return this.http.put<IContentpage>(this.resourceUrl, contentpage, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IContentpage>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContentpage[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IContentpage[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
