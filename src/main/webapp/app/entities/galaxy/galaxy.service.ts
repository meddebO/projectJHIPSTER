import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IGalaxy } from 'app/shared/model/galaxy.model';

type EntityResponseType = HttpResponse<IGalaxy>;
type EntityArrayResponseType = HttpResponse<IGalaxy[]>;

@Injectable({ providedIn: 'root' })
export class GalaxyService {
  public resourceUrl = SERVER_API_URL + 'api/galaxies';

  constructor(protected http: HttpClient) {}

  create(galaxy: IGalaxy): Observable<EntityResponseType> {
    return this.http.post<IGalaxy>(this.resourceUrl, galaxy, { observe: 'response' });
  }

  update(galaxy: IGalaxy): Observable<EntityResponseType> {
    return this.http.put<IGalaxy>(this.resourceUrl, galaxy, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGalaxy>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGalaxy[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
