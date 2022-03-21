import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAngGrp, AngGrp } from '../ang-grp.model';
import { AngGrpService } from '../service/ang-grp.service';

@Injectable({ providedIn: 'root' })
export class AngGrpRoutingResolveService implements Resolve<IAngGrp> {
  constructor(protected service: AngGrpService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAngGrp> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((angGrp: HttpResponse<AngGrp>) => {
          if (angGrp.body) {
            return of(angGrp.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AngGrp());
  }
}
