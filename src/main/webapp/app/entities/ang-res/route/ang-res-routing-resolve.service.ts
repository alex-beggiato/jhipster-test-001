import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAngRes, AngRes } from '../ang-res.model';
import { AngResService } from '../service/ang-res.service';

@Injectable({ providedIn: 'root' })
export class AngResRoutingResolveService implements Resolve<IAngRes> {
  constructor(protected service: AngResService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAngRes> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((angRes: HttpResponse<AngRes>) => {
          if (angRes.body) {
            return of(angRes.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AngRes());
  }
}
