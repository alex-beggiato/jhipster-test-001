import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAngGrp, getAngGrpIdentifier } from '../ang-grp.model';

export type EntityResponseType = HttpResponse<IAngGrp>;
export type EntityArrayResponseType = HttpResponse<IAngGrp[]>;

@Injectable({ providedIn: 'root' })
export class AngGrpService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ang-grps');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(angGrp: IAngGrp): Observable<EntityResponseType> {
    return this.http.post<IAngGrp>(this.resourceUrl, angGrp, { observe: 'response' });
  }

  update(angGrp: IAngGrp): Observable<EntityResponseType> {
    return this.http.put<IAngGrp>(`${this.resourceUrl}/${getAngGrpIdentifier(angGrp) as number}`, angGrp, { observe: 'response' });
  }

  partialUpdate(angGrp: IAngGrp): Observable<EntityResponseType> {
    return this.http.patch<IAngGrp>(`${this.resourceUrl}/${getAngGrpIdentifier(angGrp) as number}`, angGrp, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAngGrp>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAngGrp[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAngGrpToCollectionIfMissing(angGrpCollection: IAngGrp[], ...angGrpsToCheck: (IAngGrp | null | undefined)[]): IAngGrp[] {
    const angGrps: IAngGrp[] = angGrpsToCheck.filter(isPresent);
    if (angGrps.length > 0) {
      const angGrpCollectionIdentifiers = angGrpCollection.map(angGrpItem => getAngGrpIdentifier(angGrpItem)!);
      const angGrpsToAdd = angGrps.filter(angGrpItem => {
        const angGrpIdentifier = getAngGrpIdentifier(angGrpItem);
        if (angGrpIdentifier == null || angGrpCollectionIdentifiers.includes(angGrpIdentifier)) {
          return false;
        }
        angGrpCollectionIdentifiers.push(angGrpIdentifier);
        return true;
      });
      return [...angGrpsToAdd, ...angGrpCollection];
    }
    return angGrpCollection;
  }
}
