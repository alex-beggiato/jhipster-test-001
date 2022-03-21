import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAngBdg, getAngBdgIdentifier } from '../ang-bdg.model';

export type EntityResponseType = HttpResponse<IAngBdg>;
export type EntityArrayResponseType = HttpResponse<IAngBdg[]>;

@Injectable({ providedIn: 'root' })
export class AngBdgService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ang-bdgs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(angBdg: IAngBdg): Observable<EntityResponseType> {
    return this.http.post<IAngBdg>(this.resourceUrl, angBdg, { observe: 'response' });
  }

  update(angBdg: IAngBdg): Observable<EntityResponseType> {
    return this.http.put<IAngBdg>(`${this.resourceUrl}/${getAngBdgIdentifier(angBdg) as number}`, angBdg, { observe: 'response' });
  }

  partialUpdate(angBdg: IAngBdg): Observable<EntityResponseType> {
    return this.http.patch<IAngBdg>(`${this.resourceUrl}/${getAngBdgIdentifier(angBdg) as number}`, angBdg, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAngBdg>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAngBdg[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAngBdgToCollectionIfMissing(angBdgCollection: IAngBdg[], ...angBdgsToCheck: (IAngBdg | null | undefined)[]): IAngBdg[] {
    const angBdgs: IAngBdg[] = angBdgsToCheck.filter(isPresent);
    if (angBdgs.length > 0) {
      const angBdgCollectionIdentifiers = angBdgCollection.map(angBdgItem => getAngBdgIdentifier(angBdgItem)!);
      const angBdgsToAdd = angBdgs.filter(angBdgItem => {
        const angBdgIdentifier = getAngBdgIdentifier(angBdgItem);
        if (angBdgIdentifier == null || angBdgCollectionIdentifiers.includes(angBdgIdentifier)) {
          return false;
        }
        angBdgCollectionIdentifiers.push(angBdgIdentifier);
        return true;
      });
      return [...angBdgsToAdd, ...angBdgCollection];
    }
    return angBdgCollection;
  }
}
