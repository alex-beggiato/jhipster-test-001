import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAngRes, getAngResIdentifier } from '../ang-res.model';

export type EntityResponseType = HttpResponse<IAngRes>;
export type EntityArrayResponseType = HttpResponse<IAngRes[]>;

@Injectable({ providedIn: 'root' })
export class AngResService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ang-res');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(angRes: IAngRes): Observable<EntityResponseType> {
    return this.http.post<IAngRes>(this.resourceUrl, angRes, { observe: 'response' });
  }

  update(angRes: IAngRes): Observable<EntityResponseType> {
    return this.http.put<IAngRes>(`${this.resourceUrl}/${getAngResIdentifier(angRes) as number}`, angRes, { observe: 'response' });
  }

  partialUpdate(angRes: IAngRes): Observable<EntityResponseType> {
    return this.http.patch<IAngRes>(`${this.resourceUrl}/${getAngResIdentifier(angRes) as number}`, angRes, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAngRes>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAngRes[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAngResToCollectionIfMissing(angResCollection: IAngRes[], ...angResToCheck: (IAngRes | null | undefined)[]): IAngRes[] {
    const angRes: IAngRes[] = angResToCheck.filter(isPresent);
    if (angRes.length > 0) {
      const angResCollectionIdentifiers = angResCollection.map(angResItem => getAngResIdentifier(angResItem)!);
      const angResToAdd = angRes.filter(angResItem => {
        const angResIdentifier = getAngResIdentifier(angResItem);
        if (angResIdentifier == null || angResCollectionIdentifiers.includes(angResIdentifier)) {
          return false;
        }
        angResCollectionIdentifiers.push(angResIdentifier);
        return true;
      });
      return [...angResToAdd, ...angResCollection];
    }
    return angResCollection;
  }
}
