import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAngBdg, AngBdg } from '../ang-bdg.model';
import { AngBdgService } from '../service/ang-bdg.service';

@Injectable({ providedIn: 'root' })
export class AngBdgRoutingResolveService implements Resolve<IAngBdg> {
  constructor(protected service: AngBdgService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAngBdg> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((angBdg: HttpResponse<AngBdg>) => {
          if (angBdg.body) {
            return of(angBdg.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AngBdg());
  }
}
